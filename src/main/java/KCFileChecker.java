import kc2tei.analysis.DepthFirstAdapter;
import kc2tei.node.Node;
import kc2tei.node.TWordBoundaryPrefix;

public class KCFileChecker extends DepthFirstAdapter {

  private int wordCounter = 0;
  private int wordBoundaryCounter = 0;
  private int entriesNotInUnicodeTable = 0;

  public KCFileChecker () {
  }

  public void defaultIn(Node node) {

    if (node.getClass() == kc2tei.node.AWordTransliterationContent.class || node.getClass() == kc2tei.node.AHesistationTransliterationContent.class) {
      wordCounter++;
    }

  }

  public void caseTWordBoundaryPrefix (TWordBoundaryPrefix node) {
    wordBoundaryCounter++;
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

  public void setEntriesNotInUnicodeTable (Integer i) {
    this.entriesNotInUnicodeTable = i;
  }
}
