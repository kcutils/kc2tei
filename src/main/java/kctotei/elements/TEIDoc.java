package kctotei.elements;

import kctotei.KCSampaToIPAConverter;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A TEI document is a special XML document that
 * provides methods to add relevant collected information to itself.
 */
public class TEIDoc {

  private Document doc;

  private static final Namespace XMLNS = Namespace.get("http://www.tei-c.org/ns/1.0");

  private static final String XML_ID = "xml:id";
  private static final String FROM = "from";
  private static final String TO = "to";
  private static final String START = "start";
  private static final String END = "end";

  private static final String REALIZED_PHONE_TYPE = "pho-realized";
  private static final String CANONICAL_PHONE_TYPE = "pho-canonical";

  private Map namespaceMap;
  private XPath xpath;

  private AnnotationElementCollection annotationElements;

  private KCSampaToIPAConverter charConverter;

  private Integer utteranceCounter;
  private Integer spanCounter;

  private TEIDoc () {
    this.setDoc(null);
    this.setNamespaceMap(new HashMap());
    this.setXpath(null);
    this.setAnnotationElements(null);
    this.setCharConverter(null);
    this.setUtteranceCounter(0);
    this.setSpanCounter(0);

  }

  public TEIDoc (AnnotationElementCollection annotationElements) throws JaxenException {
    this();
    this.annotationElements = annotationElements;
    init();
  }

  public TEIDoc (AnnotationElementCollection annotationElements, KCSampaToIPAConverter charConverter) throws JaxenException {
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

  public Map getNamespaceMap () {
    return namespaceMap;
  }

  public void setNamespaceMap (Map namespaceMap) {
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

  private void init () throws JaxenException {

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

  private Element addElementFoundByXpath (String xpathExpr) throws JaxenException {
    this.setXpath(new Dom4jXPath(xpathExpr));
    this.getXpath().setNamespaceContext(new SimpleNamespaceContext(this.getNamespaceMap()));
    return (Element) this.getXpath().selectSingleNode(doc);
  }

  private void createXMLHeader () throws JaxenException {
    // built common header information
    this.getDoc().addElement(new QName("TEI", XMLNS)).addElement("teiHeader").addElement("fileDesc").addElement("titleStmt").addElement("title").addText("TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc").addElement("publicationStmt").addElement("authority").addText("ISFAS");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt").addElement("availability").addElement("p").addText("TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt").addElement("address").addElement("street").addText("TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("postCode").addText("24-TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("placeName").addText("Kiel");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("country").addText("Germany");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc").addElement("sourceDesc").addElement("recordingStmt").addElement("recording").addAttribute("type", "audio");
  }


  private void addTimeLineEntries () throws JaxenException {

    // set reference point
    addElementFoundByXpath("/tei:TEI/tei:text").addElement("timeline").addAttribute("unit", "s").addElement("when").addAttribute(XML_ID, "T0");

    for (int i = 0; i < annotationElements.getTimeMarkerList().size(); i++) {
      addElementFoundByXpath("/tei:TEI/tei:text/tei:timeline").addElement("when").addAttribute(XML_ID, annotationElements.getTimeMarkerList().get(i).getName()).
                                                                                                                                                                     addAttribute("interval", Float.toString(annotationElements.getTimeMarkerList().get(i).getTime())).
                                                                                                                                                                                                                                                                          addAttribute("since", "#T0");
    }
  }

  private void addContent () throws JaxenException {
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

  private void addVocalNoise (Label v) throws JaxenException {
    if (v != null && !v.getIsPause()) {
      Element vocal = addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("vocal").addAttribute(START, v.getStartTime().getName()).addAttribute(END, v.getEndTime().getName());
      vocal.addElement("desc").addText(v.getVocalNoiseType());
    } else {
      addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("pause").addAttribute(START, v.getStartTime().getName()).addAttribute(END, v.getEndTime().getName());
    }
  }

  private void addWordWithPhones (Word w) throws JaxenException {
    if (w != null && w.getStartTime() != null && w.getContent() != null && w.getEndTime() != null) {
      this.setUtteranceCounter(this.getUtteranceCounter() + 1);

      if (this.getCharConverter() == null) {
        this.setCharConverter(new KCSampaToIPAConverter());
      }

      // create TEI document elements for current word and related elements
      Element annotationBlock = addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("annotationBlock").
                                                                                                                      addAttribute(START, w.getStartTime().getName()).addAttribute(END, w.getEndTime().getName());
      annotationBlock.addElement("u").addAttribute(XML_ID, "u" + this.getUtteranceCounter()).addElement("w").addText(w.getContent().toString());


      List<Label> phoneLabels = this.getAnnotationElements().getListOfPhonesStartingWithAndNotEndingBefore(w.getStartTime(), w.getEndTime());

      if (phoneLabels != null) {
        // add realized and canonical phones to word
        Element realizedPhonesSpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", REALIZED_PHONE_TYPE);
        Element canonicalPhonesSpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", CANONICAL_PHONE_TYPE);

        for (Label l : phoneLabels) {
          addPhone(l, canonicalPhonesSpanGrp, realizedPhonesSpanGrp);
        }
      }
    }
  }

  private void addPhone (Label l, Element canonicalPhonesSpanGrp, Element realizedPhonesSpanGrp) {
    if (l != null && l.getIsPhon() && !l.getIgnorePhon() && l.getRealizedPhon() != null) {

      String realizedPhone = l.getRealizedPhon();
      String canonicalPhone = l.getRealizedPhon();

      if (l.getPhonIsDeleted() || l.getPhonIsReplaced()) {
        canonicalPhone = l.getModifiedPhon();
      }

      canonicalPhone = this.getCharConverter().getUnicodeByASCII(canonicalPhone);
      realizedPhone = this.getCharConverter().getUnicodeByASCII(realizedPhone);

      if (realizedPhone != null) {

        if (l.getIsCreaked()) {
          realizedPhone = realizedPhone + this.getCharConverter().getUnicodeByASCII("creaked");
        }

        if (l.getIsNasalized()) {
          realizedPhone = realizedPhone + this.getCharConverter().getUnicodeByASCII("nasalized");
        }

        realizedPhonesSpanGrp.addElement("span").addAttribute(FROM, l.getStartTime().getName()).addAttribute(TO, l.getEndTime().getName()).addAttribute(XML_ID, "s" + this.getSpanCounter()).addText(realizedPhone);
        this.setSpanCounter(this.getSpanCounter() + 1);
      }

      if (canonicalPhone != null) {
        canonicalPhonesSpanGrp.addElement("span").addAttribute(FROM, l.getStartTime().getName()).addAttribute(TO, l.getEndTime().getName()).addAttribute(XML_ID, "s" + this.getSpanCounter()).addText(canonicalPhone);
        this.setSpanCounter(this.getSpanCounter() + 1);
      }
    }
  }

  // write XML doc to String and return String
  public String toStringEx () throws IOException {
    String rval;
    // Pretty print the document to System.out
    OutputFormat format = OutputFormat.createPrettyPrint();
    StringWriter str = new StringWriter();
    XMLWriter writer = new XMLWriter(str, format);
    writer.write(this.getDoc());
    writer.close();
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
