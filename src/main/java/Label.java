import kc2tei.node.PTlabel;

public class Label extends TimedAnnotationElement<PTlabel> {

  private Boolean isPhon = false;
  private Boolean phonIsDeleted = false;
  private Boolean isWordBegin = false;

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

  public String toString () {
    String rval = "";

    rval = super.toString();

    rval = addStringAndDescrToString(isPhon.toString(), "isPhon", rval);
    rval = addStringAndDescrToString(phonIsDeleted.toString(), "phonIsDeleted", rval);
    rval = addStringAndDescrToString(realizedPhon, "realizedPhon", rval);
    rval = addStringAndDescrToString(modifiedPhon, "modifiedPhon", rval);

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
