package kc2tei.elements;

import kc2tei.KCSampaToIPAConverter;
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

  private Document doc;

  private Namespace xmlns;

  private HashMap namespaceMap;
  private XPath xpath;

  private AnnotationElementCollection annotationElements;

  private KCSampaToIPAConverter charConverter;

  private Integer utteranceCounter;
  private Integer spanCounter;

  private TEIDoc () {
    this.setDoc(null);
    this.setXmlns(Namespace.get("http://www.tei-c.org/ns/1.0"));
    this.setNamespaceMap(new HashMap());
    this.setXpath(null);
    this.setAnnotationElements(null);
    this.setCharConverter(null);
    this.setUtteranceCounter(0);
    this.setSpanCounter(0);
  }

  public TEIDoc (AnnotationElementCollection annotationElements) throws Exception {
    this();
    this.annotationElements = annotationElements;
    init();
  }

  public TEIDoc (AnnotationElementCollection annotationElements, KCSampaToIPAConverter charConverter) throws Exception {
    this();
    this.annotationElements = annotationElements;
    this.charConverter = charConverter;
    init();
  }

  public Document getDoc () {
    return doc;
  }

  public void setDoc (Document doc) {
    this.doc = doc;
  }

  public Namespace getXmlns () {
    return xmlns;
  }

  public void setXmlns (Namespace xmlns) {
    this.xmlns = xmlns;
  }

  public HashMap getNamespaceMap () {
    return namespaceMap;
  }

  public void setNamespaceMap (HashMap namespaceMap) {
    this.namespaceMap = namespaceMap;
  }

  public XPath getXpath () {
    return xpath;
  }

  public void setXpath (XPath xpath) {
    this.xpath = xpath;
  }

  public AnnotationElementCollection getAnnotationElements () {
    return annotationElements;
  }

  public void setAnnotationElements (AnnotationElementCollection annotationElements) {
    this.annotationElements = annotationElements;
  }

  public KCSampaToIPAConverter getCharConverter () {
    return charConverter;
  }

  public void setCharConverter (KCSampaToIPAConverter charConverter) {
    this.charConverter = charConverter;
  }

  public Integer getUtteranceCounter () {
    return utteranceCounter;
  }

  public void setUtteranceCounter (Integer utteranceCounter) {
    this.utteranceCounter = utteranceCounter;
  }

  public Integer getSpanCounter () {
    return spanCounter;
  }

  public void setSpanCounter (Integer spanCounter) {
    this.spanCounter = spanCounter;
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


  private void createXMLdoc () {
    this.setDoc(DocumentHelper.createDocument());
    this.getNamespaceMap().put("tei", "http://www.tei-c.org/ns/1.0");
  }

  private Element addElementFoundByXpath (String xpathExpr) throws Exception {
    this.setXpath(new Dom4jXPath(xpathExpr));
    this.getXpath().setNamespaceContext(new SimpleNamespaceContext(this.getNamespaceMap()));
    return (Element) this.getXpath().selectSingleNode(doc);
  }

  private void createXMLHeader () throws Exception {
    // built common header informations
    this.getDoc().addElement(new QName("TEI", xmlns)).addElement("teiHeader").addElement("fileDesc").addElement("titleStmt").addElement("title").addText("TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc").addElement("publicationStmt").addElement("authority").addText("ISFAS");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt").addElement("availability").addElement("p").addText("TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt").addElement("address").addElement("street").addText("TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("postCode").addText("24-TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("placeName").addText("Kiel");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("country").addText("Germany");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc").addElement("sourceDesc").addElement("recordingStmt").addElement("recording").addAttribute("type", "audio");
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
    for (TimedAnnotationElement t : this.getAnnotationElements().getAnnotationElements()) {
      if (t.getClass() == Word.class) {
        addWordWithPhones((Word) t);
      }
      if (t.getClass() == Label.class && ((Label) t).getIsVocalNoise() && !((Label) t).getVocalNoiseIsDeleted()) {
        addVocalNoise((Label) t);
      }
    }
  }

  private void addVocalNoise (Label v) throws Exception {
    if (v != null && !v.getIsPause()) {
      Element vocal = addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("vocal").addAttribute("start", v.getStartTime().getName()).addAttribute("end", v.getEndTime().getName());
      vocal.addElement("desc").addText(v.getVocalNoiseType());
    } else {
      addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("pause").addAttribute("start", v.getStartTime().getName()).addAttribute("end", v.getEndTime().getName());
    }
  }

  private void addWordWithPhones (Word w) throws Exception {
    if (w != null && w.getStartTime() != null && w.getContent() != null && w.getEndTime() != null) {
      this.setUtteranceCounter(this.getUtteranceCounter() + 1);

      if (this.getCharConverter() == null) {
        this.setCharConverter(new KCSampaToIPAConverter());
      }

      // create TEI document elements for current word and related elements
      Element annotationBlock = addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("annotationBlock").
                                                                                                                      addAttribute("start", w.getStartTime().getName()).addAttribute("end", w.getEndTime().getName());
      annotationBlock.addElement("u").addAttribute("xml:id", "u" + this.getUtteranceCounter()).addElement("w").addText(w.getContent().toString());


      List<Label> phoneLabels = this.getAnnotationElements().getListOfPhonesStartingWithAndNotEndingBefore(w.getStartTime(), w.getEndTime());

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

      canonicalPhon = this.getCharConverter().getUnicodeByASCII(canonicalPhon);
      realizedPhon = this.getCharConverter().getUnicodeByASCII(realizedPhon);

      if (realizedPhon != null) {

        if (l.getIsCreaked()) {
          realizedPhon = realizedPhon + this.getCharConverter().getUnicodeByASCII("creaked");
        }

        if (l.getIsNasalized()) {
          realizedPhon = realizedPhon + this.getCharConverter().getUnicodeByASCII("nasalized");
        }

        realizedPhonesSpanGrp.addElement("span").addAttribute("from", l.getStartTime().getName()).addAttribute("to", l.getEndTime().getName()).addAttribute("xml:id", "s" + this.getSpanCounter()).addText(realizedPhon);
        this.setSpanCounter(this.getSpanCounter() + 1);
      }

      if (canonicalPhon != null) {
        canonicalPhonesSpanGrp.addElement("span").addAttribute("from", l.getStartTime().getName()).addAttribute("to", l.getEndTime().getName()).addAttribute("xml:id", "s" + this.getSpanCounter()).addText(canonicalPhon);
        this.setSpanCounter(this.getSpanCounter() + 1);
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
    writer.write(this.getDoc());
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
