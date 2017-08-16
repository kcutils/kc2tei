import kc2tei.node.EOF;
import kc2tei.node.Token;
import kc2tei.parser.Parser;
import org.apache.commons.cli.*;

import java.io.FileReader;
import java.io.PushbackReader;

import kc2tei.lexer.Lexer;
import kc2tei.node.Start;

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

  private static TEIDoc teiDoc = null;

  public static void main(String[] args) throws Exception {

    processCmdLineArgs(args);

//    try {

      // create filereader
      FileReader fr = new FileReader(inputFileName);

      // create pushbackreader
      PushbackReader pbr = new PushbackReader(fr, 1024);

      // create lexer
      Lexer l = new Lexer(pbr);

      if (debugMode) {
        while (true) {
          Token t = l.next();
          if (t instanceof EOF) {
            break;
          }
          //System.out.println("State: " + l.getStateId() + ", Token type: " + t.getClass() + ", Token: '" + t.getText() + "'");
          System.out.println("Token type: " + t.getClass() + ", Token: '" + t.getText() + "'");
        }
        // create new lexer with new file reader and new pushback reader which reads from beginning of the file again
        fr = new FileReader(inputFileName);
        pbr = new PushbackReader(fr, 1024);
        l = new Lexer(pbr);
      }

      // create parser
      Parser p = new Parser(l);

      // parse input
      Start tree = p.parse();

      // pre-check for inconsistencies in file to process
      KCFileChecker check = null;

      check = new KCFileChecker();
      tree.apply(check);

      // apply translations

      TranslationAdapter t = null;

      t = new LabelTranslation(annotationElements);
      tree.apply(t);

      t = new WordTranslation(annotationElements);
      tree.apply(t);

      annotationElements.setTimeMarkerNames();

      annotationElements.refineTimedLabels();

      if (debugMode) {
        System.out.println(annotationElements + "\n");
      }

      KCSampaToIPAConverter charConverter = new KCSampaToIPAConverter();
      charConverter.setDebugMode(debugMode);

      teiDoc = new TEIDoc(annotationElements, charConverter);

      check.setEntriesNotInUnicodeTable(charConverter.getNoHits());

      if (! forceMode) {
        if (! check.noErrorsFound()) {
          System.out.print(check);
          System.exit(1);
        }
      }

      teiDoc.writeSout();

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
}
