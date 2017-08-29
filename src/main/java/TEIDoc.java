import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

public class TEIDoc {

  private Document doc = null;

  private Namespace xmlns = Namespace.get("http://www.tei-c.org/ns/1.0");

  private HashMap namespaceMap = new HashMap();
  private XPath xpath = null;

  private AnnotationElementCollection annotationElements = null;

  private KCSampaToIPAConverter charConverter = null;

  private Integer utteranceCounter = 0;
  private Integer spanCounter = 0;

  private void createXMLdoc () {
    this.doc = DocumentHelper.createDocument();
    namespaceMap.put("tei", "http://www.tei-c.org/ns/1.0");
  }

  private Element addElementFoundByXpath (String xpathExpr) throws Exception {
    xpath = new Dom4jXPath(xpathExpr);
    xpath.setNamespaceContext(new SimpleNamespaceContext(namespaceMap));
    return (Element) xpath.selectSingleNode(doc);
  }

  private void createXMLHeader () throws Exception {
    // built common header informations
    this.doc.addElement(new QName("TEI", xmlns)).addElement("teiHeader").addElement("fileDesc").addElement("titleStmt").addElement("title").addText("TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc").addElement("publicationStmt").addElement("authority").addText("ISFAS");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt").addElement("availability").addElement("p").addText("TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt").addElement("address").addElement("street").addText("TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("postCode").addText("24-TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("placeName").addText("Kiel");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("country").addText("Germany");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc").addElement("sourceDesc").addElement("recordingStmt").addElement("recording").addAttribute("type", "audio");
  }

  public TEIDoc (AnnotationElementCollection annotationElements) throws Exception {
    this.annotationElements = annotationElements;
    init();
  }

  public TEIDoc (AnnotationElementCollection annotationElements, KCSampaToIPAConverter charConverter) throws Exception {
    this.annotationElements = annotationElements;
    this.charConverter = charConverter;
    init();
  }

  private void init () throws Exception {

    createXMLdoc();
    createXMLHeader();

    // add text tag
    addElementFoundByXpath("/tei:TEI").addElement("text");

    // add timeline
    addTimeLineEntries();

    // add body
    addElementFoundByXpath("/tei:TEI/tei:text").addElement("body").addElement("p").addText("dummy");

    // add content
    addContent();
  }

  private void addTimeLineEntries () throws Exception {

    // set reference point
    addElementFoundByXpath("/tei:TEI/tei:text").addElement("timeline").addAttribute("unit", "s").addElement("when").addAttribute("xml:id", "T0");

    for (int i = 0; i < annotationElements.getTimeMarkerList().size(); i++) {
      addElementFoundByXpath("/tei:TEI/tei:text/tei:timeline").addElement("when").addAttribute("xml:id", annotationElements.getTimeMarkerList().get(i).getName()).
                                                                                                                                                                     addAttribute("interval", Float.toString(annotationElements.getTimeMarkerList().get(i).getTime())).
                                                                                                                                                                                                                                                                          addAttribute("since", "#T0");
    }
  }

  private void addContent () throws Exception {
    // we expect a sorted list of timed annotation elements
    for (TimedAnnotationElement t : annotationElements.getAnnotationElements()) {
      if (t.getClass() == Word.class) {
        addWordWithPhones((Word) t);
      }
      if (t.getClass() == Label.class && ((Label) t).getIsVocalNoise() && ! ((Label) t).getVocalNoiseIsDeleted()) {
        addVocalNoise((Label) t);
      }
    }
  }

  private void addVocalNoise (Label v) throws Exception {
    if (v != null && ! v.getIsPause()) {
      Element vocal = addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("vocal").addAttribute("start", v.getStartTime().getName()).addAttribute("end", v.getEndTime().getName());
      vocal.addElement("desc").addText(v.getVocalNoiseType());
    } else {
      addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("pause").addAttribute("start", v.getStartTime().getName()).addAttribute("end", v.getEndTime().getName());
    }
  }

  private void addWordWithPhones (Word w) throws Exception {
    if (w != null && w.getStartTime() != null && w.getContent() != null && w.getEndTime() != null) {
      utteranceCounter++;

      if (charConverter == null) {
        charConverter = new KCSampaToIPAConverter();
      }

      // create TEI document elements for current word and related elements
      Element annotationBlock = addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("annotationBlock").
                                                                                                                      addAttribute("start", w.getStartTime().getName()).addAttribute("end", w.getEndTime().getName());
      annotationBlock.addElement("u").addAttribute("xml:id", "u" + utteranceCounter).addElement("w").addText(w.getContent().toString());


      List<Label> phoneLabels = annotationElements.getListOfPhonesStartingWithAndNotEndingBefore(w.getStartTime(), w.getEndTime());

      if (phoneLabels != null) {
        // add realized and canonical phones to word
        Element realizedPhonesSpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", "pho-realized");
        Element canonicalPhonesSpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", "pho-canonical");

        for (Label l : phoneLabels) {
          addPhone(l, canonicalPhonesSpanGrp, realizedPhonesSpanGrp);
        }
      }
    }
  }

  private void addPhone (Label l, Element canonicalPhonesSpanGrp, Element realizedPhonesSpanGrp) {
    if (l != null && l.getIsPhon() && !l.getIgnorePhon() && l.getRealizedPhon() != null) {

      String realizedPhon = l.getRealizedPhon();
      String canonicalPhon = l.getRealizedPhon();

      if (l.getPhonIsDeleted() || l.getPhonIsReplaced()) {
        canonicalPhon = l.getModifiedPhon();
      }

      canonicalPhon = charConverter.getUnicodeByASCII(canonicalPhon);
      realizedPhon = charConverter.getUnicodeByASCII(realizedPhon);

      if (realizedPhon != null) {

        if (l.getIsCreaked()) {
          realizedPhon = realizedPhon + charConverter.getUnicodeByASCII("creaked");
        }

        if (l.getIsNasalized()) {
          realizedPhon = realizedPhon + charConverter.getUnicodeByASCII("nasalized");
        }

        realizedPhonesSpanGrp.addElement("span").addAttribute("from", l.getStartTime().getName()).addAttribute("to", l.getEndTime().getName()).addAttribute("xml:id", "s" + spanCounter).addText(realizedPhon);
        spanCounter++;
      }

      if (canonicalPhon != null) {
        canonicalPhonesSpanGrp.addElement("span").addAttribute("from", l.getStartTime().getName()).addAttribute("to", l.getEndTime().getName()).addAttribute("xml:id", "s" + spanCounter).addText(canonicalPhon);
        spanCounter++;
      }
    }
  }

  // write XML doc to String and return String
  public String toStringEx () throws Exception {
    String rval = "";
    // Pretty print the document to System.out
    OutputFormat format = OutputFormat.createPrettyPrint();
    StringWriter str = new StringWriter();
    XMLWriter writer = new XMLWriter(str, format);
    writer.write(doc);
    rval = str.toString();
    return rval;
  }

  public String toString () {
    try {
      return toStringEx();
    } catch (Exception e) {
    }
    return "";
  }

}
