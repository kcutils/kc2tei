import kc2tei.node.PTlabel;

public class Label extends TimedAnnotationElement<PTlabel> {

  private Boolean isPhon = false;
  private Boolean phonIsDeleted = false;
  private Boolean phonIsModified = false;

  private Boolean ignorePhon = false;

  private Boolean isWordBegin = false;
  private Boolean isCreaked = false;

  private String realizedPhon = null;
  private String modifiedPhon = null;

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

  public Boolean getPhonIsModified () {
    return phonIsModified;
  }
  public void setPhonIsModified (Boolean phonIsModified) {
    this.phonIsModified = phonIsModified;
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

  public String toString () {
    String rval = "";

    rval = super.toString();

    rval = addStringAndDescrToString(isWordBegin.toString(), "isWordBegin", rval);
    rval = addStringAndDescrToString(isPhon.toString(), "isPhon", rval);
    rval = addStringAndDescrToString(ignorePhon.toString(), "ignorePhon", rval);
    rval = addStringAndDescrToString(phonIsDeleted.toString(), "phonIsDeleted", rval);
    rval = addStringAndDescrToString(phonIsModified.toString(), "phonIsModified", rval);
    rval = addStringAndDescrToString(isCreaked.toString(), "isCreaked", rval);
    rval = addStringAndDescrToString(modifiedPhon, "modifiedPhon", rval);
    rval = addStringAndDescrToString(realizedPhon, "realizedPhon", rval);

    return rval;
  }

  private String addStringAndDescrToString (String str, String descr, String r) {
    String rval = r;
    if (str != null && ! str.equals("")) {
      rval = rval + "  " + descr + ": " + str;
    }
   return rval;
  }
}
