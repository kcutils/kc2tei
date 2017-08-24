import transliteration.analysis.DepthFirstAdapter;
import transliteration.node.Node;
import transliteration.node.THesistation;
import transliteration.node.TWord;

/*
 * class to collect words from transliteration section
 */

public class WordTranslation extends DepthFirstAdapter {

  private AnnotationElementCollection annotationElementCollection = null;

  public WordTranslation(AnnotationElementCollection annotationElementCollection) {
    this.annotationElementCollection = annotationElementCollection;
  }

  public void caseTWord (TWord node) {
    annotationElementCollection.add(new Word((Node) node));
  }

  public void caseTHesistation (THesistation node) {
    annotationElementCollection.add(new Word((Node) node));
  }
}
