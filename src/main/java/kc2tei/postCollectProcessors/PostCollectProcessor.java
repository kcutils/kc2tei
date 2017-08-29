package kc2tei.postCollectProcessors;

import kc2tei.elements.AnnotationElementCollection;
import kc2tei.elements.Label;
import kc2tei.elements.TimedAnnotationElement;
import kc2tei.elements.Word;

import java.util.Collections;
import java.util.List;

public class PostCollectProcessor {

  private Boolean timeMarkersRefined;
  private Boolean labelsRefined;
  private Boolean wordsRefined;
  private Boolean countersSet;

  private Integer amountOfTimeMarkers;
  private Integer amountOfWords;
  private Integer amountOfWordBoundaries;
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

  private Boolean getTimeMarkersRefined () {
    return timeMarkersRefined;
  }

  private void setTimeMarkersRefined (Boolean timeMarkersRefined) {
    this.timeMarkersRefined = timeMarkersRefined;
  }

  private Boolean getLabelsRefined () {
    return labelsRefined;
  }

  private void setLabelsRefined (Boolean labelsRefined) {
    this.labelsRefined = labelsRefined;
  }

  private Boolean getWordsRefined () {
    return wordsRefined;
  }

  private void setWordsRefined (Boolean wordsRefined) {
    this.wordsRefined = wordsRefined;
  }

  private Boolean getCountersSet () {
    return countersSet;
  }

  private void setCountersSet (Boolean countersSet) {
    this.countersSet = countersSet;
  }

  public Integer getAmountOfTimeMarkers () {
    return amountOfTimeMarkers;
  }

  private void setAmountOfTimeMarkers (Integer amountOfTimeMarkers) {
    this.amountOfTimeMarkers = amountOfTimeMarkers;
  }

  public Integer getAmountOfWords () {
    return amountOfWords;
  }

  private void setAmountOfWords (Integer amountOfWords) {
    this.amountOfWords = amountOfWords;
  }

  public Integer getAmountOfWordBoundaries () {
    return amountOfWordBoundaries;
  }

  private void setAmountOfWordBoundaries (Integer amountOfWordBoundaries) {
    this.amountOfWordBoundaries = amountOfWordBoundaries;
  }

  private AnnotationElementCollection getAnnotationElementCollection () {
    return annotationElementCollection;
  }

  private void setAnnotationElementCollection (AnnotationElementCollection annotationElementCollection) {
    this.annotationElementCollection = annotationElementCollection;
  }

  private void refineAnnotationElementCollection () {
    refineTimeMarkers();
    refineTimedLabels();
    refineWords();
    setCounters();
    Collections.sort(this.getAnnotationElementCollection().getAnnotationElements());
  }

  private void refineTimeMarkers () {
    if (!this.getTimeMarkersRefined()) {
      Integer nameSuffix = null;
      for (int i = 0; i < this.getAnnotationElementCollection().getTimeMarkerList().size(); i++) {
        nameSuffix = i + 1;
        this.getAnnotationElementCollection().getTimeMarkerList().get(i).setName("T" + nameSuffix);
      }
      this.setTimeMarkersRefined(true);
    }
  }

  private void refineTimedLabels () {
    if (!this.getLabelsRefined()) {

      Label lastPhon = null;

      Boolean creakModifierFound = false;
      Boolean nasalizationModifierFound = false;

      Boolean wordBeginFound = false;
      Label beginOfAccousticWordLabel = null;

      for (Label l : this.getAnnotationElementCollection().getListOfLabels()) {
        labels.node.Node node = l.getContent();
        LabelInfoGetter lInfo = new LabelInfoGetter(l, true);

        node.apply(lInfo);

        if (l.getIsWordBegin()) {
          wordBeginFound = true;
          beginOfAccousticWordLabel = l;
        }

        if (wordBeginFound && l.getIsPhon()) {
          beginOfAccousticWordLabel.setIsBeginOfAccousticWord(true);
          wordBeginFound = false;
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

          nasalizationModifierFound = false;
        }

        if (l.getIsPhon() && creakModifierFound) {
          l.setIsCreaked(true);
        }

        if (l.getIsCreakModifier()) {
          creakModifierFound = true;
        }

        if (l.getIsNasalizationModifier()) {
          nasalizationModifierFound = true;
        }

        if (l.getIsPhon() && !l.getIgnorePhon()) {
          lastPhon = l;
        }
      }
      this.setLabelsRefined(true);
    }
  }

  private void refineWords () {
    if (!this.getWordsRefined()) {
      List<Word> words = this.getAnnotationElementCollection().getListOfWords();
      Integer i = 0;
      for (Label l : this.getAnnotationElementCollection().getListOfLabels()) {

        // set start of current word to
        // start of label that marks begin of accoustic word
        // and increment word counter
        if (l.getIsBeginOfAccousticWord()) {
          if (i < words.size()) {
            words.get(i).setStartTime(l.getStartTime());
            i++;
          }
        }

        // set end of current word to end of current label
        // if label is unignored phone
        if (i > 0 && i <= words.size() && l.getIsPhon() && !l.getIgnorePhon()) {
          words.get(i - 1).setEndTime(l.getEndTime());
        }

      }

      // set end of last word to last time mark if not already set
      if (words.size() > 0 && words.get(words.size() - 1).getEndTime() == null) {
        words.get(words.size() - 1).setEndTime(this.getAnnotationElementCollection().getTimeMarkerList().get(this.getAnnotationElementCollection().getTimeMarkerList().size() - 1));
      }

      this.setWordsRefined(true);
    }
  }

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


