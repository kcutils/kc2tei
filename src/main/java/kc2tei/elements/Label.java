package kc2tei.elements;

import labels.node.PTlabel;

public class Label extends TimedAnnotationElement<PTlabel> {

  // general variables
  private Boolean isWordBegin;
  private Boolean isBeginOfAccousticWord;

  // phon specific variables
  private Boolean isPhon;
  private Boolean phonIsDeleted;
  private Boolean phonIsReplaced;

  private Boolean ignorePhon;

  private Boolean isCreakModifier;
  private Boolean isCreaked;

  private Boolean isNasalizationModifier;
  private Boolean isNasalized;
  private Boolean isNasal;

  private String realizedPhon;
  private String modifiedPhon;

  // vocal noise specific variables
  private Boolean isVocalNoise;
  private Boolean isPause;
  private Boolean vocalNoiseIsDeleted;
  private String vocalNoiseType;

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
    this.setRealizedPhon(null);
    this.setModifiedPhon(null);
    this.setIsVocalNoise(false);
    this.setIsPause(false);
    this.setVocalNoiseIsDeleted(false);
    this.setVocalNoiseType(null);
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

  public String toString () {
    String rval = "";

    rval = super.toString();

    // general informations
    rval = addTrueBooleansAndDescrToString(this.getIsWordBegin(), "isWordBegin", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsBeginOfAccousticWord(), "isBeginOfAccousticWord", rval);

    // phone specifica
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

    // vocal noise specifica
    rval = addTrueBooleansAndDescrToString(this.getIsVocalNoise(), "isVocalNoise", rval);
    rval = addTrueBooleansAndDescrToString(this.getVocalNoiseIsDeleted(), "vocalNoiseIsDeleted", rval);
    rval = addStringAndDescrToString(this.getVocalNoiseType(), "vocalNoiseType", rval);

    return rval;
  }

  private String addStringAndDescrToString (String str, String descr, String r) {
    String rval = r;
    if (str != null && !str.equals("")) {
      rval = rval + "  " + descr + ": " + str;
    }
    return rval;
  }

  private String addTrueBooleansAndDescrToString (Boolean b, String descr, String r) {
    String rval = r;
    // only add "true" booleans
    if (b != null && b) {
      rval = rval + "  " + descr;
    }
    return rval;
  }
}
