import labels.node.*;

import java.util.ArrayList;
import java.util.List;

public class AnnotationElementCollection {

  private TimeMarkSet timeMarkers = new TimeMarkSet();
  private List<TimedAnnotationElement> annotationElements = new ArrayList<>();

  private Boolean nextPhonIsCreaked = false;

  private Boolean nasalizationModifierFound = false;

  private Label lastPhon = null;

  private Integer amountOfWords = 0;
  private Integer amountOfWordBoundaries = 0;

  private Boolean labelsRefined = false;
  private Boolean wordsRefined = false;

  public void add (TimedAnnotationElement t) {

    if (t != null) {
      TimeMark s = addTimeMarkAndReturn(t.getStartTime());
      TimeMark e = addTimeMarkAndReturn(t.getEndTime());

      t.setStartTime(s);
      t.setEndTime(e);
      annotationElements.add(t);

      if (t.getClass().equals(Word.class)) {
        amountOfWords++;
      }
    }
  }

  public TimeMark addTimeMarkAndReturn (TimeMark t) {
    return timeMarkers.addAndReturn(t);
  }

  public String toString () {
    String rval = "";

    for (TimedAnnotationElement t : annotationElements) {
      rval = rval + "\n" + t.toString();
      Object n = t.getContent();
      if (n.getClass() == labels.node.ATlabel.class) {
        LabelInfoGetter lInfo = new LabelInfoGetter(this);
        ((labels.node.Node) n).apply(lInfo);
        rval = rval + lInfo.toString();
      }
    }

    return rval;
  }

  public List<TimedAnnotationElement> getListOfAnnotationElementsStartingWithAndNotEndingBefore (TimeMark t1, TimeMark t2) {
    List<TimedAnnotationElement> rval = null;

    if (t1 != null && t2 != null) {
      rval = new ArrayList<>();
      for (TimedAnnotationElement e : annotationElements) {
        if (e.getStartTime() != null && e.getEndTime() != null) {
          // TODO: gehoeren events vor einem Wort zu dem Wort oder zu dem vorigen?
          // kommt wahrscheinlich drauf an, was das fuer ein event ist ...
          //if(e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2) && ! (e.getStartTime().equals(t1) && e.getEndTime().equals(t1))) {
          //if(e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2) && ! (e.getStartTime().equals(t2) && e.getEndTime().equals(t2))) {
          if(e.getStartTime().isGreaterOrEqual(t1) && e.getEndTime().isSmallerOrEqual(t2)) {
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
          if (((Label) e).getIsWordBegin() && ! firstWordBeginFound) {
            firstWordBeginFound = true;
          }
          if (firstWordBeginFound && ! secondWordBeginFound && ((Label) e).getIsPhon() && ! ((Label) e).getIgnorePhon()) {
              rval.add((Label) e);
          }
        }
      }
    }
    return rval;
  }

  public List<TimeMark> getTimeMarkerList () {
    return timeMarkers.getList();
  }

  public List<Word> getListOfWords () {
    List<Word> rval = new ArrayList<>();
    for (TimedAnnotationElement e : annotationElements) {
      if (e.getClass().equals(Word.class)) {
        rval.add((Word) e);
      }
    }
    return rval;
  }

  public List<Label> getListOfLabels () {
    List<Label> rval = new ArrayList<>();
    for (TimedAnnotationElement e : annotationElements) {
      if (e.getClass().equals(Label.class)) {
        rval.add((Label) e);
      }
    }
    return rval;
  }


  private void setTimeMarkerNames () {
    Integer nameSuffix = null;
    for (int i = 0; i < getTimeMarkerList().size(); i++) {
      nameSuffix = i + 1;
      getTimeMarkerList().get(i).setName("T" + nameSuffix);
    }
  }

  private void refineWords () {
    if (!wordsRefined) {
      List<Word> words = getListOfWords();
      Integer i = 0;
      for (Label l : getListOfLabels()) {
        if (l.getIsWordBegin()) {
          if (i < words.size()) {
            words.get(i).setStartTime(l.getStartTime());
            if (i > 0) {
              words.get(i - 1).setEndTime(l.getStartTime());
            }
            i++;
          }
        }
      }
      if (words.get(i - 1).getEndTime() == null) {
        words.get(i - 1).setEndTime(getTimeMarkerList().get(getTimeMarkerList().size() - 1));
      }
      wordsRefined = true;
    }
  }

  private void refineTimedLabels () {
    if (!labelsRefined) {
      for (Label l : getListOfLabels()) {
        labels.node.Node node = l.getContent();
        LabelInfoGetter lInfo = new LabelInfoGetter(this, true, l);
        lInfo.setNextPhonIsCreaked(nextPhonIsCreaked);
        node.apply(lInfo);

        if (l.getIsWordBegin()) {
          amountOfWordBoundaries++;
        }

        // nasalization modifiers modify previous labels if they occure before deleted nasal
        // and they modify next labels if the occure after deleted nasal
        if (l.getIsPhon() && nasalizationModifierFound) {

          if (l.getIsNasal() && l.getPhonIsDeleted()) {
            // modify previous phon
            lastPhon.setIsNasalized(true);
          }

          // modify current phon
          if (lastPhon != null && lastPhon.getIsNasal() && lastPhon.getPhonIsDeleted()) {
            l.setIsNasalized(true);
          }

        }

        nextPhonIsCreaked = lInfo.getNextPhonIsCreaked();
        if (l.getIsPhon()) {
          nasalizationModifierFound = lInfo.getIsNasalizationModifier();
        }
        if (l.getIsPhon() && !l.getIgnorePhon()) {
          lastPhon = l;
        }
      }
      labelsRefined = true;
    }
  }

  public void refineCollection () {
    setTimeMarkerNames();
    refineTimedLabels();
    refineWords();
  }

  public Integer getAmountOfWords () {
    refineCollection();
    return amountOfWords;
  }

  public Integer getAmountOfWordBoundaries () {
    refineCollection();
    return amountOfWordBoundaries;
  }

}

// helper class with two modes
// in normal/read-only mode it can be used to gather some debug informations
// in refinement/read-write mode it modifies labels according to their contending nodes

class LabelInfoGetter extends labels.analysis.DepthFirstAdapter {

  private AnnotationElementCollection annotationElementCollection = null;

  private String details = "";
  private Boolean refineMode = false;
  private Label label = null;

  private Boolean nextPhonIsCreaked = false;

  private Boolean isNasalizationModifier = false;

  public LabelInfoGetter (AnnotationElementCollection annotationElementCollection) {
    this.annotationElementCollection = annotationElementCollection;
  }

  public LabelInfoGetter (AnnotationElementCollection annotationElementCollection, Boolean refineMode, Label label) {
    this.annotationElementCollection = annotationElementCollection;
    setRefineMode(refineMode);
    setLabel(label);
  }

  public void setRefineMode(Boolean refineMode) {
    this.refineMode = refineMode;
  }

  public void setLabel(Label label) {
    this.label = label;
  }

  public void defaultIn(labels.node.Node node) {
    details = details + "\n---  " + node.getClass().toString();

    if (refineMode) {

      if (node.getClass() == labels.node.ABoundaryConsonantLabel.class || node.getClass().toString().contains("WordBoundary")) {
        label.setIsWordBegin(true);
      }

      if (node.getClass() == labels.node.ASegmentLabel.class) {
        label.setIsPhon(true);
        if (nextPhonIsCreaked) {
          label.setIsCreaked(true);
          nextPhonIsCreaked = false;
        }
      }

      if (label.getIsPhon() && node.getClass().toString().contains("Deletion")) {
        if (label.getPhonIsReplaced()) {
          label.setPhonIsReplaced(false);
        }
        label.setPhonIsDeleted(true);
      }

      if (label.getIsPhon() && ! label.getPhonIsDeleted() && node.getClass().toString().contains("Creak")) {
        this.nextPhonIsCreaked = true;
        label.setIgnorePhon(true);
      }

      if (label.getIsPhon() && node.getClass().toString().contains("NasalConsonant")) {
        label.setIsNasal(true);
      }

      if (label.getIsPhon() && node.getClass().toString().contains("Nasalization")) {
        this.isNasalizationModifier = true;
        label.setIgnorePhon(true);
      }

      if (label.getIsPhon() && node.getClass().toString().contains("Unmodified")) {
        label.setPhonIsReplaced(false);
      }

      if (label.getIsPhon() && node.getClass().toString().contains("Modified") && ! node.getClass().toString().contains("Lengthening")) {
        if (!label.getPhonIsDeleted()) {
          label.setPhonIsReplaced(true);
          PhonReplacementDetailsGetter prdg = new PhonReplacementDetailsGetter(annotationElementCollection,label);
          node.apply(prdg);
        }
      }

      if ( (! label.getPhonIsReplaced()) && (node.getClass().toString().contains("ConsonantSymbol") || node.getClass().toString().contains("StressableVowel") || node.getClass().toString().contains("UnstressableVowel") || node.getClass() == labels.node.AAspirationSymbol.class)) {
        if (label.getPhonIsDeleted()) {
          label.setModifiedPhon(stripWhiteSpaces(node.toString()));
        } else {
          label.setRealizedPhon(stripWhiteSpaces(node.toString()));
        }
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

  public void setNextPhonIsCreaked (Boolean nextPhonIsCreaked) {
    this.nextPhonIsCreaked = nextPhonIsCreaked;
  }

  public Boolean getNextPhonIsCreaked () {
    return this.nextPhonIsCreaked;
  }

  public Boolean getIsNasalizationModifier () {
    return isNasalizationModifier;
  }

}

class PhonReplacementDetailsGetter extends labels.analysis.DepthFirstAdapter {

  private AnnotationElementCollection annotationElementCollection = null;
  private Label label = null;

  public PhonReplacementDetailsGetter (AnnotationElementCollection annotationElementCollection) {
    this.annotationElementCollection = annotationElementCollection;
  }
  public PhonReplacementDetailsGetter (AnnotationElementCollection annotationElementCollection, Label label) {
    this.annotationElementCollection = annotationElementCollection;
    setLabel(label);
  }
  public void setLabel(Label label) {
    this.label = label;
  }

  public void defaultIn(labels.node.Node node) {

    //
    // consonant replacements
    //
    // normal consonant replacement
    if (node.getClass() == labels.node.ANormalConsonantReplacement.class) {
      label.setModifiedPhon(stripWhiteSpaces(((ANormalConsonantReplacement) node).getC1().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((ANormalConsonantReplacement) node).getC2().toString()));
    }

    // j by i consonant replacement
    if (node.getClass() == labels.node.APalatalApproximantByCloseUnroundedLongConsonantReplacement.class) {
      label.setModifiedPhon(stripWhiteSpaces(((APalatalApproximantByCloseUnroundedLongConsonantReplacement) node).getJ().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((APalatalApproximantByCloseUnroundedLongConsonantReplacement) node).getI().toString()));
    }
//    {palatal_approximant_by_close_unrounded_less} uncertainty? j minus big_i;
    if (node.getClass() == labels.node.APalatalApproximantByCloseUnroundedLessConsonantReplacement.class) {
      label.setModifiedPhon(stripWhiteSpaces(((APalatalApproximantByCloseUnroundedLessConsonantReplacement) node).getJ().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((APalatalApproximantByCloseUnroundedLessConsonantReplacement) node).getBigI().toString()));
    }

    //
    // vowel replacements
    //
    if (node.getClass() == labels.node.AUnstressedByUnstressedVowelReplacement2.class) {
      label.setModifiedPhon(stripWhiteSpaces(((AUnstressedByUnstressedVowelReplacement2) node).getV1().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((AUnstressedByUnstressedVowelReplacement2) node).getV2().toString()));
    }

    if (node.getClass() == labels.node.AStressedByStressedVowelReplacement2.class) {
      label.setModifiedPhon(stripWhiteSpaces(((AStressedByStressedVowelReplacement2) node).getS1().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((AStressedByStressedVowelReplacement2) node).getS2().toString()));
    }

    if (node.getClass() == labels.node.AStressedByUnstressedVowelReplacement2.class) {
      label.setModifiedPhon(stripWhiteSpaces(((AStressedByUnstressedVowelReplacement2) node).getStressableVowel().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((AStressedByUnstressedVowelReplacement2) node).getVowelSymbol().toString()));
    }
    if (node.getClass() == labels.node.AUnstressedByStressedVowelReplacement2.class) {
      label.setModifiedPhon(stripWhiteSpaces(((AUnstressedByStressedVowelReplacement2) node).getVowelSymbol().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((AUnstressedByStressedVowelReplacement2) node).getStressableVowel().toString()));
    }
    if (node.getClass() == labels.node.ACloseUnroundedLessByAlveolarLateralApproximantVowelReplacement2.class) {
      label.setModifiedPhon(stripWhiteSpaces(((ACloseUnroundedLessByAlveolarLateralApproximantVowelReplacement2) node).getBigI().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((ACloseUnroundedLessByAlveolarLateralApproximantVowelReplacement2) node).getL().toString()));
    }
    if (node.getClass() == labels.node.ACloseUnroundedLessByVoicelessPalatalConsonantVowelReplacement2.class) {
      label.setModifiedPhon(stripWhiteSpaces(((ACloseUnroundedLessByVoicelessPalatalConsonantVowelReplacement2) node).getBigI().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((ACloseUnroundedLessByVoicelessPalatalConsonantVowelReplacement2) node).getBigC().toString()));
    }
    if (node.getClass() == labels.node.ASchwarByAlveolarLateralApproximantVowelReplacement2.class) {
      label.setModifiedPhon(stripWhiteSpaces(((ASchwarByAlveolarLateralApproximantVowelReplacement2) node).getSchwar().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((ASchwarByAlveolarLateralApproximantVowelReplacement2) node).getL().toString()));
    }
    // TODO if "code too large" problem is solved
//    {close_less_by_bilabial} big_u minus m
//    if (node.getClass() == kc2tei.node.ASchwarByAlveolarLateralApproximantVowelReplacement2.class) {
//      label.setModifiedPhon(stripWhiteSpaces(((ASchwarByAlveolarLateralApproximantVowelReplacement2) node).getSchwar().toString()));
//      label.setRealizedPhon(stripWhiteSpaces(((ASchwarByAlveolarLateralApproximantVowelReplacement2) node).getL().toString()));
//    }

  }

  private String stripWhiteSpaces (String i) {
    String rval = i;
    rval = rval.replaceAll("\\s", "");
    return rval;
  }

}
