package kctotei.elements;

import kctotei.postCollectProcessors.LabelInfoGetter;

import java.util.ArrayList;
import java.util.List;

/**
 * An annotation element collection holds relevant annotation elements
 * and provides methods collect them
 * and methods to access them, e.g. get all elements of a certain type.
 */

public class AnnotationElementCollection {

  private TimeMarkSet timeMarkers;
  private List<TimedAnnotationElement> annotationElements;

  public AnnotationElementCollection () {
    this.setTimeMarkers(new TimeMarkSet());
    this.setAnnotationElements(new ArrayList<>());
  }

  private TimeMarkSet getTimeMarkers () {
    return timeMarkers;
  }

  private void setTimeMarkers (TimeMarkSet timeMarkers) {
    this.timeMarkers = timeMarkers;
  }

  public List<TimedAnnotationElement> getAnnotationElements () {
    return this.annotationElements;
  }

  private void setAnnotationElements (List<TimedAnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
  }

  public void add (TimedAnnotationElement t) {

    if (t != null) {
      TimeMark s = addTimeMarkAndReturn(t.getStartTime());
      TimeMark e = addTimeMarkAndReturn(t.getEndTime());

      t.setStartTime(s);
      t.setEndTime(e);
      this.getAnnotationElements().add(t);

    }
  }

  public TimeMark addTimeMarkAndReturn (TimeMark t) {
    return this.getTimeMarkers().addAndReturn(t);
  }

  public String toString () {
    StringBuilder buf = new StringBuilder();

    for (TimedAnnotationElement t : this.getAnnotationElements()) {
      buf.append("\n");
      buf.append(t.toString());
      Object n = t.getContent();
      if (n.getClass() == labels.node.ATlabel.class) {
        LabelInfoGetter lInfo = new LabelInfoGetter();
        ((labels.node.Node) n).apply(lInfo);
        buf.append(lInfo.toString());
      }
    }

    return buf.toString();
  }

  public List<TimedAnnotationElement> getListOfAnnotationElementsStartingWithAndNotEndingBefore (TimeMark t1, TimeMark t2) {
    List<TimedAnnotationElement> rval = null;

    if (t1 != null && t2 != null) {
      rval = new ArrayList<>();
      for (TimedAnnotationElement e : this.getAnnotationElements()) {
        if (e.getStartTime() != null && e.getEndTime() != null) {
          // TODO: gehoeren events vor einem Wort zu dem Wort oder zu dem vorigen?
          // kommt wahrscheinlich drauf an, was das fuer ein event ist ...
          //if(e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2) && ! (e.getStartTime().equals(t1) && e.getEndTime().equals(t1))) {
          //if(e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2) && ! (e.getStartTime().equals(t2) && e.getEndTime().equals(t2))) {
          if (e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2)) {
            rval.add(e);
          }
        }
      }
    }
    return rval;
  }

  public List<Label> getListOfPhonesStartingWithAndNotEndingBefore (TimeMark t1, TimeMark t2) {
    Boolean firstWordBeginFound = false;
    Boolean secondWordBeginFound = false;
    List<Label> rval = null;

    if (t1 != null && t2 != null) {
      rval = new ArrayList<>();
      for (TimedAnnotationElement e : getListOfAnnotationElementsStartingWithAndNotEndingBefore(t1, t2)) {
        if (e.getClass() == Label.class) {
          if (((Label) e).getIsWordBegin() && firstWordBeginFound) {
            secondWordBeginFound = true;
          }
          if (((Label) e).getIsWordBegin() && !firstWordBeginFound) {
            firstWordBeginFound = true;
          }
          if (firstWordBeginFound && !secondWordBeginFound && ((Label) e).getIsPhon() && !((Label) e).getIgnorePhon()) {
            rval.add((Label) e);
          }
        }
      }
    }
    return rval;
  }

  public List<TimeMark> getTimeMarkerList () {
    return this.getTimeMarkers().getList();
  }

  public List<Word> getListOfWords () {
    List<Word> rval = new ArrayList<>();
    for (TimedAnnotationElement e : this.getAnnotationElements()) {
      if (e.getClass().equals(Word.class)) {
        rval.add((Word) e);
      }
    }
    return rval;
  }

  public List<Label> getListOfLabels () {
    List<Label> rval = new ArrayList<>();
    for (TimedAnnotationElement e : this.getAnnotationElements()) {
      if (e.getClass().equals(Label.class)) {
        rval.add((Label) e);
      }
    }
    return rval;
  }
}
