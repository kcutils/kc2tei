package kctotei.elements;

import labels.node.PTlabel;

/**
 * A label is a special timed annotation element which can hold
 * lots of different information.
 */
public class Label extends TimedAnnotationElement<PTlabel> {

  // general variables
  private Boolean isWordBegin;
  private Boolean isBeginOfAccousticWord;

  // phone specific variables
  private Boolean isPhon;
  private Boolean phonIsDeleted;
  private Boolean phonIsReplaced;

  private Boolean ignorePhon;

  private Boolean isCreakModifier;
  private Boolean isCreaked;

  private Boolean isNasalizationModifier;
  private Boolean isNasalized;
  private Boolean isNasal;

  private Boolean realizedPhoneIsStressed;
  private Boolean modifiedPhoneIsStressed;

  private StressType realizedPhoneStressType;
  private StressType modifiedPhoneStressType;

  private String realizedPhon;
  private String modifiedPhon;

  // vocal noise specific variables
  private Boolean isVocalNoise;
  private Boolean isPause;
  private Boolean vocalNoiseIsDeleted;
  private String vocalNoiseType;

  // punctuation
  private Boolean isPunctuation;
  private Boolean ignorePunctuation;
  private String punctuation;
  private Boolean punctIsUncertain;

  public Label (PTlabel pTlabel) {
    super(pTlabel);
    init();
  }

  public Label (TimeMark timeMark, PTlabel pTlabel) {
    super(timeMark, pTlabel);
    init();
  }

  private void init () {
    this.setIsWordBegin(false);
    this.setIsBeginOfAccousticWord(false);
    this.setIsPhon(false);
    this.setPhonIsDeleted(false);
    this.setPhonIsReplaced(false);
    this.setIgnorePhon(false);
    this.setIsCreakModifier(false);
    this.setIsCreaked(false);
    this.setIsNasalizationModifier(false);
    this.setIsNasalized(false);
    this.setIsNasal(false);
    this.setRealizedPhoneIsStressed(false);
    this.setModifiedPhoneIsStressed(false);
    this.setRealizedPhoneStressType(null);
    this.setModifiedPhoneStressType(null);
    this.setRealizedPhon(null);
    this.setModifiedPhon(null);
    this.setIsVocalNoise(false);
    this.setIsPause(false);
    this.setVocalNoiseIsDeleted(false);
    this.setVocalNoiseType(null);
    this.setIsPunctuation(false);
    this.setIgnorePunctuation(false);
    this.setPunctuation(null);
    this.setPunctIsUncertain(false);
  }

  public Boolean getIsPhon () {
    return isPhon;
  }

  public void setIsPhon (Boolean phon) {
    isPhon = phon;
  }

  public Boolean getPhonIsDeleted () {
    return phonIsDeleted;
  }

  public void setPhonIsDeleted (Boolean phonIsDeleted) {
    this.phonIsDeleted = phonIsDeleted;
  }

  public Boolean getIsWordBegin () {
    return isWordBegin;
  }

  public void setIsWordBegin (Boolean wordBegin) {
    isWordBegin = wordBegin;
  }

  public Boolean getIsBeginOfAccousticWord () {
    return isBeginOfAccousticWord;
  }

  public void setIsBeginOfAccousticWord (Boolean isBeginOfAccousticWord) {
    this.isBeginOfAccousticWord = isBeginOfAccousticWord;
  }

  public String getRealizedPhon () {
    return realizedPhon;
  }

  public void setRealizedPhon (String phon) {
    this.realizedPhon = phon;
  }

  public String getModifiedPhon () {
    return modifiedPhon;
  }

  public void setModifiedPhon (String modifiedPhon) {
    this.modifiedPhon = modifiedPhon;
  }

  public Boolean getPhonIsReplaced () {
    return phonIsReplaced;
  }

  public void setPhonIsReplaced (Boolean phonIsReplaced) {
    this.phonIsReplaced = phonIsReplaced;
  }

  public Boolean getIsCreakModifier () {
    return isCreakModifier;
  }

  public void setIsCreakModifier (Boolean isCreakModifier) {
    this.isCreakModifier = isCreakModifier;
  }

  public Boolean getIsCreaked () {
    return this.isCreaked;
  }

  public void setIsCreaked (Boolean isCreaked) {
    this.isCreaked = isCreaked;
  }

  public Boolean getIgnorePhon () {
    return this.ignorePhon;
  }

  public void setIgnorePhon (Boolean ignorePhon) {
    this.ignorePhon = ignorePhon;
  }

  public Boolean getIsNasalizationModifier () {
    return isNasalizationModifier;
  }

  public void setIsNasalizationModifier (Boolean isNasalizationModifier) {
    this.isNasalizationModifier = isNasalizationModifier;
  }

  public Boolean getIsNasalized () {
    return isNasalized;
  }

  public void setIsNasalized (Boolean isNasalized) {
    this.isNasalized = isNasalized;
  }

  public Boolean getIsNasal () {
    return isNasal;
  }

  public void setIsNasal (Boolean isNasal) {
    this.isNasal = isNasal;
  }

  public Boolean getRealizedPhoneIsStressed () {
    return realizedPhoneIsStressed;
  }

  public void setRealizedPhoneIsStressed (Boolean realizedPhoneIsStressed) {
    this.realizedPhoneIsStressed = realizedPhoneIsStressed;
  }

  public Boolean getModifiedPhoneIsStressed () {
    return modifiedPhoneIsStressed;
  }

  public void setModifiedPhoneIsStressed (Boolean modifiedPhoneIsStressed) {
    this.modifiedPhoneIsStressed = modifiedPhoneIsStressed;
  }

  public StressType getRealizedPhoneStressType () {
    return realizedPhoneStressType;
  }

  public void setRealizedPhoneStressType (StressType realizedPhoneStressType) {
    this.realizedPhoneStressType = realizedPhoneStressType;
  }

  public StressType getModifiedPhoneStressType () {
    return modifiedPhoneStressType;
  }

  public void setModifiedPhoneStressType (StressType modifiedPhoneStressType) {
    this.modifiedPhoneStressType = modifiedPhoneStressType;
  }

  public Boolean getIsVocalNoise () {
    return isVocalNoise;
  }

  public void setIsVocalNoise (Boolean isVocalNoise) {
    this.isVocalNoise = isVocalNoise;
  }

  public Boolean getIsPause () {
    return isPause;
  }

  public void setIsPause (Boolean isPause) {
    this.isPause = isPause;
  }

  public Boolean getVocalNoiseIsDeleted () {
    return vocalNoiseIsDeleted;
  }

  public void setVocalNoiseIsDeleted (Boolean vocalNoiseIsDeleted) {
    this.vocalNoiseIsDeleted = vocalNoiseIsDeleted;
  }

  public String getVocalNoiseType () {
    return vocalNoiseType;
  }

  public void setVocalNoiseType (String vocalNoiseType) {
    this.vocalNoiseType = vocalNoiseType;
  }

  public Boolean getIsPunctuation () {
    return isPunctuation;
  }

  public void setIsPunctuation (Boolean isPunctuation) {
    this.isPunctuation = isPunctuation;
  }

  public Boolean getIgnorePunctuation () {
    return ignorePunctuation;
  }

  public void setIgnorePunctuation (Boolean ignorePunctuation) {
    this.ignorePunctuation = ignorePunctuation;
  }

  public String getPunctuation () {
    return punctuation;
  }

  public void setPunctuation (String punctuation) {
    this.punctuation = punctuation;
  }

  public Boolean getPunctIsUncertain () {
    return punctIsUncertain;
  }

  public void setPunctIsUncertain (Boolean punctIsUncertain) {
    this.punctIsUncertain = punctIsUncertain;
  }

  public String toString () {
    String rval = super.toString();

    // general information
    rval = addTrueBooleansAndDescrToString(this.getIsWordBegin(), "isWordBegin", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsBeginOfAccousticWord(), "isBeginOfAccousticWord", rval);

    // phone specifics
    rval = addTrueBooleansAndDescrToString(this.getIsPhon(), "isPhon", rval);
    rval = addTrueBooleansAndDescrToString(this.getIgnorePhon(), "ignorePhon", rval);
    rval = addTrueBooleansAndDescrToString(this.getPhonIsDeleted(), "phonIsDeleted", rval);
    rval = addTrueBooleansAndDescrToString(this.getPhonIsReplaced(), "phonIsReplaced", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsCreakModifier(), "isCreakModifier", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsCreaked(), "isCreaked", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsNasalizationModifier(), "isNasalizationModifier", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsNasal(), "isNasal", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsNasalized(), "isNasalized", rval);

    rval = addStringAndDescrToString(this.getModifiedPhon(), "modifiedPhon", rval);
    rval = addStringAndDescrToString(this.getRealizedPhon(), "realizedPhon", rval);

    // vocal noise specifics
    rval = addTrueBooleansAndDescrToString(this.getIsVocalNoise(), "isVocalNoise", rval);
    rval = addTrueBooleansAndDescrToString(this.getVocalNoiseIsDeleted(), "vocalNoiseIsDeleted", rval);
    rval = addStringAndDescrToString(this.getVocalNoiseType(), "vocalNoiseType", rval);

    // punctuation specifics
    rval = addTrueBooleansAndDescrToString(this.getIsPunctuation(), "isPunctuation", rval);
    rval = addTrueBooleansAndDescrToString(this.getIgnorePunctuation(), "ignorePunctuation", rval);
    rval = addStringAndDescrToString(this.getPunctuation(), "punctuation", rval);
    rval = addTrueBooleansAndDescrToString(this.getPunctIsUncertain(), "punctIsUncertain", rval);

    return rval;
  }

  public String addStringAndDescrToString (String str, String descr, String r) {
    String rval = null;
    if (r != null) {
      rval = r;
    }
    if (str != null && !str.replaceAll("\\s", "").equals("")) {
      if (descr != null && ! descr.replaceAll("\\s", "").equals("")) {
        if (rval == null) {
          rval = "";
        }
        rval = rval + "  " + descr + ": " + str;
      }
    }
    return rval;
  }

  public String addTrueBooleansAndDescrToString (Boolean b, String descr, String r) {
    String rval = null;
    if (r != null) {
      rval = r;
    }
    // only add "true" booleans
    if (b != null && b) {
      if (descr != null && !descr.replaceAll("\\s", "").equals("")) {
        if (rval == null) {
          rval = "";
        }
        rval = rval + "  " + descr;
      }
    }
    return rval;
  }
}
