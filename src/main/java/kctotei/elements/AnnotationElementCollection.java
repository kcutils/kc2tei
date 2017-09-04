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

  /**
   * Add timed annotation element to collection and be sure it has time marks from a set of
   * time marks where all time marks have a unique time.
   *
   * @param t TimedAnnotationElement to add to collection
   */
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

  private List<Label> getListOfLabelsWithinWordStartingWithAndNotEndingBefore (TimeMark t1, TimeMark t2) {
    Boolean firstWordBeginFound = false;
    List<Label> rval = null;

    if (t1 != null && t2 != null) {
      rval = new ArrayList<>();
      for (TimedAnnotationElement e : this.getAnnotationElements()) {
        if (e.getStartTime() != null && e.getEndTime() != null) {
          if (e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2)) {
            if (e.getClass() == Label.class) {
              if (((Label) e).getIsWordBegin()) {
                if (! firstWordBeginFound) {
                  firstWordBeginFound = true;
                } else {
                  // second word begin found which marks end of word in question
                  break;
                }
              }
              if (firstWordBeginFound) {
                rval.add((Label) e);
              }
            }
          }
        }
      }
    }
    return rval;
  }

  public List<Label> getListOfPhonesStartingWithAndNotEndingBefore (TimeMark t1, TimeMark t2) {
    List<Label> rval = null;
    if (t1 != null && t2 != null) {
      rval = new ArrayList<>();
      for (Label p : getListOfLabelsWithinWordStartingWithAndNotEndingBefore(t1, t2)) {
        if (p.getIsPhon() && ! p.getIgnorePhon()) {
          rval.add(p);
        }
      }
    }
    return rval;
  }


  public List<Label> getListOfPunctuationsStartingWithAndNotEndingBefore (TimeMark t1, TimeMark t2) {
    List<Label> rval = null;
    if (t1 != null && t2 != null) {
      rval = new ArrayList<>();
      for (Label p : getListOfLabelsWithinWordStartingWithAndNotEndingBefore(t1, t2)) {
        if (p.getIsPunctuation() && ! p.getIgnorePunctuation()) {
          rval.add(p);
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
