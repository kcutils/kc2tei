import kc2tei.node.ATimedLabel;
import kc2tei.node.PTlabel;


public class UnspecificLabelTranslation extends TranslationAdapter {

  private TimedAnnotationElement<PTlabel> lastAnnotationElement = null;
  private TimeMark lastTimeMark = null;


  public UnspecificLabelTranslation(AnnotationElementCollection annotationElementCollection) {
    super(annotationElementCollection);
  }

  /*
   * Note that there are two types of labels
   * labels with duration and
   * labels with zero duration (events)
   *
   * events have same time mark as following label
   */
  public void caseATimedLabel(ATimedLabel node) {

    TimeMark currentTimeMark = annotationElementCollection.addTimeMarkAndReturn(new TimeMark(node.getTimeMarkerTwo().toString()));

    // create new timed element with start time and content
    TimedAnnotationElement<PTlabel> currentAnnotationElement = new UnspecificLabel(currentTimeMark, node.getTlabel());

    // assume each timed element is an event (until we know more details, i.e. what's the following time mark)
    currentAnnotationElement.setEndTime(currentTimeMark);

    // add to global collection of elements
    annotationElementCollection.add(currentAnnotationElement);

    // if last element is no event, correct it
    if (lastTimeMark != null && !lastTimeMark.equals(currentTimeMark)) {
      lastAnnotationElement.setEndTime(currentTimeMark);
    }

    // save objects if we re-enter this function
    lastTimeMark = currentTimeMark;
    lastAnnotationElement = currentAnnotationElement;

  }

}
