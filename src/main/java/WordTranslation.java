import transliteration.analysis.DepthFirstAdapter;
import transliteration.node.*;

/*
 * class to collect words from transliteration section
 */

public class WordTranslation extends DepthFirstAdapter {

  private AnnotationElementCollection annotationElementCollection = null;

  public WordTranslation(AnnotationElementCollection annotationElementCollection) {
    this.annotationElementCollection = annotationElementCollection;
  }
/*
  public void caseTWord (TWord node) {
    annotationElementCollection.add(new Word((Node) node));
  }

  public void caseTHesistWord (THesistWord node) {
    annotationElementCollection.add(new Word((Node) node));
  }

  public void caseAHesistWordTransliterationContent (AHesistWordTransliterationContent node) {
    annotationElementCollection.add(new Word((Node) node));
  }

  */
  public void caseAWordTransliterationContent (AWordTransliterationContent node) {
    annotationElementCollection.add(new Word((Node) node));
  }

  public void caseAHesistationTransliterationContent (AHesistationTransliterationContent node) {
    annotationElementCollection.add(new Word((Node) node));
  }
}
