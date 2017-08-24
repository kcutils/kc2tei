import kc2tei.node.Node;
import kc2tei.node.THesistation;
import kc2tei.node.TWord;

/*
 * class to collect words from transliteration section
 */

public class WordTranslation extends TranslationAdapter {

  public WordTranslation(AnnotationElementCollection annotationElementCollection) {
    super(annotationElementCollection);
  }

  public void caseTWord (TWord node) {
    annotationElementCollection.add(new Word((Node) node));
  }

  public void caseTHesistation (THesistation node) {
    annotationElementCollection.add(new Word((Node) node));
  }
}
