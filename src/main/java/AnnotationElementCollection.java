import java.util.ArrayList;
import java.util.List;

public class AnnotationElementCollection {

  private TimeMarkSet timeMarkers = new TimeMarkSet();
  private List<TimedAnnotationElement> annotationElements = new ArrayList<>();

  public void add (TimedAnnotationElement t) {

    if (t != null) {
      TimeMark s = addTimeMarkAndReturn(t.getStartTime());
      TimeMark e = addTimeMarkAndReturn(t.getEndTime());

      t.setStartTime(s);
      t.setEndTime(e);
      annotationElements.add(t);
    }

  }

  public TimeMark addTimeMarkAndReturn (TimeMark t) {
    return timeMarkers.addAndReturn(t);
  }

  public String toString () {
    String rval = "";

    for (TimedAnnotationElement t : annotationElements) {
      rval = rval + "\n" + t.toString();
    }

    return rval;
  }

  public List<TimedAnnotationElement> getListOfAnnotationElementsWithSameStart (TimeMark t) {
    List<TimedAnnotationElement> rval = new ArrayList<>();
    for (TimedAnnotationElement e : annotationElements) {
      if (t == e.getStartTime()) {
        rval.add(e);
      }
    }
    return rval;
  }

  public List<TimedAnnotationElement> getListOfAnnotationElementsStartingWithAndNotEndingBefore (TimeMark t1, TimeMark t2) {
    List<TimedAnnotationElement> rval = null;

    if (t1 != null && t2 != null) {
    rval = new ArrayList<>();
    for (TimedAnnotationElement e : annotationElements) {
      // TODO: gehoeren events vor einem Wort zu dem Wort oder zu dem vorigen?
      // kommt wahrscheinlich drauf an, was das fuer ein event ist ...
      //if(e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2) && ! (e.getStartTime().equals(t1) && e.getEndTime().equals(t1))) {
      if(e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2) && ! (e.getStartTime().equals(t2) && e.getEndTime().equals(t2))) {
      //if(e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2)) {
        rval.add(e);
      }
    }}


    return rval;
  }

  public List<TimeMark> getTimeMarkerList () {
    return timeMarkers.getList();
  }

  public List<TimedAnnotationElement> getListOfWords () {
    List<TimedAnnotationElement> rval = new ArrayList<>();
    for (TimedAnnotationElement e : annotationElements) {
      if (e.getClass().equals(Word.class)) {
        rval.add(e);
      }
    }
    return rval;
  }

  public void setTimeMarkerNames () {
    Integer nameSuffix = null;
    for (int i = 0; i < getTimeMarkerList().size(); i++) {
      nameSuffix = i + 1;
      getTimeMarkerList().get(i).setName("T" + nameSuffix);
    }
  }

}
