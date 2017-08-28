import labels.node.PTlabel;

public class Label extends TimedAnnotationElement<PTlabel> {

  // general variables
  private Boolean isWordBegin = false;
  private Boolean isBeginOfAccousticWord = false;

  // phon specific variables
  private Boolean isPhon = false;
  private Boolean phonIsDeleted = false;
  private Boolean phonIsReplaced = false;

  private Boolean ignorePhon = false;

  private Boolean isCreakModifier = false;
  private Boolean isCreaked = false;

  private Boolean isNasalizationModifier = false;
  private Boolean isNasalized= false;
  private Boolean isNasal = false;

  private String realizedPhon = null;
  private String modifiedPhon = null;

  // vocal noise specific variables
  private Boolean isVocalNoise = false;
  private Boolean isPause = false;
  private Boolean vocalNoiseIsDeleted = false;
  private String vocalNoiseType = null;


  public Label(PTlabel pTlabel) {
    super(pTlabel);
  }

  public Label(TimeMark timeMark, PTlabel pTlabel) {
    super(timeMark, pTlabel);
  }

  public Boolean getIsPhon() {
    return isPhon;
  }

  public void setIsPhon(Boolean phon) {
    isPhon = phon;
  }

  public Boolean getPhonIsDeleted() {
    return phonIsDeleted;
  }

  public void setPhonIsDeleted(Boolean phonIsDeleted) {
    this.phonIsDeleted = phonIsDeleted;
  }

  public Boolean getIsWordBegin() {
    return isWordBegin;
  }

  public void setIsWordBegin(Boolean wordBegin) {
    isWordBegin = wordBegin;
  }

  public Boolean getIsBeginOfAccousticWord () {
    return isBeginOfAccousticWord;
  }

  public void setIsBeginOfAccousticWord (Boolean isBeginOfAccousticWord) {
    this.isBeginOfAccousticWord = isBeginOfAccousticWord;
  }

  public void setRealizedPhon(String phon) {
    this.realizedPhon = phon;
  }

  public String getRealizedPhon() {
    return realizedPhon;
  }

  public String getModifiedPhon() {
    return modifiedPhon;
  }

  public void setModifiedPhon(String modifiedPhon) {
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

  public void setIsCreaked (Boolean isCreaked) {
    this.isCreaked = isCreaked;
  }

  public Boolean getIsCreaked () {
    return this.isCreaked;
  }

  public void setIgnorePhon (Boolean ignorePhon) {
    this.ignorePhon = ignorePhon;
  }

  public Boolean getIgnorePhon () {
    return this.ignorePhon;
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
    rval = addTrueBooleansAndDescrToString(isWordBegin, "isWordBegin", rval);
    rval = addTrueBooleansAndDescrToString(isBeginOfAccousticWord, "isBeginOfAccousticWord", rval);

    // phone specifica
    rval = addTrueBooleansAndDescrToString(isPhon, "isPhon", rval);
    rval = addTrueBooleansAndDescrToString(ignorePhon, "ignorePhon", rval);
    rval = addTrueBooleansAndDescrToString(phonIsDeleted, "phonIsDeleted", rval);
    rval = addTrueBooleansAndDescrToString(phonIsReplaced, "phonIsReplaced", rval);
    rval = addTrueBooleansAndDescrToString(isCreakModifier, "isCreakModifier", rval);
    rval = addTrueBooleansAndDescrToString(isCreaked, "isCreaked", rval);
    rval = addTrueBooleansAndDescrToString(isNasalizationModifier, "isNasalizationModifier", rval);
    rval = addTrueBooleansAndDescrToString(isNasal, "isNasal", rval);
    rval = addTrueBooleansAndDescrToString(isNasalized, "isNasalized", rval);

    rval = addStringAndDescrToString(modifiedPhon, "modifiedPhon", rval);
    rval = addStringAndDescrToString(realizedPhon, "realizedPhon", rval);

    // vocal noise specifica
    rval = addTrueBooleansAndDescrToString(isVocalNoise, "isVocalNoise", rval);
    rval = addTrueBooleansAndDescrToString(vocalNoiseIsDeleted, "vocalNoiseIsDeleted", rval);
    rval = addStringAndDescrToString(vocalNoiseType, "vocalNoiseType", rval);

    return rval;
  }

  private String addStringAndDescrToString (String str, String descr, String r) {
    String rval = r;
    if (str != null && ! str.equals("")) {
      rval = rval + "  " + descr + ": " + str;
    }
    return rval;
  }

  private String addTrueBooleansAndDescrToString(Boolean b, String descr, String r) {
    String rval = r;
    // only add "true" booleans
    if (b != null && b) {
      rval = rval + "  " + descr;
    }
   return rval;
  }
}
