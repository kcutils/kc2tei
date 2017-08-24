
public class KCFileChecker {

  private int wordCounter = 0;
  private int wordBoundaryCounter = 0;
  private int entriesNotInUnicodeTable = 0;

  private AnnotationElementCollection annotationElementCollection;
  private KCSampaToIPAConverter sampaToIPAConverter;

  public KCFileChecker () {
  }

  KCFileChecker (AnnotationElementCollection annotationElementCollection, KCSampaToIPAConverter sampaToIPAConverter) {
    this.annotationElementCollection = annotationElementCollection;
    this.sampaToIPAConverter = sampaToIPAConverter;
    setValues();
  }

  private void setValues () {
    entriesNotInUnicodeTable = sampaToIPAConverter.getNoHits();
    wordCounter = annotationElementCollection.getAmountOfWords();
    wordBoundaryCounter = annotationElementCollection.getAmountOfWordBoundaries();
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
