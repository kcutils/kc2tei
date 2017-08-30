package kctotei.checkers;

import com.thaiopensource.validate.ValidationDriver;
import kctotei.elements.TEIDoc;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * A XML validator in this context validates a given TEI XML document against a given RNG schema.
 */

public class XMLValidator {

  private TEIDoc doc;
  private File rngSchemaFile;

  private XMLValidator () {
    this.setDoc(null);
    this.setRngSchemaFile(null);
  }

  XMLValidator (TEIDoc doc, File rngSchemaFile) {
    this();
    this.setDoc(doc);
    this.setRngSchemaFile(rngSchemaFile);
  }

  private TEIDoc getDoc () {
    return doc;
  }

  private void setDoc (TEIDoc doc) {
    this.doc = doc;
  }

  private File getRngSchemaFile () {
    return rngSchemaFile;
  }

  private void setRngSchemaFile (File rngSchemaFile) {
    this.rngSchemaFile = rngSchemaFile;
  }

  Boolean validate () throws IOException, SAXException {
    ValidationDriver vd = new ValidationDriver();

    // load the schema from a schema file
    InputSource inRng = ValidationDriver.fileInputSource(this.getRngSchemaFile());
    inRng.setEncoding("UTF-8");
    vd.loadSchema(inRng);

    // get the document as a stream
    StringReader docStr = new StringReader(this.getDoc().toString());
    InputSource inDoc = new InputSource(docStr);
    inDoc.setEncoding("UTF-8");

    // validate document against schema
    // sadly this creates lots of output on errors that cannot be silenced
    return vd.validate(inDoc);
  }
}
