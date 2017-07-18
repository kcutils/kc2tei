import kc2tei.node.PTlabel;

public class UnspecificLabel extends TimedAnnotationElement<PTlabel> {
  public UnspecificLabel (PTlabel pTlabel) {
    super(pTlabel);
  }

  public UnspecificLabel (TimeMark timeMark, PTlabel pTlabel) {
    super(timeMark, pTlabel);
  }
}
