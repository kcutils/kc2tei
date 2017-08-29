package kc2tei.checkers;

import kc2tei.KCSampaToIPAConverter;
import kc2tei.elements.TEIDoc;
import kc2tei.postCollectProcessors.PostCollectProcessor;

import java.io.File;

public class ResultChecker {
  private PostCollectProcessor postCollectProcessor;
  private KCSampaToIPAConverter charConverter;
  private File rngSchemaFile;
  private TEIDoc teiDoc;

  private KCFileChecker kcFileChecker;
  private XMLValidator xmlValidator;

  ResultChecker () {
    this.setPostCollectProcessor(null);
    this.setCharConverter(null);
    this.setRngSchemaFile(null);
    this.setTeiDoc(null);
    this.setKcFileChecker(null);
    this.setXmlValidator(null);
  }

  public ResultChecker (PostCollectProcessor postCollectProcessor, KCSampaToIPAConverter charConverter, File rngSchemaFile, TEIDoc teiDoc) throws Exception {
    this();
    this.setPostCollectProcessor(postCollectProcessor);
    this.setCharConverter(charConverter);
    this.setRngSchemaFile(rngSchemaFile);
    this.setTeiDoc(teiDoc);

    this.setKcFileChecker(new KCFileChecker(this.getPostCollectProcessor(), this.getCharConverter()));
    this.setXmlValidator(new XMLValidator(this.getTeiDoc(), this.getRngSchemaFile()));

  }

  public PostCollectProcessor getPostCollectProcessor () {
    return postCollectProcessor;
  }

  public void setPostCollectProcessor (PostCollectProcessor postCollectProcessor) {
    this.postCollectProcessor = postCollectProcessor;
  }

  public KCSampaToIPAConverter getCharConverter () {
    return charConverter;
  }

  public void setCharConverter (KCSampaToIPAConverter charConverter) {
    this.charConverter = charConverter;
  }

  public File getRngSchemaFile () {
    return rngSchemaFile;
  }

  public void setRngSchemaFile (File rngSchemaFile) {
    this.rngSchemaFile = rngSchemaFile;
  }

  public TEIDoc getTeiDoc () {
    return teiDoc;
  }

  public void setTeiDoc (TEIDoc teiDoc) {
    this.teiDoc = teiDoc;
  }

  public KCFileChecker getKcFileChecker () {
    return kcFileChecker;
  }

  public void setKcFileChecker (KCFileChecker kcFileChecker) {
    this.kcFileChecker = kcFileChecker;
  }

  public XMLValidator getXmlValidator () {
    return xmlValidator;
  }

  public void setXmlValidator (XMLValidator xmlValidator) {
    this.xmlValidator = xmlValidator;
  }

  public Boolean noErrorsFound () throws Exception {
    return this.getKcFileChecker().noErrorsFound() && this.getXmlValidator().validate();
  }

  public String toString () {
    return this.getKcFileChecker().toString();
  }
}
