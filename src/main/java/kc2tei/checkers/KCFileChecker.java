package kc2tei.checkers;

import kc2tei.KCSampaToIPAConverter;
import kc2tei.postCollectProcessors.PostCollectProcessor;

public class KCFileChecker {

  private int wordCounter = 0;
  private int wordBoundaryCounter = 0;
  private int entriesNotInUnicodeTable = 0;

  private PostCollectProcessor postCollectProcessor;
  private KCSampaToIPAConverter sampaToIPAConverter;

  public KCFileChecker () {
    this.setWordCounter(0);
    this.setWordBoundaryCounter(0);
    this.setEntriesNotInUnicodeTable(0);
    this.setPostCollectProcessor(null);
    this.setSampaToIPAConverter(null);
  }

  public KCFileChecker (PostCollectProcessor postCollectProcessor, KCSampaToIPAConverter sampaToIPAConverter) {
    this();
    this.setPostCollectProcessor(postCollectProcessor);
    this.setSampaToIPAConverter(sampaToIPAConverter);
    setValues();
  }

  public int getWordCounter () {
    return wordCounter;
  }

  public void setWordCounter (int wordCounter) {
    this.wordCounter = wordCounter;
  }

  public int getWordBoundaryCounter () {
    return wordBoundaryCounter;
  }

  public void setWordBoundaryCounter (int wordBoundaryCounter) {
    this.wordBoundaryCounter = wordBoundaryCounter;
  }

  public int getEntriesNotInUnicodeTable () {
    return entriesNotInUnicodeTable;
  }

  public void setEntriesNotInUnicodeTable (int entriesNotInUnicodeTable) {
    this.entriesNotInUnicodeTable = entriesNotInUnicodeTable;
  }

  public PostCollectProcessor getPostCollectProcessor () {
    return postCollectProcessor;
  }

  public void setPostCollectProcessor (PostCollectProcessor postCollectProcessor) {
    this.postCollectProcessor = postCollectProcessor;
  }

  public KCSampaToIPAConverter getSampaToIPAConverter () {
    return sampaToIPAConverter;
  }

  public void setSampaToIPAConverter (KCSampaToIPAConverter sampaToIPAConverter) {
    this.sampaToIPAConverter = sampaToIPAConverter;
  }

  private void setValues () {
    this.setEntriesNotInUnicodeTable(this.getSampaToIPAConverter().getNoHits());
    this.setWordCounter(this.getPostCollectProcessor().getAmountOfWords());
    this.setWordBoundaryCounter(this.getPostCollectProcessor().getAmountOfWordBoundaries());
  }

  public Boolean noErrorsFound () {
    Boolean rval = true;

    rval = rval && ! (wordCounter != wordBoundaryCounter);

    rval = rval && ! (entriesNotInUnicodeTable > 0);

    return rval;
  }

  public String toString () {
    String rval = "";

    String warn = "WARNING: ";
    String explaination = "This could lead to invalid conversion results!";

    if (wordCounter != wordBoundaryCounter) {
      rval = rval + warn + "Amount of words in orthography (" + wordCounter + ") and words in label section (" + wordBoundaryCounter + ") differ. " + explaination + "\n";
    }

    if (entriesNotInUnicodeTable > 0) {
      rval = rval + warn + "There are " + entriesNotInUnicodeTable + " entries that could not be translated via Unicode table. " + explaination +"\n";
    }
    return rval;
  }

}
