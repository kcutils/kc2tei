package kc2tei.postCollectProcessors;

import kc2tei.elements.Label;

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
