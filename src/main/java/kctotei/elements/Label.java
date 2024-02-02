package kctotei.elements;

import labels.node.PTlabel;

/**
 * A label is a special timed annotation element which can hold
 * lots of different information.
 */
public class Label extends TimedAnnotationElement<PTlabel> {

  // general variables
  private boolean isWordBegin;
  private boolean isBeginOfAccousticWord;

  // phone specific variables
  private boolean isPhon;
  private boolean phonIsDeleted;
  private boolean phonIsReplaced;
  private boolean phonIsInserted;

  private boolean ignorePhon;

  private boolean isCreakModifier;
  private boolean isCreaked;

  private boolean isNasalizationModifier;
  private boolean isNasalized;
  private boolean isNasal;

  private boolean isMAModifier;

  private boolean realizedPhoneIsStressed;
  private boolean modifiedPhoneIsStressed;

  private StressType realizedPhoneStressType;
  private StressType modifiedPhoneStressType;

  private String realizedPhon;
  private String modifiedPhon;

  // vocal noise specific variables
  private boolean isVocalNoise;
  private boolean isPause;
  private boolean vocalNoiseIsDeleted;
  private String vocalNoiseType;

  // punctuation
  private boolean isPunctuation;
  private boolean ignorePunctuation;
  private String punctuation;
  private boolean punctIsUncertain;

  // prosody
  private boolean isProsodicLabel;
  private String prosodicLabel;
  private boolean isPhraseBegin;
  private boolean isPhraseEnd;

  // false start and truncation
  private boolean isFalseStart;
  private boolean isTruncation;

  // function word marker
  private boolean isFwm;


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
    this.setPhonIsInserted(false);
    this.setIgnorePhon(false);
    this.setIsCreakModifier(false);
    this.setIsCreaked(false);
    this.setIsNasalizationModifier(false);
    this.setIsNasalized(false);
    this.setIsNasal(false);
    this.setIsMAModifier(false);
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
    this.setIsProsodicLabel(false);
    this.setProsodicLabel(null);
    this.setIsPhraseBegin(false);
    this.setIsPhraseEnd(false);
    this.setIsFalseStart(false);
    this.setIsTruncation(false);
    this.setIsFwm(false);
  }

  public boolean getIsPhon () {
    return isPhon;
  }

  public void setIsPhon (boolean phon) {
    isPhon = phon;
  }

  public boolean getPhonIsDeleted () {
    return phonIsDeleted;
  }

  public void setPhonIsDeleted (boolean phonIsDeleted) {
    this.phonIsDeleted = phonIsDeleted;
  }

  public boolean getIsWordBegin () {
    return isWordBegin;
  }

  public void setIsWordBegin (boolean wordBegin) {
    isWordBegin = wordBegin;
  }

  public boolean getIsBeginOfAccousticWord () {
    return isBeginOfAccousticWord;
  }

  public void setIsBeginOfAccousticWord (boolean isBeginOfAccousticWord) {
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

  public boolean getPhonIsReplaced () {
    return phonIsReplaced;
  }

  public void setPhonIsReplaced (boolean phonIsReplaced) {
    this.phonIsReplaced = phonIsReplaced;
  }

  public boolean getPhonIsInserted () {
    return phonIsInserted;
  }

  public void setPhonIsInserted (boolean phonIsInserted) {
    this.phonIsInserted = phonIsInserted;
  }

  public boolean getIsCreakModifier () {
    return isCreakModifier;
  }

  public void setIsCreakModifier (boolean isCreakModifier) {
    this.isCreakModifier = isCreakModifier;
  }

  public boolean getIsCreaked () {
    return this.isCreaked;
  }

  public void setIsCreaked (boolean isCreaked) {
    this.isCreaked = isCreaked;
  }

  public boolean getIgnorePhon () {
    return this.ignorePhon;
  }

  public void setIgnorePhon (boolean ignorePhon) {
    this.ignorePhon = ignorePhon;
  }

  public boolean getIsNasalizationModifier () {
    return isNasalizationModifier;
  }

  public void setIsNasalizationModifier (boolean isNasalizationModifier) {
    this.isNasalizationModifier = isNasalizationModifier;
  }

  public boolean getIsNasalized () {
    return isNasalized;
  }

  public void setIsNasalized (boolean isNasalized) {
    this.isNasalized = isNasalized;
  }

  public boolean getIsNasal () {
    return isNasal;
  }

  public void setIsNasal (boolean isNasal) {
    this.isNasal = isNasal;
  }

    public boolean getIsMAModifier () {
    return isMAModifier;
  }

  public void setIsMAModifier (boolean isMAModifier) {
    this.isMAModifier = isMAModifier;
  }

  public boolean getRealizedPhoneIsStressed () {
    return realizedPhoneIsStressed;
  }

  public void setRealizedPhoneIsStressed (boolean realizedPhoneIsStressed) {
    this.realizedPhoneIsStressed = realizedPhoneIsStressed;
  }

  public boolean getModifiedPhoneIsStressed () {
    return modifiedPhoneIsStressed;
  }

  public void setModifiedPhoneIsStressed (boolean modifiedPhoneIsStressed) {
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

  public boolean getIsVocalNoise () {
    return isVocalNoise;
  }

  public void setIsVocalNoise (boolean isVocalNoise) {
    this.isVocalNoise = isVocalNoise;
  }

  public boolean getIsPause () {
    return isPause;
  }

  public void setIsPause (boolean isPause) {
    this.isPause = isPause;
  }

  public boolean getVocalNoiseIsDeleted () {
    return vocalNoiseIsDeleted;
  }

  public void setVocalNoiseIsDeleted (boolean vocalNoiseIsDeleted) {
    this.vocalNoiseIsDeleted = vocalNoiseIsDeleted;
  }

  public String getVocalNoiseType () {
    return vocalNoiseType;
  }

  public void setVocalNoiseType (String vocalNoiseType) {
    this.vocalNoiseType = vocalNoiseType;
  }

  public boolean getIsPunctuation () {
    return isPunctuation;
  }

  public void setIsPunctuation (boolean isPunctuation) {
    this.isPunctuation = isPunctuation;
  }

  public boolean getIgnorePunctuation () {
    return ignorePunctuation;
  }

  public void setIgnorePunctuation (boolean ignorePunctuation) {
    this.ignorePunctuation = ignorePunctuation;
  }

  public String getPunctuation () {
    return punctuation;
  }

  public void setPunctuation (String punctuation) {
    this.punctuation = punctuation;
  }

  public boolean getPunctIsUncertain () {
    return punctIsUncertain;
  }

  public void setPunctIsUncertain (boolean punctIsUncertain) {
    this.punctIsUncertain = punctIsUncertain;
  }

  public boolean getIsProsodicLabel() {
    return isProsodicLabel;
  }

  public void setIsProsodicLabel(boolean isProsodicLabel) {
    this.isProsodicLabel = isProsodicLabel;
  }

  public String getProsodicLabel() {
    return prosodicLabel;
  }

  public void setProsodicLabel(String prosodicLabel) {
    this.prosodicLabel = prosodicLabel;
  }

  public boolean getIsPhraseBegin() {
    return isPhraseBegin;
  }

  public void setIsPhraseBegin(boolean phraseBegin) {
    isPhraseBegin = phraseBegin;
  }

  public boolean getIsPhraseEnd() {
    return isPhraseEnd;
  }

  public void setIsPhraseEnd(boolean phraseEnd) {
    isPhraseEnd = phraseEnd;
  }

  public boolean getIsFalseStart() {
    return isFalseStart;
  }

  public void setIsFalseStart(boolean falseStart) {
    isFalseStart = falseStart;
  }

  public boolean getIsTruncation() {
    return isTruncation;
  }

  public void setIsTruncation(boolean truncation) {
    isTruncation = truncation;
  }

  public boolean getIsFwm() {
    return isFwm;
  }

  public void setIsFwm(boolean fwm) {
    isFwm = fwm;
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
    rval = addTrueBooleansAndDescrToString(this.getPhonIsInserted(), "phonIsInserted", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsCreakModifier(), "isCreakModifier", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsCreaked(), "isCreaked", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsNasalizationModifier(), "isNasalizationModifier", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsNasal(), "isNasal", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsNasalized(), "isNasalized", rval);

    rval = addStringAndDescrToString(this.getModifiedPhon(), "modifiedPhon", rval);
    rval = addStringAndDescrToString(this.getRealizedPhon(), "realizedPhon", rval);

    // vocal noise specifics
    rval = addTrueBooleansAndDescrToString(this.getIsVocalNoise(), "isVocalNoise", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsPause(), "isPause", rval);
    rval = addTrueBooleansAndDescrToString(this.getVocalNoiseIsDeleted(), "vocalNoiseIsDeleted", rval);
    rval = addStringAndDescrToString(this.getVocalNoiseType(), "vocalNoiseType", rval);

    // punctuation specifics
    rval = addTrueBooleansAndDescrToString(this.getIsPunctuation(), "isPunctuation", rval);
    rval = addTrueBooleansAndDescrToString(this.getIgnorePunctuation(), "ignorePunctuation", rval);
    rval = addStringAndDescrToString(this.getPunctuation(), "punctuation", rval);
    rval = addTrueBooleansAndDescrToString(this.getPunctIsUncertain(), "punctIsUncertain", rval);

    // prosody specifics
    rval = addTrueBooleansAndDescrToString(this.getIsProsodicLabel(), "isProsodicLabel", rval);
    rval = addStringAndDescrToString(this.getProsodicLabel(), "prosodicLabel", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsPhraseBegin(), "isPhraseBegin", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsPhraseEnd(), "isPhraseEnd", rval);

    // false start and truncation
    rval = addTrueBooleansAndDescrToString(this.getIsFalseStart(), "isFalseStart", rval);
    rval = addTrueBooleansAndDescrToString(this.getIsTruncation(), "isTruncation", rval);

    // function word marker
    rval = addTrueBooleansAndDescrToString(this.getIsFwm(), "isFwm", rval);

    return rval;
  }

  public String addStringAndDescrToString (String str, String descr, String r) {
    String rval = null;
    if (r != null) {
      rval = r;
    }
    if (str != null && !str.replaceAll("\\s", "").isEmpty()) {
      if (descr != null && !descr.replaceAll("\\s", "").isEmpty()) {
        if (rval == null) {
          rval = "";
        }
        rval = rval + "  " + descr + ": " + str;
      }
    }
    return rval;
  }

  public String addTrueBooleansAndDescrToString (boolean b, String descr, String r) {
    String rval = null;
    if (r != null) {
      rval = r;
    }
    // only add "true" booleans
    if (b) {
      if (descr != null && !descr.replaceAll("\\s", "").isEmpty()) {
        if (rval == null) {
          rval = "";
        }
        rval = rval + "  " + descr;
      }
    }
    return rval;
  }
}
