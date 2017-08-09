import kc2tei.node.AConsonantReplacement;
import kc2tei.node.Node;

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
      Node n = (Node) t.getContent();
      if (n.getClass() == kc2tei.node.ATlabel.class) {
        NodeChildClassInfoGetter nInfo = new NodeChildClassInfoGetter(this);
        n.apply(nInfo);
        rval = rval + nInfo.toString();
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
      //if(e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2) && ! (e.getStartTime().equals(t2) && e.getEndTime().equals(t2))) {
      if(e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2)) {
        rval.add(e);
      }
    }}


    return rval;
  }

  public List<TimedAnnotationElement> getListOfRealizedPhonesStartingWithAndNotEndingBefore (TimeMark t1, TimeMark t2) {
    Boolean firstWordBeginFound = false;
    Boolean secondWordBeginFound = false;
    List<TimedAnnotationElement> rval = null;

    if (t1 != null && t2 != null) {
      rval = new ArrayList<>();
      for (TimedAnnotationElement e : getListOfAnnotationElementsStartingWithAndNotEndingBefore(t1, t2)) {
        if (e.getClass() == Label.class) {
          if (((Label) e).getIsWordBegin() && firstWordBeginFound) {
            secondWordBeginFound = true;
          }
          if (((Label) e).getIsWordBegin() && ! firstWordBeginFound) {
            firstWordBeginFound = true;
          }
          if (firstWordBeginFound && ! secondWordBeginFound && ((Label) e).getIsPhon() && ! ((Label) e).getPhonIsDeleted()) {
              rval.add(e);
          }
        }
      }
    }
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

  public void refineTimedLabels () {
    for (TimedAnnotationElement t : annotationElements) {
      Node node = (Node) t.getContent();
      if (node.getClass() == kc2tei.node.ATlabel.class) {
        NodeChildClassInfoGetter nInfo = new NodeChildClassInfoGetter(this, true, (Label) t);
        node.apply(nInfo);
      }
    }
  }

}

// helper class with two modes
// in normal/read-only mode it can be used to gather some debug informations
// in refinement/read-write mode it modifies labels according to their contending nodes

class NodeChildClassInfoGetter extends TranslationAdapter {

  private String details = "";
  private Boolean refineMode = false;
  private Label label = null;

  public NodeChildClassInfoGetter (AnnotationElementCollection annotationElementCollection) {
    super(annotationElementCollection);
  }

  public NodeChildClassInfoGetter (AnnotationElementCollection annotationElementCollection, Boolean refineMode, Label label) {
    super(annotationElementCollection);
    setRefineMode(refineMode);
    setLabel(label);
  }

  public void setRefineMode(Boolean refineMode) {
    this.refineMode = refineMode;
  }

  public void setLabel(Label label) {
    this.label = label;
  }

  public void defaultIn(Node node) {
    details = details + "\n---  " + node.getClass().toString();

    if (refineMode) {

      if (node.getClass() == kc2tei.node.ASegmentLabel.class) {
        label.setIsPhon(true);
      }

      if (label.getIsPhon() && node.getClass().toString().contains("Deletion")) {
        label.setPhonIsDeleted(true);
      }

      if (node.getClass() == kc2tei.node.ABoundaryConsonantLabel.class || node.getClass().toString().contains("WordBoundary")) {
        label.setIsWordBegin(true);
      }

      if (node.getClass().toString().contains("ConsonantSymbol") || node.getClass().toString().contains("StressableVowel") || node.getClass().toString().contains("UnstressableVowel") || node.getClass() == kc2tei.node.AAspirationSymbol.class) {
        if (label.getPhonIsDeleted()) {
          label.setModifiedPhon(stripWhiteSpaces(node.toString()));
        } else {
          label.setRealizedPhon(stripWhiteSpaces(node.toString()));
        }
      }

      if (node.getClass() == kc2tei.node.AConsonantReplacement.class) {
        label.setModifiedPhon(stripWhiteSpaces(((AConsonantReplacement) node).getC1().toString()));
        label.setRealizedPhon(stripWhiteSpaces(((AConsonantReplacement) node).getC2().toString()));
      }

    }
  }

  public String toString () {
    return details;
  }

  private String stripWhiteSpaces (String i) {
    String rval = i;
    rval = rval.replaceAll("\\s", "");
    return rval;
  }

}
