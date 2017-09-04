package kctotei;

import kctotei.checkers.ResultChecker;
import kctotei.collectors.ElementCollector;
import kctotei.elements.AnnotationElementCollection;
import kctotei.elements.TEIDoc;
import kctotei.postCollectProcessors.PostCollectProcessor;
import org.apache.commons.cli.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jaxen.JaxenException;
import org.xml.sax.SAXException;

import java.io.*;

class Main {

  private static final String HEADER = "Convert old Kiel Corpus files to standardized TEI file format.\n\n";
  private static final String FOOTER = "\nPlease report issues at TODO:ABCDEF";
  private static final String PROG_NAME = "kc2tei";
  private static final String VERSION = "TODO";
  private static String inputFileName;
  private static String outputFileName;

  private static final String RNG_SCHEMA_FILE_NAME = "TEIschema.rng";

  private static Boolean debugMode = false;
  private static Boolean forceMode = false;

  private static final AnnotationElementCollection ANNOTATION_ELEMENT_COLLECTION = new AnnotationElementCollection();

  private Main () {}

  /**
   * Conversion of Kiel Corpus files is done in five steps:
   * <p>
   * Firstly, all relevant elements will be collected from a provided Kiel Corpus file.
   * Then, the collected elements will be refined only by using information from collected elements.
   * With these refined elements a XML document is being created. While creating this document
   * phonetic information denoted in XSAMPA is being translated into IPA (Unicode) equivalents.
   * When this conversion process is done, checkers check if the conversion was done without errors
   * and if the resulting output is a proper TEI XML document.
   * This document can then be written to a file or on console.
   * </p>
   * For most of these conversion steps there are separate classes/objects that do the work
   * while this main method only calls them in the right order.
   *
   * @param args  given parameters
   * @throws transliteration.lexer.LexerException   on problems while lexing the transliteration content
   * @throws labels.lexer.LexerException            on problems while lexing the label content
   * @throws transliteration.parser.ParserException on problems while parsing the transliteration content
   * @throws labels.parser.ParserException          on problems while parsing the label content
   * @throws IOException                            on problems with reading/writing files
   * @throws JaxenException                         on XML problems, e.g. finding nodes, building elements
   * @throws SAXException                           on XML problems
   */
  public static void main (String[] args) throws transliteration.lexer.LexerException, labels.lexer.LexerException, transliteration.parser.ParserException, IOException, JaxenException, SAXException, labels.parser.ParserException {

    processCmdLineArgs(args);


    new ElementCollector(ANNOTATION_ELEMENT_COLLECTION, inputFileName, debugMode);

    if (debugMode) {
      System.out.println("Collected elements:");
      System.out.println(ANNOTATION_ELEMENT_COLLECTION + "\n");
    }

    PostCollectProcessor pCp = new PostCollectProcessor(ANNOTATION_ELEMENT_COLLECTION);

    if (debugMode) {
      System.out.println("Post-processed elements:");
      System.out.println(ANNOTATION_ELEMENT_COLLECTION + "\n");
    }

    KCSampaToIPAConverter charConverter = new KCSampaToIPAConverter(debugMode);

    TEIDoc teiDoc = new TEIDoc(ANNOTATION_ELEMENT_COLLECTION, charConverter);

    File rngSchemaFile = getResourceAsFile(RNG_SCHEMA_FILE_NAME);
    if (rngSchemaFile == null || !rngSchemaFile.exists()) {
      System.err.println("WARNING: TEI Schema file does not exist. The produced TEI output can not be validated!");
    }

    ResultChecker rC = new ResultChecker(pCp, charConverter, rngSchemaFile, teiDoc);

    if (! rC.noErrorsFound()) {
      System.err.print(rC);
      if (! forceMode) {
        System.exit(1);
      }
    }

    if (outputFileName != null && ! outputFileName.equals("")) {
      writeTEIDocToFile(teiDoc, outputFileName);
    } else {
      System.out.print(teiDoc.toStringEx());
    }

  }

  /**
   * This method firstly generates option sets of available options.
   * Then it parses the provided commandline arguments and checks if they are valid.
   * At the end local variables are set depending on commandline argument values.
   *
   * @param args  the commandline arguments to process
   */
  private static void processCmdLineArgs (String[] args) {

    // define available options
    Option versionOp = Option.builder("v").longOpt("version").desc("Print the version of this application").build();
    Option helpOp = Option.builder("h").longOpt("help").desc("Print this output").build();
    Option inFileOp = Option.builder("i").longOpt("input").desc("The file to be processed").hasArg().argName("FILE").required().build();
    Option outFileOp = Option.builder("o").longOpt("output").desc("The file to write output to").hasArg().argName("FILE").build();
    Option forceOp = Option.builder("f").longOpt("force").desc("Produce TEI output even if there are errors in file to convert or in the generated output").build();
    Option debugOp = Option.builder("d").longOpt("debug").desc("Run program in debug mode. Can result in lots of output!").build();

    // we need two sets of options to be able to first check for some options
    // without getting exceptions that some required options are not provided
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
    CommandLine cmd;

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
    outputFileName = cmd.getOptionValue("output");
    forceMode = cmd.hasOption("f");
    debugMode = cmd.hasOption("d");
  }

  /**
   * Method that prints version or help and exits or
   * that prints help if insufficient command-line parameters were given.
   *
   * @param formatter for printing formatted help message
   * @param cmd       contains given command-line options
   * @param options   contains all available options
   */
  private static void printUsage (HelpFormatter formatter, CommandLine cmd, Options options) {

    if (cmd == null) {
      formatter.printHelp(PROG_NAME, HEADER, options, FOOTER, true);
    } else {
      if (cmd.hasOption("h") || cmd.hasOption("v")) {
        if (cmd.hasOption("v")) {
          System.out.println("Version: " + VERSION);
        }
        if (cmd.hasOption("h")) {
          formatter.printHelp(PROG_NAME, HEADER, options, FOOTER, true);
        }
        System.exit(0);
      }
    }
  }

  /**
   * There was the problem that resource files withing jar archive could not be
   * read directly from that archive. Therefore this method takes the resource
   * file from the jar archive and copies it to a real temporary file lying on
   * disk.
   *
   * @param resourcePath   the path to the file that should be extracted
   * @return               the temporary copy of the resource File
   * @throws IOException   on IO errors
   */
  private static File getResourceAsFile (String resourcePath) throws IOException {
    File rval = null;

    InputStream in = Main.class.getResourceAsStream(resourcePath);
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

    in.close();

    return rval;
  }

  /**
   * Writes the content of a TEI XML document to a file on disc.
   * Does some checks if the given file name is proper.
   *
   * @param teiDoc        the TEI document to be processed
   * @param fileName      the file to write the TEI content to
   * @throws IOException  on any IO errors
   */
  private static void writeTEIDocToFile (TEIDoc teiDoc, String fileName) throws IOException {
    if (fileName != null && ! fileName.equals("")) {
      File file = new File(fileName);
      if (file.exists()) {
        System.err.println("File " + fileName + " already exists! Doing nothing ...");
        System.exit(1);
      }
      if (file.isDirectory()) {
        System.err.println("Bad filename specified!");
        System.exit(1);
      }

      XMLWriter writer = new XMLWriter(new FileWriter(fileName), OutputFormat.createPrettyPrint());
      writer.write(teiDoc.getDoc());
      writer.close();
    }
  }
}
