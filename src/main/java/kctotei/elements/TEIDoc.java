package kctotei.elements;

import kctotei.KCSampaToIPAConverter;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;

import java.io.IOException;
import java.io.StringWriter;
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
  private Integer wordCounter;
  private Integer spanCounter;
  private Integer pcCounter;

  private TEIDoc () {
    this.setDoc(null);
    this.setNamespaceMap(new HashMap());
    this.setXpath(null);
    this.setAnnotationElements(null);
    this.setCharConverter(null);
    this.setUtteranceCounter(0);
    this.setWordCounter(0);
    this.setSpanCounter(0);
    this.setPcCounter(0);

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

  public Integer getWordCounter () {
    return wordCounter;
  }

  public void setWordCounter (Integer wordCounter) {
    this.wordCounter = wordCounter;
  }

  public Integer getSpanCounter () {
    return spanCounter;
  }

  public void setSpanCounter (Integer spanCounter) {
    this.spanCounter = spanCounter;
  }

  public Integer getPcCounter () {
    return pcCounter;
  }

  public void setPcCounter (Integer pcCounter) {
    this.pcCounter = pcCounter;
  }

  private void init () throws JaxenException {

    createXMLdoc();
    createXMLHeader();

    // add text tag
    addElementFoundByXpath("/tei:TEI").addElement("text");

    // add timeline
    addElementFoundByXpath("/tei:TEI/tei:text").addElement("front");
    addTimeLineEntries();

    // add body
    addElementFoundByXpath("/tei:TEI/tei:text").addElement("body");

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
    addElementFoundByXpath("/tei:TEI/tei:text/tei:front").addElement("timeline").addAttribute("unit", "s").addElement("when").addAttribute(XML_ID, "T0");

    for (int i = 0; i < annotationElements.getTimeMarkerList().size(); i++) {
      addElementFoundByXpath("/tei:TEI/tei:text/tei:front/tei:timeline").addElement("when").addAttribute(XML_ID, annotationElements.getTimeMarkerList().get(i).getName()).
                                                                                                                                                                     addAttribute("interval", Float.toString(annotationElements.getTimeMarkerList().get(i).getTime())).
                                                                                                                                                                                                                                                                          addAttribute("since", "#T0");
    }
  }

  private void addContent () throws JaxenException {
    // we expect a sorted list of timed annotation elements
    for (TimedAnnotationElement t : this.getAnnotationElements().getAnnotationElements()) {
      if (t.getClass() == Word.class) {
        addWordWithPhonesAndPunctuations((Word) t);
      }
      if (t.getClass() == Label.class && ((Label) t).getIsVocalNoise() && !((Label) t).getVocalNoiseIsDeleted()) {
        addVocalNoise((Label) t);
      }
    }
  }

  private void addVocalNoise (Label v) throws JaxenException {
    if (v != null && !v.getIsPause()) {
      Element vocal = addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("vocal").addAttribute(START, "#" + v.getStartTime().getName()).addAttribute(END, "#" + v.getEndTime().getName());
      vocal.addElement("desc").addText(v.getVocalNoiseType());
    } else {
      addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("pause").addAttribute(START, "#" + v.getStartTime().getName()).addAttribute(END, "#" + v.getEndTime().getName());
    }
  }

  private void addWordWithPhonesAndPunctuations (Word w) throws JaxenException {
    if (w != null && w.getStartTime() != null && w.getContent() != null && w.getEndTime() != null) {

      if (this.getCharConverter() == null) {
        this.setCharConverter(new KCSampaToIPAConverter());
      }

      // create TEI document elements for current word and related elements
      Element annotationBlock = addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("annotationBlock").
                                                                                                                      addAttribute(START, "#" + w.getStartTime().getName()).addAttribute(END, "#" + w.getEndTime().getName());

      this.setUtteranceCounter(this.getUtteranceCounter() + 1);
      Element utterance = annotationBlock.addElement("u").addAttribute(XML_ID, "u" + this.getUtteranceCounter());

      this.setWordCounter(this.getWordCounter() + 1);
      utterance.addElement("w").addAttribute(XML_ID, "w" + this.getWordCounter()).addText(replaceSpecialCharsInWord(w.getContent().toString()));

      // add punctuations
      List<Label> punctuationLabels = this.getAnnotationElements().getListOfPunctuationsStartingWithAndNotEndingBefore(w.getStartTime(), w.getEndTime());
      if (punctuationLabels != null) {
        for (Label p : punctuationLabels) {
          addPunctuation(p, utterance);
        }
      }

      // add phones
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
    if (l != null && l.getIsPhon() && !l.getIgnorePhon()) {

      String realizedPhone = l.getRealizedPhon();
      String canonicalPhone = null;

      realizedPhone = this.getCharConverter().getUnicodeByASCII(realizedPhone);

      if (realizedPhone != null) {
        if (l.getRealizedPhoneIsStressed()) {
          if (l.getRealizedPhoneStressType().isPrimary()) {
            realizedPhone = this.getCharConverter().getUnicodeByASCII("pri_stress") + realizedPhone;
          } else {
            realizedPhone = this.getCharConverter().getUnicodeByASCII("sec_stress") + realizedPhone;
          }
        }
      }

      if (l.getPhonIsDeleted() || l.getPhonIsReplaced()) {
        canonicalPhone = l.getModifiedPhon();
        canonicalPhone = this.getCharConverter().getUnicodeByASCII(canonicalPhone);
        if (canonicalPhone != null) {
          if (l.getModifiedPhoneIsStressed()) {
            if (l.getModifiedPhoneStressType().isPrimary()) {
              canonicalPhone = this.getCharConverter().getUnicodeByASCII("pri_stress") + canonicalPhone;
            } else {
              canonicalPhone = this.getCharConverter().getUnicodeByASCII("sec_stress") + canonicalPhone;
            }
          }
        }
      } else {
        canonicalPhone = realizedPhone;
      }

      if (realizedPhone != null) {

        if (l.getIsCreaked()) {
          realizedPhone = realizedPhone + this.getCharConverter().getUnicodeByASCII("creaked");
        }

        if (l.getIsNasalized()) {
          realizedPhone = realizedPhone + this.getCharConverter().getUnicodeByASCII("nasalized");
        }

        this.setSpanCounter(this.getSpanCounter() + 1);
        realizedPhonesSpanGrp.addElement("span").addAttribute(FROM, "#" + l.getStartTime().getName()).addAttribute(TO, "#" + l.getEndTime().getName()).addAttribute(XML_ID, "s" + this.getSpanCounter()).addText(realizedPhone);
      }

      if (canonicalPhone != null) {
        this.setSpanCounter(this.getSpanCounter() + 1);
        canonicalPhonesSpanGrp.addElement("span").addAttribute(FROM, "#" + l.getStartTime().getName()).addAttribute(TO, "#" + l.getEndTime().getName()).addAttribute(XML_ID, "s" + this.getSpanCounter()).addText(canonicalPhone);
      }
    }
  }

  private void addPunctuation (Label p, Element utterance) throws JaxenException {

    // TODO: How to deal with uncertain punctuations?

    if (p != null) {
      this.setPcCounter(this.getPcCounter() + 1);
      utterance.addElement("pc").addAttribute(XML_ID, "pc" + this.getPcCounter()).addText(p.getPunctuation());
    }
  }

  /**
   * Gets a word and replaces special chars (e.g. umlauts)
   * @param w word with simplified special chars
   * @return  word with "real" special chars
   */
  private String replaceSpecialCharsInWord (String w) {
    String rval = null;

    if (w != null) {
      // umlauts and sz
      rval = w.replaceAll("\"a", "ä").replaceAll("\"A", "Ä").replaceAll("\"o", "ö").replaceAll("\"O", "Ö").replaceAll("\"u", "ü").replaceAll("\"U", "Ü").replaceAll("\"s", "ß");

      // hesitational lengthening
      rval = rval.replaceAll("<Z>", " - ");

      // < and > are used for hesistations, e.g. <"ahm>
      // * is used to mark neologisms
      rval = rval.replaceAll("[<>*]", "");
    }

    return rval;
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
