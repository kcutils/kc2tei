import kc2tei.lexer.Lexer;
import kc2tei.node.*;
import kc2tei.parser.Parser;
import kc2tei.analysis.DepthFirstAdapter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.PushbackReader;
import java.util.Scanner;

public class GrammarTest {

  private int filenameCounter = 0;
  private int noiseCounter = 0;
  private int vocalNoiseCounter = 0;
  private int sentenceStartCounter = 0;
  private int punctuationCounter = 0;

  private int wordCounter = 0;
  private int sentencePunctuationCounter = 0;
  private int wordBoundaryCounter = 0;


  private Start lexParse(File file) throws Exception {

    // create lexer
    MyLexer l = new MyLexer(new PushbackReader(new FileReader(file), 1024));

    // print states and tokens
    while (true) {
      Token t = l.next();
      if (t instanceof EOF) {
        break;
      }
      // DEBUG
//      System.out.println("State: " + l.getStateId() + ", Token type: " + t.getClass() + ", Token: '" + t.getText() + "'");

      classCounter(t.getClass());
    }

    // create new lexer with new file reader which reads from beginning of the file again
    l = new MyLexer(new PushbackReader(new FileReader(file), 1024));

    // create parser
    Parser p = new Parser(l);

    // return parsed content
    return p.parse();
  }

  private void classCounter (Class c) {
    if (c == kc2tei.node.TFilename.class) {
      filenameCounter++;
    }
    // OK, not all of these things are noises ...
    if (c == kc2tei.node.TBodyNoise.class || c == kc2tei.node.TExternalNoise.class || c == kc2tei.node.TBreathing.class || c == kc2tei.node.TPause.class || c == kc2tei.node.THesistationalLengthning.class) {
      noiseCounter++;
    }
    if (c == kc2tei.node.TVocalNoiseH.class) {
      vocalNoiseCounter++;
    }
    if (c == kc2tei.node.TSentenceStartSymbol.class) {
      sentenceStartCounter++;
    }
    if (c == kc2tei.node.TPunctuation.class) {
      punctuationCounter++;
    }
    if (c == kc2tei.node.TWordBoundaryPrefix.class) {
      wordBoundaryCounter++;
    }
  }

  @Test
  public void processTestFiles() throws Exception {
    Scanner read = new Scanner (new File(getClass().getResource("testFiles.conf").getFile()));

    String fileName;
    Integer expectedNoises, expectedVocalNoises, expectedSentenceStarts, expectedSentencePunctuations, expectedPunctuations, expectedWords, expectedWordBoundaries;

    read.useDelimiter(",");

    while (read.hasNext()) {
      fileName = read.next();
      if (fileName.trim().startsWith("#")) {
        read.nextLine();
        continue;
      }
      expectedNoises = Integer.valueOf(read.next());
      expectedVocalNoises = Integer.valueOf(read.next());
      expectedSentenceStarts = Integer.valueOf(read.next());
      expectedSentencePunctuations = Integer.valueOf(read.next());
      expectedPunctuations = Integer.valueOf(read.next());
      expectedWords = Integer.valueOf(read.next());
      expectedWordBoundaries = Integer.valueOf(read.next());

      testLexParse(fileName, expectedNoises, expectedVocalNoises, expectedSentenceStarts, expectedSentencePunctuations, expectedPunctuations, expectedWords, expectedWordBoundaries);

    }

    read.close();

  }


  public void testLexParse(String fileName, Integer expectedNoises, Integer expectedVocalNoises, Integer expectedSentenceStarts, Integer expectedSentencePunctuations, Integer expectedPunctuations, Integer expectedWords, Integer expectedWordBoundaries) throws Exception {

    System.out.println("\n\n\n----------------------------------------------------------------------");
    System.out.println("Processing " + fileName + " ...\n");

    filenameCounter = 0;
    noiseCounter = 0;
    vocalNoiseCounter= 0;
    sentenceStartCounter = 0;
    sentencePunctuationCounter = 0;
    punctuationCounter = 0;
    wordCounter = 0;
    wordBoundaryCounter = 0;

    //Start tree = lexParse(new File(getClass().getResource("/sample_kc_file.s1h").getFile()));
    Start tree = lexParse(new File(getClass().getResource(fileName).getFile()));

    Assert.assertNotNull(tree); // parsing went wrong?!
    tree.apply(new testTranslator());

    System.out.println(filenameCounter + " filenames found.");
    System.out.println(noiseCounter + " noises in orthography found, " + expectedNoises + " expected.");
    System.out.println(vocalNoiseCounter + " vocal noises found, " + expectedVocalNoises + " expected.");
    System.out.println(sentenceStartCounter + " sentence starts found, " + expectedSentenceStarts + " expected.");
    System.out.println(sentencePunctuationCounter + " sentence punctuations found, " + expectedSentencePunctuations + " expected.");
    System.out.println(punctuationCounter + " punctuations found, " + expectedPunctuations + " expected.");
    System.out.println(wordCounter + " words found, " + expectedWords + " expected.");
    System.out.println(wordBoundaryCounter + " word boundaries found, " + expectedWordBoundaries + " expected.");

    Assert.assertTrue(noiseCounter == expectedNoises);
    //TODO Assert.assertTrue(vocalNoiseCounter == expectedVocalNoises);
    Assert.assertTrue(sentenceStartCounter == expectedSentenceStarts);
    Assert.assertTrue(sentencePunctuationCounter == expectedSentencePunctuations);
    Assert.assertTrue(punctuationCounter == expectedPunctuations);
    Assert.assertTrue(wordCounter == expectedWords);
    Assert.assertTrue(wordBoundaryCounter == expectedWordBoundaries);

    Assert.assertTrue(filenameCounter == 1); // each file has at least one filename
    if (sentenceStartCounter > 0) {
      //  Assert.assertTrue(punctuationCounter >= sentenceStartCounter);
      Assert.assertTrue(sentencePunctuationCounter == sentenceStartCounter); // each starting sentence ends
      Assert.assertTrue(punctuationCounter >= sentencePunctuationCounter); // a sentence punctuation is a punctuation
    }
    Assert.assertTrue(wordBoundaryCounter == wordCounter );

    System.out.println("\nEnd of processing of " + fileName + " .");
    System.out.println("----------------------------------------------------------------------");

  }

  public class MyLexer extends Lexer {
    public MyLexer(java.io.PushbackReader in) {
      super(in);
    }

    public int getStateId() {
      return this.state.id();
    }
  }

  public class testTranslator extends DepthFirstAdapter {

    public void caseTWord(TWord node) {
      wordCounter++;
    }

    public void caseTHesistation(THesistation node) {
      wordCounter++;
    }

    public void caseACase1SentencePunctuation(ACase1SentencePunctuation node) {
      sentencePunctuationCounter++;
    }

    public void caseACase2SentencePunctuation(ACase2SentencePunctuation node) {
      sentencePunctuationCounter++;
    }
  }
}

