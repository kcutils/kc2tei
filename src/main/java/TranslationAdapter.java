import kc2tei.analysis.DepthFirstAdapter;

public abstract class TranslationAdapter extends DepthFirstAdapter {

  AnnotationElementCollection annotationElementCollection = null;

  public TranslationAdapter (AnnotationElementCollection annotationElementCollection) {
    this.annotationElementCollection = annotationElementCollection;
  }

}
