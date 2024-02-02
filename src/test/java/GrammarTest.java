import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

/**
 * Class for testing if the lexer and parsers generated from grammars
 * recognize things as expected by a human lexer/parser.
 */
public class GrammarTest {

  private static final String TEST_CONF_FILE = "testFiles.conf";

  private int filenameCounter = 0;
  private int noiseCounter = 0;
  private int vocalNoiseCounter = 0;
  private int sentenceStartCounter = 0;
  private int punctuationCounter = 0;

  private int wordCounter = 0;
  private int sentencePunctuationCounter = 0;
  private int wordBoundaryCounter = 0;

  /**
   * Read the test configuration file and get from it files to check lexer/parser against and
   * expected values lexer/parser should produce if they do their business right.
   */
  @Test
  public void processTestFiles () throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader(new File(getClass().getResource(TEST_CONF_FILE).getFile())));

    String line = null;
    Scanner scanner = null;
    int i = 0;
    String fileName = null;
    int expectedNoises;
    int expectedVocalNoises;
    int expectedSentenceStarts;
    int expectedSentencePunctuations;
    int expectedPunctuations;
    int expectedWords;
    int expectedWordBoundaries;

    while ((line = reader.readLine()) != null) {
      scanner = new Scanner(line);
      scanner.useDelimiter(",");

      i = 0;
      fileName = null;
      expectedNoises = 0;
      expectedVocalNoises = 0;
      expectedSentenceStarts = 0;
      expectedSentencePunctuations = 0;
      expectedPunctuations = 0;
      expectedWords = 0;
      expectedWordBoundaries = 0;

      while (scanner.hasNext()) {
        String field = scanner.next();
        if (field.trim().startsWith("#")) {
          // comment line found
          break;
        }
        // at the moment our csv file has 8 fields
        switch (i) {
          case 0: fileName = field;
                  break;
          case 1: expectedNoises = Integer.parseInt(field);
                  break;
          case 2: expectedVocalNoises = Integer.parseInt(field);
                  break;
          case 3: expectedSentenceStarts = Integer.parseInt(field);
                  break;
          case 4: expectedSentencePunctuations = Integer.parseInt(field);
                  break;
          case 5: expectedPunctuations = Integer.parseInt(field);
                  break;
          case 6: expectedWords = Integer.parseInt(field);
                  break;
          case 7: expectedWordBoundaries = Integer.parseInt(field);
                  break;
          default:
                  // ignore additional fields/values
                  break;
        }

        i++;
      }
      if (scanner != null) {
        scanner.close();
      }
      if (fileName != null) {
        testLexParse(fileName, expectedNoises, expectedVocalNoises, expectedSentenceStarts, expectedSentencePunctuations, expectedPunctuations, expectedWords, expectedWordBoundaries);
      }
    }
   reader.close();

  }

  /**
   * Lex and parse file and compare found values with expected values
   */
  public void testLexParse (String fileName, Integer expectedNoises, Integer expectedVocalNoises, Integer expectedSentenceStarts, Integer expectedSentencePunctuations, Integer expectedPunctuations, Integer expectedWords, Integer expectedWordBoundaries) throws Exception {

    System.out.println("\n\n\n----------------------------------------------------------------------");
    System.out.println("Processing " + fileName + " ...\n");

    filenameCounter = 0;
    noiseCounter = 0;
    vocalNoiseCounter = 0;
    sentenceStartCounter = 0;
    sentencePunctuationCounter = 0;
    punctuationCounter = 0;
    wordCounter = 0;
    wordBoundaryCounter = 0;

    transliteration.node.Start translitTree = lexParseTranslit(new File(getClass().getResource(fileName).getFile()));

    Assert.assertNotNull(translitTree); // parsing went wrong?!
    translitTree.apply(new testTranslitTranslator());

    labels.node.Start labelsTree = lexParseLabels(new File(getClass().getResource(fileName).getFile()));

    Assert.assertNotNull(labelsTree); // parsing went wrong?!
    labelsTree.apply(new testLabelsTranslator());

    System.out.println(filenameCounter + "/1 filenames (transliteration)");
    System.out.println(noiseCounter + "/" + expectedNoises + " noises (transliteration)");
    System.out.println(vocalNoiseCounter + "/" + expectedVocalNoises + " vocal noises (transliteration)");
    System.out.println(sentenceStartCounter + "/" + expectedSentenceStarts + " sentence starts (labels)");
    System.out.println(sentencePunctuationCounter + "/" + expectedSentencePunctuations + " sentence punctuations (labels)");
    System.out.println(punctuationCounter + "/" + expectedPunctuations + " punctuations (transliteration)");
    System.out.println(wordCounter + "/" + expectedWords + " words (transliteration)");
    System.out.println(wordBoundaryCounter + "/" + expectedWordBoundaries + " word boundaries (labels)");

    Assert.assertEquals(1, filenameCounter);
    Assert.assertEquals(noiseCounter, (int) expectedNoises);
    Assert.assertEquals(vocalNoiseCounter, (int) expectedVocalNoises);
    Assert.assertEquals(sentenceStartCounter, (int) expectedSentenceStarts);
    Assert.assertEquals(sentencePunctuationCounter, (int) expectedSentencePunctuations);
    Assert.assertEquals(punctuationCounter, (int) expectedPunctuations);
    Assert.assertEquals(wordCounter, (int) expectedWords);
    Assert.assertEquals(wordBoundaryCounter, (int) expectedWordBoundaries);


    System.out.println("\nEnd of processing of " + fileName + " .");
    System.out.println("----------------------------------------------------------------------");

  }

  /**
   * Generate lexer and parser for transliteration section
   *
   * @param file  the file that should be lexed/parsed
   * @return Start node of tree generated by parser
   * @throws IOException  on file write/read problems
   * @throws transliteration.lexer.LexerException  on lexer problems
   * @throws transliteration.parser.ParserException  on parser problems
   */
  private transliteration.node.Start lexParseTranslit (File file) throws IOException, transliteration.lexer.LexerException, transliteration.parser.ParserException {
    // create lexer
    transliteration.lexer.Lexer l = new transliteration.lexer.Lexer(new PushbackReader(new FileReader(file), 1024));

    // create new lexer with new file reader which reads from beginning of the file again
    l = new transliteration.lexer.Lexer(new PushbackReader(new FileReader(file), 1024));

    // create parser
    transliteration.parser.Parser p = new transliteration.parser.Parser(l);

    // return parsed content
    return p.parse();
  }

  /**
   * Generate lexer and parser for labels section
   *
   * @param file  the file that should be lexed/parsed
   * @return Start node of tree generated by parser
   * @throws IOException  on file write/read problems
   * @throws labels.lexer.LexerException  on lexer problems
   * @throws labels.parser.ParserException  on parser problems
   */
  private labels.node.Start lexParseLabels (File file) throws IOException, labels.lexer.LexerException, labels.parser.ParserException {
    // create lexer
    labels.lexer.Lexer l = new labels.lexer.Lexer(new PushbackReader(new FileReader(file), 1024));

    // create new lexer with new file reader which reads from beginning of the file again
    l = new labels.lexer.Lexer(new PushbackReader(new FileReader(file), 1024));

    // create parser
    labels.parser.Parser p = new labels.parser.Parser(l);

    // return parsed content
    return p.parse();
  }

  /**
   * pick some relevant data from transliteration section
   */
  public class testTranslitTranslator extends transliteration.analysis.DepthFirstAdapter {

    public void caseTFilename (transliteration.node.TFilename node) {
      filenameCounter++;
    }

    public void caseABodyNoiseTransliterationNonVocalUtterance (transliteration.node.ABodyNoiseTransliterationNonVocalUtterance node) {
      noiseCounter++;
    }

    public void caseAExternalNoiseTransliterationNonVocalUtterance (transliteration.node.AExternalNoiseTransliterationNonVocalUtterance node) {
      noiseCounter++;
    }

    public void caseABreathingTransliterationNonVocalUtterance (transliteration.node.ABreathingTransliterationNonVocalUtterance node) {
      noiseCounter++;
    }

    public void caseAHesitationTransliterationContent (transliteration.node.AHesitationTransliterationContent node) {
      vocalNoiseCounter++;
      wordCounter++;
    }

    public void caseTWord (transliteration.node.TWord node) {
      wordCounter++;
    }

    public void caseTHesitWord (transliteration.node.THesitWord node) {
      wordCounter++;
    }

    public void caseTPunctuation (transliteration.node.TPunctuation node) {
      punctuationCounter++;
    }

  }

  /**
   * pick some relevant data from label section
   */
  public class testLabelsTranslator extends labels.analysis.DepthFirstAdapter {

    public void caseTSentenceStartSymbol (labels.node.TSentenceStartSymbol node) {
      sentenceStartCounter++;
    }

    public void caseAFullStopSentencePunctuation (labels.node.AFullStopSentencePunctuation node) {
      sentencePunctuationCounter++;
    }

    public void caseAQuestionMarkSentencePunctuation (labels.node.AQuestionMarkSentencePunctuation node) {
      sentencePunctuationCounter++;
    }

    public void caseAExclamationMarkSentencePunctuation (labels.node.AExclamationMarkSentencePunctuation node) {
      sentencePunctuationCounter++;
    }

    public void caseTWordBoundaryPrefix (labels.node.TWordBoundaryPrefix node) {
      wordBoundaryCounter++;
    }
  }
}

