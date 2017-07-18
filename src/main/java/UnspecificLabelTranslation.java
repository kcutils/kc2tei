import java.util.Stack;

import kc2tei.node.ATimedLabel;
import kc2tei.node.PTlabel;


public class UnspecificLabelTranslation extends TranslationAdapter {

  private TimedAnnotationElement<PTlabel> currentAnnotationElement = null;
  private Stack<TimedAnnotationElement<PTlabel>> lastAnnotationElements = new Stack();
  private TimeMark lastTimeMark = null;


  public UnspecificLabelTranslation(AnnotationElementCollection annotationElementCollection) {
    super(annotationElementCollection);
  }

  public void caseATimedLabel(ATimedLabel node) {

    TimeMark currentTimeMark = new TimeMark(node.getTimeMarkerTwo().toString());

    if (lastTimeMark == null) {
      lastTimeMark = annotationElementCollection.addTimeMarkAndReturn(currentTimeMark);
    }

    if (!lastTimeMark.equals(currentTimeMark)) {
      // set last time marker to current time marker
      lastTimeMark = annotationElementCollection.addTimeMarkAndReturn(currentTimeMark);
      // set end times for all elements on stack
      setEndTimes();
    }

    // create new timed element with start time and content
    currentAnnotationElement = new UnspecificLabel(lastTimeMark, node.getTlabel());

    // add to global collection of elements
    annotationElementCollection.add(currentAnnotationElement);

    // build stack of annotation elements to set end time later
    lastAnnotationElements.push(currentAnnotationElement);

  }

  public void setEndTimes() {
    while (!lastAnnotationElements.empty()) {
      lastAnnotationElements.pop().setEndTime(lastTimeMark);
    }
  }
}
