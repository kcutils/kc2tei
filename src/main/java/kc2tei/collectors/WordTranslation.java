package kc2tei.collectors;

import kc2tei.elements.*;
import transliteration.analysis.DepthFirstAdapter;
import transliteration.node.*;

/*
 * class to collect words from transliteration section
 */

public class WordTranslation extends DepthFirstAdapter {

  private AnnotationElementCollection annotationElementCollection;

  private WordTranslation () {
    this.setAnnotationElementCollection(null);
  }

  WordTranslation(AnnotationElementCollection annotationElementCollection) {
    this();
    this.setAnnotationElementCollection(annotationElementCollection);
  }

  private AnnotationElementCollection getAnnotationElementCollection () {
    return annotationElementCollection;
  }

  private void setAnnotationElementCollection (AnnotationElementCollection annotationElementCollection) {
    this.annotationElementCollection = annotationElementCollection;
  }

  /*
    public void caseTWord (TWord node) {
      this.getAnnotationElementCollection().add(new Word((Node) node));
    }

    public void caseTHesistWord (THesistWord node) {
      this.getAnnotationElementCollection().add(new Word((Node) node));
    }

    public void caseAHesistWordTransliterationContent (AHesistWordTransliterationContent node) {
      this.getAnnotationElementCollection().add(new Word((Node) node));
    }

    */
  public void caseAWordTransliterationContent (AWordTransliterationContent node) {
    this.getAnnotationElementCollection().add(new Word((Node) node));
  }

  public void caseAHesistationTransliterationContent (AHesistationTransliterationContent node) {
    this.getAnnotationElementCollection().add(new Word((Node) node));
  }
}
