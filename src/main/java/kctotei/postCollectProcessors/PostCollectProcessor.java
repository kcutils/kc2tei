package kctotei.postCollectProcessors;

import kctotei.elements.AnnotationElementCollection;
import kctotei.elements.Label;
import kctotei.elements.TimedAnnotationElement;
import kctotei.elements.Word;

import java.util.Collections;
import java.util.List;

/**
 * The post collect processor is a wrapper class that calls other parts
 * of the program which refine the collected elements.
 */
public class PostCollectProcessor {

  private boolean timeMarkersRefined;
  private boolean labelsRefined;
  private boolean wordsRefined;
  private boolean countersSet;

  private int amountOfTimeMarkers;
  private int amountOfWords;
  private int amountOfWordBoundaries;
  private AnnotationElementCollection annotationElementCollection;

  private PostCollectProcessor () {
    this.setTimeMarkersRefined(false);
    this.setLabelsRefined(false);
    this.setWordsRefined(false);
    this.setCountersSet(false);
    this.setAmountOfTimeMarkers(0);
    this.setAmountOfWords(0);
    this.setAmountOfWordBoundaries(0);
    this.setAnnotationElementCollection(null);
  }

  public PostCollectProcessor (AnnotationElementCollection annotationElementCollection) {
    this();
    this.setAnnotationElementCollection(annotationElementCollection);
    refineAnnotationElementCollection();
  }

  private boolean getTimeMarkersRefined () {
    return timeMarkersRefined;
  }

  private void setTimeMarkersRefined (boolean timeMarkersRefined) {
    this.timeMarkersRefined = timeMarkersRefined;
  }

  private boolean getLabelsRefined () {
    return labelsRefined;
  }

  private void setLabelsRefined (boolean labelsRefined) {
    this.labelsRefined = labelsRefined;
  }

  private boolean getWordsRefined () {
    return wordsRefined;
  }

  private void setWordsRefined (boolean wordsRefined) {
    this.wordsRefined = wordsRefined;
  }

  private boolean getCountersSet () {
    return countersSet;
  }

  private void setCountersSet (boolean countersSet) {
    this.countersSet = countersSet;
  }

  public int getAmountOfTimeMarkers () {
    return amountOfTimeMarkers;
  }

  private void setAmountOfTimeMarkers (int amountOfTimeMarkers) {
    this.amountOfTimeMarkers = amountOfTimeMarkers;
  }

  public int getAmountOfWords () {
    return amountOfWords;
  }

  private void setAmountOfWords (int amountOfWords) {
    this.amountOfWords = amountOfWords;
  }

  public int getAmountOfWordBoundaries () {
    return amountOfWordBoundaries;
  }

  private void setAmountOfWordBoundaries (int amountOfWordBoundaries) {
    this.amountOfWordBoundaries = amountOfWordBoundaries;
  }

  private AnnotationElementCollection getAnnotationElementCollection () {
    return annotationElementCollection;
  }

  private void setAnnotationElementCollection (AnnotationElementCollection annotationElementCollection) {
    this.annotationElementCollection = annotationElementCollection;
  }

  /**
   * call all refinement methods and sort the resulting collection
   */
  private void refineAnnotationElementCollection () {
    refineTimeMarkers();
    refineTimedLabels();
    refineWords();
    setCounters();
    List<TimedAnnotationElement> annotationElements = this.getAnnotationElementCollection().getAnnotationElements();
    Collections.sort(annotationElements);
  }

  /**
   * add names to time markers
   */
  private void refineTimeMarkers () {
    if (!this.getTimeMarkersRefined()) {
      int nameSuffix;
      for (int i = 0; i < this.getAnnotationElementCollection().getTimeMarkerList().size(); i++) {
        nameSuffix = i + 1;
        this.getAnnotationElementCollection().getTimeMarkerList().get(i).setName("T" + nameSuffix);
      }
      this.setTimeMarkersRefined(true);
    }
  }

  /**
   * refine timed labels by getting information for each label
   */
  private void refineTimedLabels () {
    if (!this.getLabelsRefined()) {

      Label lastPhone = null;

      boolean creakModifierFound = false;
      boolean nasalizationModifierFound = false;

      boolean wordBeginFound = false;
      Label beginOfAcousticWordLabel = null;

      boolean phraseEndFound = true;

      for (Label l : this.getAnnotationElementCollection().getListOfLabels()) {
        labels.node.Node node = l.getContent();
        LabelInfoGetter lInfo = new LabelInfoGetter(l, true);

        node.apply(lInfo);

        if (l.getIsWordBegin()) {
          wordBeginFound = true;
          beginOfAcousticWordLabel = l;
        }

        if (wordBeginFound && l.getIsPhon()) {
          beginOfAcousticWordLabel.setIsBeginOfAccousticWord(true);
          wordBeginFound = false;
        }

        // nasalization modifiers modify previous labels if they occur before deleted nasal
        // and they modify next labels if the occur after deleted nasal
        if (l.getIsPhon() && nasalizationModifierFound) {

          if (lastPhone != null && l.getIsNasal() && l.getPhonIsDeleted()) {
            // modify previous phone
            lastPhone.setIsNasalized(true);
          }

          // modify current phone
          if (lastPhone != null && lastPhone.getIsNasal() && lastPhone.getPhonIsDeleted()) {
            l.setIsNasalized(true);
          }

          nasalizationModifierFound = false;
        }

        if (l.getIsPhon() && creakModifierFound) {
          l.setIsCreaked(true);
          creakModifierFound = false;
        }

        if (l.getIsCreakModifier()) {
          creakModifierFound = true;
        }

        if (l.getIsNasalizationModifier()) {
          nasalizationModifierFound = true;
        }

        if (l.getIsPhon() && !l.getIgnorePhon()) {
          lastPhone = l;
        }

        if (l.getIsProsodicLabel() && phraseEndFound) {
          l.setIsPhraseBegin(true);
          phraseEndFound = false;
        }
        if (l.getIsProsodicLabel() && l.getIsPhraseEnd()) {
          phraseEndFound = true;
        }
      }
      this.setLabelsRefined(true);
    }
  }

  /**
   * refine words by setting starting and ending time marks to proper values
   */
  private void refineWords () {
    if (!this.getWordsRefined()) {
      List<Word> words = this.getAnnotationElementCollection().getListOfWords();
      int i = 0;
      for (Label l : this.getAnnotationElementCollection().getListOfLabels()) {

        // set start of current word to
        // start of label that marks begin of acoustic word
        // and increment word counter
        if (l.getIsBeginOfAccousticWord()) {
          if (i < words.size()) {
            words.get(i).setStartTime(l.getStartTime());
            i++;
          }
        }

        // set end of current word to end of current label
        // if label is not ignored phone
        if (i > 0 && i <= words.size() && l.getIsPhon() && !l.getIgnorePhon()) {
          words.get(i - 1).setEndTime(l.getEndTime());
        }

      }

      // set end of last word to last time mark if not already set
      if (!words.isEmpty() && words.get(words.size() - 1).getEndTime() == null) {
        words.get(words.size() - 1).setEndTime(this.getAnnotationElementCollection().getTimeMarkerList().get(this.getAnnotationElementCollection().getTimeMarkerList().size() - 1));
      }

      this.setWordsRefined(true);
    }
  }

  /**
   * set counters for elements in collection
   */
  private void setCounters () {
    if (!this.getCountersSet()) {

      this.setAmountOfTimeMarkers(this.getAnnotationElementCollection().getTimeMarkerList().size());

      for (TimedAnnotationElement t : this.getAnnotationElementCollection().getAnnotationElements()) {
        if (t.getClass() == Word.class) {
          this.setAmountOfWords(this.getAmountOfWords() + 1);
        }

        if (t.getClass() == Label.class && ((Label) t).getIsBeginOfAccousticWord()) {
          this.setAmountOfWordBoundaries(this.getAmountOfWordBoundaries() + 1);
        }
      }

      this.setCountersSet(true);
    }
  }
}


