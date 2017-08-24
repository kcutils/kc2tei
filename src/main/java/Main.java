import org.apache.commons.cli.*;

import java.io.*;

public class Main {

  private static String header = "Convert old Kiel Corpus files to standarized TEI file format.\n\n";
  private static String footer = "\nPlease report issues at TODO:ABCDEF";
  private static String progName = "kc2tei";
  private static String version = "TODO";
  private static String inputFileName;
  private static String outputFileName;

  private static Boolean debugMode = false;
  private static Boolean forceMode = false;

  private static AnnotationElementCollection annotationElements = new AnnotationElementCollection();

  private static File rngSchemaFile;

  private static TEIDoc teiDoc = null;

  public static void main(String[] args) throws Exception {

    processCmdLineArgs(args);

    rngSchemaFile = getResourceAsFile("TEIschema.rng");

//    try {

      // create filereader
      FileReader translitFr = new FileReader(inputFileName);
      FileReader labelsFr = new FileReader(inputFileName);

      // create pushbackreader
      PushbackReader translitPbr = new PushbackReader(translitFr, 1024);
      PushbackReader labelsPbr = new PushbackReader(labelsFr, 1024);

      // create lexer
      transliteration.lexer.Lexer translitLexer  = new transliteration.lexer.Lexer(translitPbr);
      labels.lexer.Lexer labelsLexer  = new labels.lexer.Lexer(labelsPbr);

      if (debugMode) {

        while (true) {
          transliteration.node.Token t = translitLexer.next();
          if (t instanceof transliteration.node.EOF) {
            break;
          }
          //System.out.println("State: " + l.getStateId() + ", Token type: " + t.getClass() + ", Token: '" + t.getText() + "'");
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
          //System.out.println("State: " + l.getStateId() + ", Token type: " + t.getClass() + ", Token: '" + t.getText() + "'");
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

      LabelTranslation l = new LabelTranslation(annotationElements);
      labelsTree.apply(l);

      WordTranslation w = new WordTranslation(annotationElements);
      translitTree.apply(w);

      annotationElements.refineCollection();

      if (debugMode) {
        System.out.println(annotationElements + "\n");
      }

      KCSampaToIPAConverter charConverter = new KCSampaToIPAConverter();
      charConverter.setDebugMode(debugMode);

      teiDoc = new TEIDoc(annotationElements, charConverter);

      // check for inconsistencies in file to process
      KCFileChecker check = new KCFileChecker(annotationElements, charConverter);

      if (! check.noErrorsFound()) {
        System.err.print(check);
        if (! forceMode) {
          System.exit(1);
        }
      }

      if (rngSchemaFile == null || !rngSchemaFile.exists()) {
        System.err.println("WARNING: TEI Schema file does not exist. The produced TEI output can not be validated!");
      } else {
        XMLValidator xVal = new XMLValidator(teiDoc, rngSchemaFile);

        // sadly validate() creates lots of output that cannot be silenced
        if (!xVal.validate()) {
          System.err.println("Invalid TEI output produced!");
          if (!forceMode) {
            System.exit(1);
          }
        }
      }

      System.out.print(teiDoc.toStringEx());

/*
    } catch (Exception e) {
      System.out.print("Error: ");
      System.out.println(e.getMessage());
      System.exit(1);
    }*/
  }

  private static void processCmdLineArgs(String[] args) {

    // define available options
    Option versionOp = OptionBuilder.withLongOpt("version")
                           .withDescription("Print the version of the application")
                           .create('v');
    Option helpOp = OptionBuilder.withLongOpt("help")
                        .withDescription("Print this output")
                        .create('h');
    Option inFileOp = OptionBuilder.withLongOpt("input")
                          .withDescription("The file to be processed")
                          .hasArg()
                          .withArgName("FILE")
                          .isRequired()
                          .create('i');
    Option outFileOp = OptionBuilder.withLongOpt("output")
                           .withDescription("The file to write output to")
                           .hasArg()
                           .withArgName("FILE")
                           .create('o');
    Option forceOp = OptionBuilder.withLongOpt("force")
                           .withDescription("produce TEI output even if there are errors in file to convert")
                           .create('f');
    Option debugOp = OptionBuilder.withLongOpt("debug")
                         .withDescription("run program in debug mode, do not produce TEI output")
                         .create('d');

    // we need two sets of options to be able to first check for some options
    // without getting exceptions that required options are not provided
    Options options = new Options();
    Options options1 = new Options();

    // place options in option sets
    options.addOption(helpOp);
    options.addOption(inFileOp);
    options.addOption(outFileOp);
    options.addOption(versionOp);
    options.addOption(forceOp);
    options.addOption(debugOp);
    options1.addOption(helpOp);
    options1.addOption(versionOp);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd = null;

    // first check for help option
    // do not handle exception
    try {
      cmd = parser.parse(options1, args);

      printUsage(formatter, cmd, options);
    } catch (Exception e) {
    }

    // reset cmd
    cmd = null;

    // then check for remaining options (including options we already checked for)
    try {
      cmd = parser.parse(options, args);

    } catch (Exception e) {
      // exception will be thrown if required options are not provided
      // or if provided option is not recognized
      System.out.println(e.getMessage());
      printUsage(formatter, cmd, options);
      System.exit(1);
    }

    printUsage(formatter, cmd, options);

    inputFileName = cmd.getOptionValue("input");
    forceMode = cmd.hasOption("f");
    debugMode = cmd.hasOption("d");
  }

  private static void printUsage(HelpFormatter formatter, CommandLine cmd, Options options) {

    if (cmd == null) {
      formatter.printHelp(progName, header, options, footer, true);
    } else {
      if (cmd.hasOption("h") || cmd.hasOption("v")) {
        if (cmd.hasOption("v")) {
          System.out.println("Version: " + version);
        }
        if (cmd.hasOption("h")) {
          formatter.printHelp(progName, header, options, footer, true);
        }
        System.exit(0);
      }
    }
  }

  // we need a resource file as File object not as stream object
  // because methods we use can only deal with Files not with Streams
  // files within jar packages can not be accessed directly as a File
  public static File getResourceAsFile(String resourcePath) throws Exception {
    File rval = null;
    InputStream in = new Main().getClass().getResourceAsStream(resourcePath);
    if (in == null) {
      return rval;
    }

    rval = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
    rval.deleteOnExit();

    FileOutputStream out = new FileOutputStream(rval);

    //copy stream
    byte[] buffer = new byte[1024];
    int len = in.read(buffer);
    while (len != -1) {
      out.write(buffer, 0, len);
      len = in.read(buffer);
    }

    return rval;
  }
}
