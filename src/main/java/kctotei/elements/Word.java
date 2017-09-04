package kctotei.elements;
import transliteration.node.Node;

/**
 * A word is a special timed annotation element.
 */
public class Word extends TimedAnnotationElement<Node> {

  public Word (Node word) {
    super(word);
  }
}
