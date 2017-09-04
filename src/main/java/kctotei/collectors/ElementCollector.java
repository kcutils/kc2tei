package kctotei.collectors;

import kctotei.elements.AnnotationElementCollection;

import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

/**
 * An element collector creates lexer and parser and processes a Kiel Corpus file
 * with the help of these components.
 *
 * On the trees resulting from lexing and parsing separate classes collect
 * relevant informations.
 */

public class ElementCollector {

  private AnnotationElementCollection annotationElementCollection;
  private String inputFileName;

  private Boolean debugMode;

  private ElementCollector () {
    this.setAnnotationElementCollection(null);
    this.setInputFileName("");
    this.setDebugMode(false);
  }

  public ElementCollector (AnnotationElementCollection annotationElementCollection, String inputFileName, Boolean debugMode) throws transliteration.lexer.LexerException, labels.lexer.LexerException, transliteration.parser.ParserException, IOException, labels.parser.ParserException {
    this();
    this.setInputFileName(inputFileName);
    this.setAnnotationElementCollection(annotationElementCollection);
    this.setDebugMode(debugMode);
    this.lexAndParse();
  }

  private AnnotationElementCollection getAnnotationElementCollection () {
    return annotationElementCollection;
  }

  private void setAnnotationElementCollection (AnnotationElementCollection annotationElementCollection) {
    this.annotationElementCollection = annotationElementCollection;
  }

  private void setInputFileName (String inputFileName) {
    this.inputFileName = inputFileName;
  }

  private void setDebugMode (Boolean debugMode) {
    this.debugMode = debugMode;
  }

  private void lexAndParse () throws IOException, transliteration.lexer.LexerException, labels.lexer.LexerException, transliteration.parser.ParserException, labels.parser.ParserException {
    // create file reader
    FileReader translitFr = new FileReader(inputFileName);
    FileReader labelsFr = new FileReader(inputFileName);

    // create push back reader
    PushbackReader translitPbr = new PushbackReader(translitFr, 1024);
    PushbackReader labelsPbr = new PushbackReader(labelsFr, 1024);

    // create lexer
    transliteration.lexer.Lexer translitLexer = new transliteration.lexer.Lexer(translitPbr);
    labels.lexer.Lexer labelsLexer = new labels.lexer.Lexer(labelsPbr);

    if (debugMode) {

      while (true) {
        transliteration.node.Token t = translitLexer.next();
        if (t instanceof transliteration.node.EOF) {
          break;
        }
        System.out.println("Token type: " + t.getClass() + ", Token: '" + t.getText() + "'");
      }
      // create new lexer with new file reader and new pushback reader which reads from beginning of the file again
      translitFr = new FileReader(inputFileName);
      translitPbr = new PushbackReader(translitFr, 1024);
      translitLexer = new transliteration.lexer.Lexer(translitPbr);

      while (true) {
        labels.node.Token t = labelsLexer.next();
        if (t instanceof labels.node.EOF) {
          break;
        }
        System.out.println("Token type: " + t.getClass() + ", Token: '" + t.getText() + "'");
      }
      // create new lexer with new file reader and new pushback reader which reads from beginning of the file again
      labelsFr = new FileReader(inputFileName);
      labelsPbr = new PushbackReader(labelsFr, 1024);
      labelsLexer = new labels.lexer.Lexer(labelsPbr);

    }

    // create parser
    transliteration.parser.Parser translitParser = new transliteration.parser.Parser(translitLexer);
    labels.parser.Parser labelsParser = new labels.parser.Parser(labelsLexer);

    // parse input
    transliteration.node.Start translitTree = translitParser.parse();
    labels.node.Start labelsTree = labelsParser.parse();

    // apply translations

    LabelTranslation l = new LabelTranslation(this.getAnnotationElementCollection());
    labelsTree.apply(l);

    WordTranslation w = new WordTranslation(this.getAnnotationElementCollection());
    translitTree.apply(w);
  }

}
