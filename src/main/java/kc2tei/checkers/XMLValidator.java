package kc2tei.checkers;

import com.thaiopensource.validate.SchemaReader;
import com.thaiopensource.validate.ValidationDriver;
import com.thaiopensource.validate.auto.AutoSchemaReader;
import kc2tei.elements.TEIDoc;
import org.xml.sax.InputSource;

import java.io.*;

public class XMLValidator {

  private TEIDoc doc;
  private File rngSchemaFile;

  private SchemaReader sr;
  private ValidationDriver vd;
  private InputSource inRng;
  private InputSource inDoc;

  public XMLValidator () {
    this.setDoc(null);
    this.setRngSchemaFile(null);
    this.setSr(null);
    this.setVd(null);
    this.setInRng(null);
    this.setInDoc(null);
  }

  public XMLValidator (TEIDoc doc, File rngSchemaFile) throws Exception {
    this();
    this.setDoc(doc);
    this.setRngSchemaFile(rngSchemaFile);

    this.setSr(new AutoSchemaReader());
    this.setVd(new ValidationDriver());
    this.setInRng(ValidationDriver.fileInputSource(this.getRngSchemaFile()));
    this.getInRng().setEncoding("UTF-8");
    this.getVd().loadSchema(this.getInRng());

    StringReader docStr = new StringReader(this.getDoc().toString());
    this.setInDoc(new InputSource(docStr));
    this.getInDoc().setEncoding("UTF-8");
  }

  public TEIDoc getDoc () {
    return doc;
  }

  public void setDoc (TEIDoc doc) {
    this.doc = doc;
  }

  public File getRngSchemaFile () {
    return rngSchemaFile;
  }

  public void setRngSchemaFile (File rngSchemaFile) {
    this.rngSchemaFile = rngSchemaFile;
  }

  public SchemaReader getSr () {
    return sr;
  }

  public void setSr (SchemaReader sr) {
    this.sr = sr;
  }

  public ValidationDriver getVd () {
    return vd;
  }

  public void setVd (ValidationDriver vd) {
    this.vd = vd;
  }

  public InputSource getInRng () {
    return inRng;
  }

  public void setInRng (InputSource inRng) {
    this.inRng = inRng;
  }

  public InputSource getInDoc () {
    return inDoc;
  }

  public void setInDoc (InputSource inDoc) {
    this.inDoc = inDoc;
  }

  public Boolean validate () throws Exception {
    return vd.validate(inDoc);
  }
}
