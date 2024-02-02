package kctotei.checkers;

import kctotei.KCSampaToIPAConverter;
import kctotei.postCollectProcessors.PostCollectProcessor;

/**
 * A Kiel Corpus file checker, checks for inconsistencies in a provided Kiel Corpus file.
 * Therefore, it does not analyze a file directly, but relies on information collected by
 * other parts of this program (collector, postCollectProcessor, ...).
 *
 * As for now the only inconsistency that could lead to bad conversion of a file is
 * a mismatch between the amount of words collected/recognized in the transliteration
 * section of a Kiel Corpus file and the amount of words found in the labels section
 * of a Kiel Corpus file.
 *
 * All remaining possible inconsistencies between these both sections are not
 * considered harmful as the transliteration section is only needed for collection
 * orthographic representations of words while all other information is collected
 * from the label section.
 *
 * Additionally, this checker checks if there were phonetic information that could
 * not be translated to Unicode/IPA equivalents.
 *
 */
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

  /**
   * Gather relevant information from other parts of this program.
   */
  private void setValues () {
    this.setEntriesNotInUnicodeTable(this.getSampaToIPAConverter().getNoHits());
    this.setWordCounter(this.getPostCollectProcessor().getAmountOfWords());
    this.setWordBoundaryCounter(this.getPostCollectProcessor().getAmountOfWordBoundaries());
  }

  /**
   *
   * @return true if amount of words found in transliteration section and amount of words found
   *              in label section is equal and if there were no conversion problems between
   *              SAMPA and IPA/Unicode,
   *         false otherwise
   */
  public boolean noErrorsFound () {
    boolean rval = wordCounter == wordBoundaryCounter;

    rval = rval && ! (entriesNotInUnicodeTable > 0);

    return rval;
  }

  /**
   *
   * @return String with warnings/explanations of possible problems if any problems were found
   */
  public String toString () {
    String rval = "";

    String warn = "WARNING: ";
    String explanation = "This could lead to invalid conversion results!";

    if (wordCounter != wordBoundaryCounter) {
      rval = rval + warn + "Amount of words in orthography (" + wordCounter + ") and words in label section (" + wordBoundaryCounter + ") differ. " + explanation + "\n";
    }

    if (entriesNotInUnicodeTable > 0) {
      rval = rval + warn + "There are " + entriesNotInUnicodeTable + " entries that could not be translated via Unicode table. " + explanation +"\n";
    }
    return rval;
  }

}
