package kc2tei;

import kc2tei.analysis.*;
import kc2tei.node.*;

public class Translation extends DepthFirstAdapter {

  // create TEI XML doc object to be able to call
  // XML producing methods
  private TEIDoc doc = new TEIDoc();

  // in case we have found a timed label (no matter which content)
  // produce some entries in XML document
  // NOTE: in Kiel Corpus files these labels come before time markers
  public void caseATlabel (ATlabel node) {
    doc.addUnspecifiedUtterance(node.toString());
  }

  // in case we have found relevant time marker
  // replace all contained whitespaces and produce
  // timeline entries in XML document
  public void caseTTimeMarkerTwo (TTimeMarkerTwo node) {
    doc.addTimeLineEntry(node.toString().replaceAll("\\s",""));    
  }

  // helper function to write document to stdout
  // TODO: for testing, can be removed/cleaned up in future
  public void writeSout() throws Exception {
    doc.writeSout();
  }

}
