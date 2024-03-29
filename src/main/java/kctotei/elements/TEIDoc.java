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
import java.time.ZonedDateTime;
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

  private static final String MISC_TYPE = "misc";

  private static final String ERROR_TYPE = "error";

  private static final String FALSE_START_ERROR_DESC = "Fehlstart";
  private static final String TRUNCATION_ERROR_DESC = "Abbruch";

  private Map<String, String> namespaceMap;
  private XPath xpath;

  private AnnotationElementCollection annotationElements;

  private KCSampaToIPAConverter charConverter;

  private int utteranceCounter;
  private int wordCounter;
  private int spanCounter;
  private int pcCounter;
  private int vocalCounter;
  private int pauseCounter;

  private String audioFileName;

  private String converterName;
  private String converterVersion;

  private TEIDoc () {
    this.setDoc(null);
    this.setNamespaceMap(new HashMap<String,String>());
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

  public TEIDoc (AnnotationElementCollection annotationElements, KCSampaToIPAConverter charConverter, String audioFileName, String converterName, String converterVersion) throws JaxenException {
    this();
    this.annotationElements = annotationElements;
    this.charConverter = charConverter;
    this.setAudioFileName(audioFileName);
    this.setConverterName(converterName);
    this.setConverterVersion(converterVersion);
    init();
  }

  public Document getDoc () {
    return doc;
  }

  public void setDoc (Document doc) {
    this.doc = doc;
  }

  public Map<String, String> getNamespaceMap () {
    return namespaceMap;
  }

  public void setNamespaceMap (Map<String, String> namespaceMap) {
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

  public int getUtteranceCounter () {
    return utteranceCounter;
  }

  public void setUtteranceCounter (int utteranceCounter) {
    this.utteranceCounter = utteranceCounter;
  }

  public int getWordCounter () {
    return wordCounter;
  }

  public void setWordCounter (int wordCounter) {
    this.wordCounter = wordCounter;
  }

  public int getSpanCounter () {
    return spanCounter;
  }

  public void setSpanCounter (int spanCounter) {
    this.spanCounter = spanCounter;
  }

  public int getPcCounter () {
    return pcCounter;
  }

  public void setPcCounter (int pcCounter) {
    this.pcCounter = pcCounter;
  }

  public int getVocalCounter() {
    return vocalCounter;
  }

  public void setVocalCounter(int vocalCounter) {
    this.vocalCounter = vocalCounter;
  }

  public int getPauseCounter() {
    return pauseCounter;
  }

  public void setPauseCounter(int pauseCounter) {
    this.pauseCounter = pauseCounter;
  }

  public String getAudioFileName() {
    return audioFileName;
  }

  public void setAudioFileName(String audioFileName) {
    this.audioFileName = audioFileName;
  }

  public String getConverterName() {
    return converterName;
  }

  public void setConverterName(String converterName) {
    this.converterName = converterName;
  }

  public String getConverterVersion() {
    return converterVersion;
  }

  public void setConverterVersion(String converterVersion) {
    this.converterVersion = converterVersion;
  }

  private void init () throws JaxenException {

    createXMLdoc();
    createXMLHeader();

    // add text tag
    addElementFoundByXpath("/tei:TEI").addElement("text");

    // add timeline
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
    this.getDoc().
            addComment(" ******************************************************************************************************** ").
            addComment(" result of transformation from legacy Kiel Corpus File Format to Kiel Corpus ISO/TEI (KCTEI)-Format using ").
            addComment(" " + String.format("%1$-104s", this.getConverterName() + " version " + this.getConverterVersion() + " on " + ZonedDateTime.now()) + " ").
            addComment(" ******************************************************************************************************** ");
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
    addElementFoundByXpath("/tei:TEI/tei:text").addElement("timeline").addAttribute("unit", "s").addElement("when").addAttribute(XML_ID, "T0");

    for (int i = 0; i < annotationElements.getTimeMarkerList().size(); i++) {
      addElementFoundByXpath("/tei:TEI/tei:text/tei:timeline").addElement("when").addAttribute(XML_ID, annotationElements.getTimeMarkerList().get(i).getName()).
                                                                                                                                                                     addAttribute("interval", Float.toString(annotationElements.getTimeMarkerList().get(i).getTime())).
                                                                                                                                                                                                                                                                          addAttribute("since", "#T0");
    }
  }

  private void addContent () throws JaxenException {

    // There are two types of annotation files in Kiel Corpus
    //   - the first contains no phrase information (*.s1)
    //   - the second contains phrase information (*.s2)
    //
    // If there is no phrase information, we take the word as
    // the biggest unit in hierarchy, else we take the phrase
    // as the biggest unit.
    //

    // we expect a sorted list of timed annotation elements

    TimeMark start = null;
    TimeMark end = this.getAnnotationElements().getTimeMarkerList().get(0);

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


  // biggest element can either be a word (s1-type file) or a phrase (s2-type file)
  //
  // for example: if a word is our biggest element (in s1-type-files)
  // then we want all elements that do not belong to that word to appear
  // from the end of the current word and the beginning of the next word
  //
  private void addStuffBetweenTwoBiggestElements (TimeMark endOfCurrentElement, TimeMark startOfNextElement) throws JaxenException {

    Element rootElement = addElementFoundByXpath("/tei:TEI/tei:text/tei:body");

    List<TimedAnnotationElement> elements;

    elements = annotationElements.getListOfTimedAnnotationElementsStartingWithAndNotEndingBefore(endOfCurrentElement, startOfNextElement);

    for (TimedAnnotationElement e : elements) {
      // in between two superordinated elements we can only have noises at the moment

      // add noise
      if (e.getClass() == Label.class && ((Label) e).getIsVocalNoise() && !((Label) e).getVocalNoiseIsDeleted()) {
        // we don't want vocal noises without duration at this moment
        if (((Label) e).getStartTime() != ((Label) e).getEndTime()) {
          addVocalNoise((Label) e, rootElement);
        }
      }
    }
  }

  //
  // biggest element can either be a word (s1-type file) or a phrase (s2-type file)
  //
  private void addBiggestElementWithSubordinatedElements (int amountOfPhraseEndLabels, TimeMark start, TimeMark end) throws JaxenException {
    if (amountOfPhraseEndLabels >= 0 && start != null && end != null) {

      // create TEI document elements for current biggest element and related elements

      // an annotationBlock holds it all
      Element annotationBlock = addElementFoundByXpath("/tei:TEI/tei:text/tei:body").addElement("annotationBlock").
              addAttribute(START, "#" + start.getName()).addAttribute(END, "#" + end.getName());

      List<TimedAnnotationElement> elements;

      if (amountOfPhraseEndLabels >= 1) {
        // biggest element is a phrase (s2-type file)
        elements = annotationElements.getListOfTimedAnnotationElementsWithinPhraseStartingWithAndNotEndingBefore(start, end);
      } else {
        // biggest element is a word (s1-type file)
        elements = annotationElements.getListOfTimedAnnotationElementsWithinWordStartingWithAndNotEndingBefore(start, end);
      }

      if (elements != null) {

        // an utterance contains words, punctuations and noises
        this.setUtteranceCounter(this.getUtteranceCounter() + 1);
        Element utterance = annotationBlock.addElement("u").addAttribute(XML_ID, "u" + this.getUtteranceCounter());

        Element realizedPhonesSpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", REALIZED_PHONE_TYPE);
        Element canonicalPhonesSpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", CANONICAL_PHONE_TYPE);
        Element prosodySpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", PROSODY_TYPE);
        Element errorSpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", ERROR_TYPE);
        Element miscSpanGrp = annotationBlock.addElement("spanGrp").addAttribute("type", MISC_TYPE);

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
              // we don't want noises without duration at the moment
              if (((Label) e).getStartTime() != ((Label) e).getEndTime()) {
                addVocalNoise((Label) e, utterance);
              }
            }

            // add phone
            if (((Label) e).getIsPhon() && !((Label) e).getIgnorePhon()) {
              addPhone((Label) e, canonicalPhonesSpanGrp, realizedPhonesSpanGrp);
            }

            // add prosody
            if (((Label) e).getIsProsodicLabel()) {
              addProsody((Label) e, prosodySpanGrp);
            }

            // add misc
            // add MA mark
            // add FWM (function word mark)
            if ((((Label) e).getIsPhon() && ((Label) e).getIsMAModifier()) ||
                    (((Label) e).getIsFwm())) {
              addMisc((Label) e, miscSpanGrp);
            }

            // add speak errors
            if (((Label) e).getIsFalseStart() || ((Label) e).getIsTruncation()) {
              addError((Label) e, errorSpanGrp);
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
        if (! l.getPhonIsInserted()) {
          canonicalPhone = realizedPhone;
        }
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

  private void addError (Label l, Element errorSpanGrp) {
    if (l != null && (l.getIsTruncation() || l.getIsFalseStart())) {
      this.setSpanCounter(this.getSpanCounter() + 1);

      // determine type of incident
      String type = "unbekannt";

      if (l.getIsTruncation()) {
        type = TRUNCATION_ERROR_DESC;
      }
      if (l.getIsFalseStart()) {
        type = FALSE_START_ERROR_DESC;
      }

      errorSpanGrp.addElement("span").
              addAttribute(FROM, "#" + l.getStartTime().getName()).
              addAttribute(TO, "#" + l.getEndTime().getName()).
              addAttribute(XML_ID, "s" + this.getSpanCounter()).
              addText(type);
    }
  }

  private void addMisc (Label l, Element miscSpanGrp) {
    if (l != null && l.getIsPhon() && l.getIsMAModifier() && miscSpanGrp != null) {
      this.setSpanCounter(this.getSpanCounter() + 1);
      miscSpanGrp.addElement("span").
              addAttribute(FROM, "#" + l.getStartTime().getName()).
              addAttribute(TO, "#" + l.getEndTime().getName()).
              addAttribute(XML_ID, "s" + this.getSpanCounter()).
              addText("MA");
    }

    // FWM - function word marker is at end of function words - this means: the last label
    // it has no duration
    if (l != null && l.getIsFwm() && miscSpanGrp != null) {
      this.setSpanCounter(this.getSpanCounter() + 1);
      miscSpanGrp.addElement("span").
              addAttribute(FROM, "#" + l.getEndTime().getName()).
              addAttribute(TO, "#" + l.getEndTime().getName()).
              addAttribute(XML_ID, "s" + this.getSpanCounter()).
              addText("FWM");
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
    } catch (Exception ignored) {
    }
    return "";
  }


}
