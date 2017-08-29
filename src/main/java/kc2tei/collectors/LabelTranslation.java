package kc2tei.collectors;

import kc2tei.elements.AnnotationElementCollection;
import kc2tei.elements.Label;
import kc2tei.elements.TimeMark;
import kc2tei.elements.TimedAnnotationElement;
import labels.analysis.DepthFirstAdapter;
import labels.node.ATimedLabel;
import labels.node.PTlabel;

/*
 * class to collect timed labels and time marks from label section
 */

public class LabelTranslation extends DepthFirstAdapter {

  private AnnotationElementCollection annotationElementCollection = null;

  private TimedAnnotationElement<PTlabel> lastAnnotationElement = null;
  private TimeMark lastTimeMark = null;

  private LabelTranslation () {
    this.setAnnotationElementCollection(null);
    this.setLastAnnotationElement(null);
    this.setLastTimeMark(null);
  }

  public LabelTranslation (AnnotationElementCollection annotationElementCollection) {
    this();
    this.setAnnotationElementCollection(annotationElementCollection);
  }

  private AnnotationElementCollection getAnnotationElementCollection () {
    return annotationElementCollection;
  }

  private void setAnnotationElementCollection (AnnotationElementCollection annotationElementCollection) {
    this.annotationElementCollection = annotationElementCollection;
  }

  private TimedAnnotationElement<PTlabel> getLastAnnotationElement () {
    return lastAnnotationElement;
  }

  private void setLastAnnotationElement (TimedAnnotationElement<PTlabel> lastAnnotationElement) {
    this.lastAnnotationElement = lastAnnotationElement;
  }

  private TimeMark getLastTimeMark () {
    return lastTimeMark;
  }

  private void setLastTimeMark (TimeMark lastTimeMark) {
    this.lastTimeMark = lastTimeMark;
  }

  /*
     * Note that there are two types of labels
     * labels with duration and
     * labels with zero duration (events)
     *
     * events have same time mark as following label
     */
  public void caseATimedLabel (ATimedLabel node) {

    TimeMark currentTimeMark = this.getAnnotationElementCollection().addTimeMarkAndReturn(new TimeMark(node.getTimeMarkerTwo().toString()));

    // create new timed element with start time and content
    TimedAnnotationElement<PTlabel> currentAnnotationElement = new Label(currentTimeMark, node.getTlabel());

    // assume each timed element is an event (until we know more details, i.e. what's the following time mark)
    currentAnnotationElement.setEndTime(currentTimeMark);

    // add to global collection of elements
    this.getAnnotationElementCollection().add(currentAnnotationElement);

    // if last element is no event, correct it
    if (this.getLastTimeMark() != null && !this.getLastTimeMark().equals(currentTimeMark)) {
      this.getLastAnnotationElement().setEndTime(currentTimeMark);
    }

    // save objects if we re-enter this function
    this.setLastTimeMark(currentTimeMark);
    this.setLastAnnotationElement(currentAnnotationElement);

  }

}
