package kctotei.postCollectProcessors;

import kctotei.elements.Label;
import kctotei.elements.StressType;
import labels.analysis.DepthFirstAdapter;
import labels.node.*;

/**
 * The phone replacement details getter could be part of
 * the label info getter, but was extracted to keep
 * the label info getter more clear.
 */
class PhoneReplacementDetailsGetter extends DepthFirstAdapter {

  private Label label;

  private PhoneReplacementDetailsGetter () {
    this.setLabel(null);
  }

  PhoneReplacementDetailsGetter (Label label) {
    this();
    this.setLabel(label);
  }

  public void setLabel (Label label) {
    this.label = label;
  }

  public void defaultIn (labels.node.Node node) {

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
    // {palatal_approximant_by_close_unrounded_less} uncertainty? j minus big_i;
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
      boolean modifiedPhoneHasPrimaryStress = ((AStressedByStressedVowelReplacement2) node).getL1().toString().contains("'");
      boolean realizedPhoneHasPrimaryStress = ((AStressedByStressedVowelReplacement2) node).getL2().toString().contains("'");
      label.setModifiedPhon(stripWhiteSpaces(((AStressedByStressedVowelReplacement2) node).getS1().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((AStressedByStressedVowelReplacement2) node).getS2().toString()));
      label.setModifiedPhoneIsStressed(true);
      label.setModifiedPhoneStressType(new StressType().setPrimaryStressAndReturnStressType(modifiedPhoneHasPrimaryStress));
      label.setRealizedPhoneIsStressed(true);
      label.setRealizedPhoneStressType(new StressType().setPrimaryStressAndReturnStressType(realizedPhoneHasPrimaryStress));
    }

    if (node.getClass() == labels.node.AStressedByUnstressedVowelReplacement2.class) {
      boolean isPrimaryStress = ((AStressedByUnstressedVowelReplacement2) node).getLexicalStress().toString().contains("'");
      label.setModifiedPhon(stripWhiteSpaces(((AStressedByUnstressedVowelReplacement2) node).getStressableVowel().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((AStressedByUnstressedVowelReplacement2) node).getVowelSymbol().toString()));
      label.setModifiedPhoneIsStressed(true);
      label.setModifiedPhoneStressType(new StressType().setPrimaryStressAndReturnStressType(isPrimaryStress));
    }
    if (node.getClass() == labels.node.AUnstressedByStressedVowelReplacement2.class) {
      boolean isPrimaryStress = ((AUnstressedByStressedVowelReplacement2) node).getLexicalStress().toString().contains("'");
      label.setModifiedPhon(stripWhiteSpaces(((AUnstressedByStressedVowelReplacement2) node).getVowelSymbol().toString()));
      label.setRealizedPhon(stripWhiteSpaces(((AUnstressedByStressedVowelReplacement2) node).getStressableVowel().toString()));
      label.setRealizedPhoneIsStressed(true);
      label.setRealizedPhoneStressType(new StressType().setPrimaryStressAndReturnStressType(isPrimaryStress));
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
