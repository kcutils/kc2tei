package kctotei.postCollectProcessors;

import kctotei.elements.Label;
import kctotei.elements.StressType;

/**
 * A label info getter analyzes a label and its children to get
 * a more precise view of the type of the label.
 *
 * When running in refinement mode it modifies the label according to
 * the gathered information, otherwise it only gathers information
 * without modifying the label.
 */

public class LabelInfoGetter extends labels.analysis.DepthFirstAdapter {

  private String details;
  private Boolean refineMode;
  private Label label;

  public LabelInfoGetter () {
    setDetails("");
    setLabel(null);
    setRefineMode(false);
  }

  public LabelInfoGetter (Label label, Boolean refineMode) {
    this();
    setLabel(label);
    setRefineMode(refineMode);
  }

  private String getDetails () {
    return details;
  }

  public void setDetails (String details) {
    this.details = details;
  }

  public void setLabel (Label label) {
    this.label = label;
  }

  private Boolean getRefineMode () {
    return refineMode;
  }

  private void setRefineMode (Boolean refineMode) {
    this.refineMode = refineMode;
  }

  public void defaultIn (labels.node.Node node) {
    setDetails(getDetails() + "\n---  " + node.getClass().toString());

    if (this.getRefineMode()) {

      if (node.getClass() == labels.node.ABoundaryConsonantLabel.class || node.getClass().toString().contains("WordBoundary")) {
        label.setIsWordBegin(true);
      }

      // detect punctuation labels
      if (node.getClass() == labels.node.APunctuationLabel.class) {
        label.setIsPunctuation(true);
      }

      // refine punctuation labels
      if (label.getIsPunctuation()) {
        if (node.getClass() == labels.node.ASentenceStartSentencePunctuation.class) {
          label.setIgnorePunctuation(true);
        }

        if (node.getClass() == labels.node.ACommaPunctuationLabel.class) {
          label.setPunctuation(",");
        }

        if (node.getClass() == labels.node.AFullStopSentencePunctuation.class) {
          label.setPunctuation(".");
          if (((labels.node.AFullStopSentencePunctuation) node).getUncertainty() != null) {
            label.setPunctIsUncertain(true);
          }
        }

        if (node.getClass() == labels.node.AQuestionMarkSentencePunctuation.class) {
          label.setPunctuation("?");
          if (((labels.node.AQuestionMarkSentencePunctuation) node).getUncertainty() != null) {
            label.setPunctIsUncertain(true);
          }
        }

        if (node.getClass() == labels.node.AExclamationMarkSentencePunctuation.class) {
          label.setPunctuation("!");
        }
      }

      // detect vocal non verbal noise labels
      if (node.getClass() == labels.node.AVocalNoiseNonverbalLabel.class) {
        label.setIsVocalNoise(true);
      }

      // refine vocal non verbal noise labels
      if (label.getIsVocalNoise()) {
        if (node.getClass().toString().contains("Deletion")) {
          label.setVocalNoiseIsDeleted(true);
        }

        if (node.getClass().toString().contains("Breathing")) {
          label.setVocalNoiseType("breathing");
        }

        if (node.getClass().toString().contains("Laughter")) {
          label.setVocalNoiseType("laughing");
        }

        if (node.getClass().toString().contains("Cough")) {
          label.setVocalNoiseType("cough");
        }

        if (node.getClass().toString().contains("Harrumph")) {
          label.setVocalNoiseType("harrumph");
        }

        if (node.getClass().toString().contains("Smack")) {
          label.setVocalNoiseType("smack");
        }

        if (node.getClass().toString().contains("Swallow")) {
          label.setVocalNoiseType("swallow");
        }

        if (node.getClass().toString().contains("Unspecific")) {
          label.setVocalNoiseType("unspecific");
        }

        if (node.getClass().toString().contains("Silence")) {
          label.setIsPause(true);
        }
      }

      // detect phone labels
      if (node.getClass() == labels.node.ASegmentLabel.class) {
        label.setIsPhon(true);
      }

      // refine phone labels
      if (label.getIsPhon()) {

        if (node.getClass().toString().contains("Deletion")) {
          if (label.getPhonIsReplaced()) {
            label.setPhonIsReplaced(false);
          }
          label.setPhonIsDeleted(true);
        }

        if (!label.getPhonIsDeleted() && node.getClass().toString().contains("Creak")) {
          label.setIgnorePhon(true);
          label.setIsCreakModifier(true);
        }

        if (node.getClass().toString().contains("NasalConsonant")) {
          label.setIsNasal(true);
        }

        if (node.getClass().toString().contains("Nasalization")) {
          label.setIsNasalizationModifier(true);
          label.setIgnorePhon(true);
        }

        if (node.getClass().toString().contains("Insertion") || node.getClass().toString().contains("Unmodified")) {
          label.setPhonIsReplaced(false);
        }

        if (node.getClass().toString().contains("Lengthening") || node.getClass().toString().contains("MaConsonantLabel") || node.getClass().toString().contains("KpConsonantLabel")) {
          label.setIgnorePhon(true);
        }

        if (node.getClass().toString().contains("Modified") && !node.getClass().toString().contains("Lengthening")) {
          if (!label.getPhonIsDeleted()) {
            label.setPhonIsReplaced(true);
            PhoneReplacementDetailsGetter prdg = new PhoneReplacementDetailsGetter(label);
            node.apply(prdg);
          }
        }

        if (node.getClass() == labels.node.AStressedVowel.class || node.getClass() == labels.node.AStressedVowelDeletion.class) {
          Boolean isPrimaryStress = true;
          if (node.getClass() == labels.node.AStressedVowel.class) {
            isPrimaryStress = ((labels.node.AStressedVowel) node).getLexicalStress().equals("'");
          } else {
            isPrimaryStress = ((labels.node.AStressedVowelDeletion) node).getLexicalStress().equals("'");
          }
          if (label.getPhonIsDeleted()) {
            label.setModifiedPhoneIsStressed(true);
            label.setModifiedPhoneStressType(new StressType().setPrimaryStressAndReturnStressType(isPrimaryStress));
          } else {
            label.setRealizedPhoneIsStressed(true);
            label.setRealizedPhoneStressType(new StressType().setPrimaryStressAndReturnStressType(isPrimaryStress));
          }
        }

        if ((!label.getPhonIsReplaced()) && (node.getClass().toString().contains("ConsonantSymbol") || node.getClass().toString().contains("StressableVowel") || node.getClass().toString().contains("UnstressableVowel") || node.getClass() == labels.node.AAspirationSymbol.class)) {
          if (label.getPhonIsDeleted()) {
            label.setModifiedPhon(stripWhiteSpaces(node.toString()));
          } else {
            label.setRealizedPhon(stripWhiteSpaces(node.toString()));
          }
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

}
