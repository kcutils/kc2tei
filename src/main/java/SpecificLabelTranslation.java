import kc2tei.node.*;

import java.util.concurrent.LinkedBlockingQueue;

public class SpecificLabelTranslation extends TranslationAdapter {

  private Word currentWord = null;
  private TimeMark currentTimeMark = null;
  private TimeMark lastTimeMark = null;

  private Boolean wasInWord = false;
  private LinkedBlockingQueue<Word> words = new LinkedBlockingQueue<>();

  public SpecificLabelTranslation(AnnotationElementCollection annotationElementCollection) {
    super(annotationElementCollection);
  }

  public void caseTWord (TWord node) {
    currentWord = new Word(node);
    annotationElementCollection.add(currentWord);

    // TODO
    // try and catch beacuse of interruption exception not needed here
    try {
      words.put(currentWord);
    } catch (Exception e) {
    }
  }

  // get and register word begin in label
  public void caseTWordBoundaryPrefix (TWordBoundaryPrefix node) {
    lastTimeMark = getTimeMark(node);
    if (words.peek() != null && words.peek().getStartTime() == null) {
      words.peek().setStartTime(lastTimeMark);
    }
  }

  // get and register word end in label
  public void caseTNonWordPrefix (TNonWordPrefix node) {
    lastTimeMark = getTimeMark(node);
    if (words.peek() != null && words.peek().getStartTime() != null && words.peek().getEndTime() == null) {
      words.poll().setEndTime(lastTimeMark);
    }
  }

  // get a valid time mark for a specific node by walking up the tree
  private TimeMark getTimeMark (Node node) {
    TimeMark rval = null;

    while (node.getClass() != null && ! node.getClass().equals(kc2tei.node.ATimedLabel.class)) {
      node = node.parent();
    }

    if (node != null) {
      rval = annotationElementCollection.addTimeMarkAndReturn(new TimeMark(((ATimedLabel) node).getTimeMarkerTwo().toString()));
    }

    return rval;
  }

}
