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

  private static final String AUTHORITY = "Institut für Skandinavistik, Frisistik und Allgemeine Sprachwissenschaft - Abteilung für Allgemeine Sprachwissenschaft";
  private static final String DISTRIBUTOR = AUTHORITY;
  private static final String STREET = "Ohlshausenstraße 40";
  private static final String POSTCODE = "24098";
  private static final String PLACENAME = "Kiel";
  private static final String COUNTRY = "Germany";

  private static final String XML_ID = "xml:id";
  private static final Namespace XMLNS = Namespace.get("http://www.tei-c.org/ns/1.0");

  private static final String FROM = "from";
  private static final String TO = "to";
  private static final String START = "start";
  private static final String END = "end";

  private static final String REALIZED_PHONE_TYPE = "pho-realized";
  private static final String CANONICAL_PHONE_TYPE = "pho-canonical";

  private static final String PROSODY_TYPE = "prolab";

  private Map namespaceMap;
  private XPath xpath;

  private AnnotationElementCollection annotationElements;

  private KCSampaToIPAConverter charConverter;

  private Integer utteranceCounter;
  private Integer wordCounter;
  private Integer spanCounter;
  private Integer pcCounter;
  private Integer vocalCounter;
  private Integer pauseCounter;

  private String audioFileName;

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
    this.setVocalCounter(0);
    this.setPauseCounter(0);
    this.setAudioFileName(null);

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

  public TEIDoc (AnnotationElementCollection annotationElements, KCSampaToIPAConverter charConverter, String audioFileName) throws JaxenException {
    this();
    this.annotationElements = annotationElements;
    this.charConverter = charConverter;
    this.setAudioFileName(audioFileName);
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

  public Integer getVocalCounter() {
    return vocalCounter;
  }

  public void setVocalCounter(Integer vocalCounter) {
    this.vocalCounter = vocalCounter;
  }

  public Integer getPauseCounter() {
    return pauseCounter;
  }

  public void setPauseCounter(Integer pauseCounter) {
    this.pauseCounter = pauseCounter;
  }

  public String getAudioFileName() {
    return audioFileName;
  }

  public void setAudioFileName(String audioFileName) {
    this.audioFileName = audioFileName;
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
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc").addElement("publicationStmt").addElement("authority").addText(AUTHORITY);
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt").addElement("distributor").addText(DISTRIBUTOR);
    //addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt").addElement("availability").addElement("p").addText("TODO");
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt").addElement("address").addElement("street").addText(STREET);
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("postCode").addText(POSTCODE);
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("placeName").addText(PLACENAME);
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc/tei:publicationStmt/tei:address").addElement("country").addText(COUNTRY);
    addElementFoundByXpath("/tei:TEI/tei:teiHeader/tei:fileDesc").
            addElement("sourceDesc").addElement("recordingStmt").
            addElement("recording").addAttribute("type", "audio").
            addElement("media").addAttribute("mimeType", "audio/wav").
            addAttribute("url", getAudioFileName());

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

    // There are two types of annotation files in Kiel Corpus
    //   - the first contains no phrase information
    //   - the second contains phrase information
    //
    // If there is no phrase information, we take the word as
    // the biggest unit in hierarchy, else we take the phrase
    // as the biggest unit.
    //

    // we expect a sorted list of timed annotation elements

    TimeMark start = null;
    TimeMark end = null;

    // amountOfPhraseEndLabels < 1: first type of annotation file
    // amountOfPhraseEndLabels >=1: second type of annotation file

    int amountOfPhraseEndLabels = this.annotationElements.getAmountOfPhraseEndLabels();

    for (TimedAnnotationElement t : this.getAnnotationElements().getAnnotationElements()) {

      if (amountOfPhraseEndLabels >= 1) {

        // build a block for each phrase start and end pair

        if (t.getClass() == Label.class && ((Label) t).getIsPhraseBegin()) {
          start = t.getStartTime();
          if (end != null) {
            addStuffBetweenTwoBiggestElements(end, start);
            end = null;
          }
        }

        if (t.getClass() == Label.class && ((Label) t).getIsPhraseEnd() && start != null) {
          end = t.getEndTime();

          addBiggestElementWithSubordinatedElements(amountOfPhraseEndLabels, start, end);

          start = null;
        }

      } else {
        // amountOfPhraseEndLabels < 1

        // we don't have enough phrase labels to build phrase blocks
        // so build word blocks

        if (t.getClass() == Word.class) {
          start = t.getStartTime();
          if (end != null) {
            addStuffBetweenTwoBiggestElements(end, start);
          }
          end = t.getEndTime();

          addBiggestElementWithSubordinatedElements(amountOfPhraseEndLabels, start, end);

          start = null;
        }
      }
    }
  }

  private void addStuffBetweenTwoBiggestElements (TimeMark endOfCurrentElement, TimeMark startOfNextElement) throws JaxenException {

    Element rootElement = addElementFoundByXpath("/tei:TEI/tei:text/tei:body");

    List<TimedAnnotationElement> elements;

    elements = annotationElements.getListOfTimedAnnotationElementsStartingWithAndNotEndingBefore(endOfCurrentElement, startOfNextElement);

    for (TimedAnnotationElement e : elements) {
      // in between two superordinated elements we can only have noises at the moment

      // add noise
      if (e.getClass() == Label.class && ((Label) e).getIsVocalNoise() && !((Label) e).getVocalNoiseIsDeleted()) {
        addVocalNoise((Label) e, rootElement);
      }
    }
  }

  private void addBiggestElementWithSubordinatedElements (int amountOfPhraseEndLabels, TimeMark start, TimeMark end) throws JaxenException {
    if (amountOfPhraseEndLabels >= 0 && start != null && end != null) {

      // create TEI document elements for current phrase and related elements
      Element annotationBlock = addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("annotationBlock").
              addAttribute(START, "#" + start.getName()).addAttribute(END, "#" + end.getName());

      List<TimedAnnotationElement> elements;

      if (amountOfPhraseEndLabels >= 1) {
        elements = annotationElements.getListOfTimedAnnotationElementsWithinPhraseStartingWithAndNotEndingBefore(start, end);
      } else {
        elements = annotationElements.getListOfTimedAnnotationElementsWithinWordStartingWithAndNotEndingBefore(start, end);
      }

      if (elements != null) {

        // an utterance contains words, punctuations and noises
        this.setUtteranceCounter(this.getUtteranceCounter() + 1);
        Element utterance = annotationBlock.addElement("u").addAttribute(XML_ID, "u" + this.getUtteranceCounter());

        Element realizedPhonesSpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", REALIZED_PHONE_TYPE);
        Element canonicalPhonesSpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", CANONICAL_PHONE_TYPE);
        Element prosodySpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", PROSODY_TYPE);

        for (TimedAnnotationElement e : elements) {

          // add word
          if (e.getClass() == Word.class) {
            this.setWordCounter(this.getWordCounter() + 1);

            // to provide the information where a word starts and ends
            // we use the synch-attribute in the word tag for the starting point
            // and a following anchor element with a synch-attribute for the ending point
            utterance.addElement("w").addAttribute(XML_ID, "w" + this.getWordCounter()).
                    addAttribute("synch", "#" + e.getStartTime().getName()).
                    addText(replaceSpecialCharsInWord(e.getContent().toString()));
            utterance.addElement("anchor").addAttribute("synch", "#" + e.getEndTime().getName());
          }

          if (e.getClass() == Label.class) {

            // add punctuation
            if (((Label) e).getIsPunctuation() && !((Label) e).getIgnorePunctuation()) {
              addPunctuation((Label) e, utterance);
            }

            // add noise
            if (((Label) e).getIsVocalNoise() && !((Label) e).getVocalNoiseIsDeleted()) {
              addVocalNoise((Label) e, utterance);
            }

            // add phone
            if (((Label) e).getIsPhon() && !((Label) e).getIgnorePhon()) {
              addPhone((Label) e, canonicalPhonesSpanGrp, realizedPhonesSpanGrp);
            }

            // add prosody
            if (((Label) e).getIsProsodicLabel()) {
              addProsody((Label) e, prosodySpanGrp);
            }
          }
        }
      }
    }
  }

  private void addPunctuation (Label p, Element utterance) throws JaxenException {

    // TODO: How to deal with uncertain punctuations?

    if (p != null) {
      this.setPcCounter(this.getPcCounter() + 1);
      utterance.addElement("pc").
              addAttribute(XML_ID, "pc" + this.getPcCounter()).
              addText(p.getPunctuation());
    }
  }

  private void addProsody (Label p, Element spanGrp) throws JaxenException {
    if (p != null) {
      this.setSpanCounter(this.getSpanCounter() + 1);
      spanGrp.addElement("span").
              addAttribute(FROM, "#" + p.getStartTime().getName()).
              addAttribute(TO, "#" + p.getEndTime().getName()).
              addAttribute(XML_ID, "s" + this.getSpanCounter()).
              addText(p.getProsodicLabel().replaceAll("#", ""));
    }
  }

  private void addVocalNoise (Label v, Element utterance) throws JaxenException {
    if (v != null) {
      if (!v.getIsPause()) {
        this.setVocalCounter(this.getVocalCounter() + 1);
        utterance.addElement("vocal").
                addAttribute(XML_ID, "v" + this.getVocalCounter()).
                addAttribute(START, "#" + v.getStartTime().getName()).
                addAttribute(END, "#" + v.getEndTime().getName()).
                addElement("desc").addText(v.getVocalNoiseType());
      } else {
        this.setPauseCounter(this.getPauseCounter() + 1);
        utterance.addElement("pause").
                addAttribute(XML_ID, "p" + this.getPauseCounter()).
                addAttribute(START, "#" + v.getStartTime().getName()).
                addAttribute(END, "#" + v.getEndTime().getName());
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
        realizedPhonesSpanGrp.addElement("span").
                addAttribute(FROM, "#" + l.getStartTime().getName()).
                addAttribute(TO, "#" + l.getEndTime().getName()).
                addAttribute(XML_ID, "s" + this.getSpanCounter()).
                addText(realizedPhone);
      }

      if (canonicalPhone != null) {
        this.setSpanCounter(this.getSpanCounter() + 1);
        canonicalPhonesSpanGrp.addElement("span").
                addAttribute(FROM, "#" + l.getStartTime().getName()).
                addAttribute(TO, "#" + l.getEndTime().getName()).
                addAttribute(XML_ID, "s" + this.getSpanCounter()).
                addText(canonicalPhone);
      }
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
      rval = rval.replaceAll("<Z>", "|Z|");

      // < and > are used for hesistations, e.g. <"ahm>
      // * is used to mark neologisms
      rval = rval.replaceAll("[<>*]", "");

      // hesitational lengthening
      rval = rval.replaceAll("\\|Z\\|", "<Zögern>");
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
