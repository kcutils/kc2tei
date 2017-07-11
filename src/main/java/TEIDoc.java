import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class TEIDoc{

  private Document doc;
  private Document docCopy; // holds a copy that can be modified (namespace)
                            // without modifying original doc

  private Namespace xmlns = Namespace.get("http://www.tei-c.org/ns/1.0");

  private int nextTimeLineEntry = 0;  // holds timeline entry that can be added next
                                      // (n-1) entries were already added

  private String lastInterval = "-1"; // holds the last given interval in order to
                                      // be able to avoid duplicate timeline entries

  public TEIDoc() {

    // create XML document
    this.doc = DocumentHelper.createDocument();

    // built common header informations

    // if we define namespace at this place then xpathes won't work
    // this.doc.addElement("TEI", "http://www.tei-c.org/ns/1.0");
    this.doc.addElement("TEI");

    // find some nodes by xpathes and cast them as elements to add subelements and attributes
    ((Element) this.doc.selectSingleNode("/TEI")).addElement("teiHeader");
    ((Element) this.doc.selectSingleNode("/TEI/teiHeader")).addElement("fileDesc");
    ((Element) this.doc.selectSingleNode("/TEI/teiHeader/fileDesc")).addElement("titleStmt")
                                                                    .addElement("title")
                                                                    .addText("TODO");
    Element pubStmt = DocumentHelper.createElement("publicationStmt");
    ((Element) this.doc.selectSingleNode("/TEI/teiHeader/fileDesc")).content().add(1, pubStmt);
    ((Element) this.doc.selectSingleNode("/TEI/teiHeader/fileDesc/publicationStmt")).addElement("authority")
                                                                                    .addText("ISFAS");
    ((Element) this.doc.selectSingleNode("/TEI/teiHeader/fileDesc/publicationStmt")).addElement("availability")
                                                                                    .addElement("p")
                                                                                    .addText("TODO");
    ((Element) this.doc.selectSingleNode("/TEI/teiHeader/fileDesc/publicationStmt")).addElement("address")
                                                                                    .addElement("street")
                                                                                    .addText("TODO");
    ((Element) this.doc.selectSingleNode("/TEI/teiHeader/fileDesc/publicationStmt/address")).addElement("postCode")
                                                                                            .addText("24-TODO");
    ((Element) this.doc.selectSingleNode("/TEI/teiHeader/fileDesc/publicationStmt/address")).addElement("placeName")
                                                                                            .addText("Kiel");
    ((Element) this.doc.selectSingleNode("/TEI/teiHeader/fileDesc/publicationStmt/address")).addElement("country")
                                                                                            .addText("Germany");
    Element srcDesc = DocumentHelper.createElement("sourceDesc");
    ((Element) this.doc.selectSingleNode("/TEI/teiHeader/fileDesc")).content().add(2, srcDesc);
    ((Element) this.doc.selectSingleNode("/TEI/teiHeader/fileDesc/sourceDesc")).addElement("recordingStmt")
                                                                               .addElement("recording")
                                                                               .addAttribute("type", "audio");
    ((Element) this.doc.selectSingleNode("/TEI")).addElement("text");
    ((Element) this.doc.selectSingleNode("/TEI/text")).addElement("body");
    
  }

  // get a duration (interval) as String and create a timeline entry
  // first timeline entry is an absolute reference point
  public void addTimeLineEntry (String interval) {
    if (nextTimeLineEntry == 0) {
      // create timeline node
      // and absolute reference point
      Element timeline = DocumentHelper.createElement("timeline");
      timeline.addAttribute("unit", "s");
      ((Element) this.doc.selectSingleNode("/TEI/text")).content().add(0, timeline);
      ((Element) this.doc.selectSingleNode("/TEI/text/timeline")).addElement("when")
                                                                 .addAttribute("xml:id", "T0");
      nextTimeLineEntry++;
    }

    if (! lastInterval.equals(interval)) {

      // create time stamp relative to absolute reference point
      ((Element) this.doc.selectSingleNode("/TEI/text/timeline")).addElement("when")
                                                                 .addAttribute("xml:id", "T" + nextTimeLineEntry)
                                                                 .addAttribute("interval", interval)
                                                                 .addAttribute("since", "#T0");
      lastInterval = interval;
      nextTimeLineEntry++;
    }
  }

  public void addUnspecifiedUtterance (String u) {
    ((Element) this.doc.selectSingleNode("/TEI/text/body")).addElement("u")
                                                           .addAttribute("start", "#T" + (nextTimeLineEntry + 1))
                                                           .addAttribute("end", "#T" + (nextTimeLineEntry + 2))
                                                           .addText(u);
  }

  // return XML document
  // enriched by namespace information
  public Document get () {
    // ugly hack to avoid problems with xpath when namespace is set
    this.docCopy = doc;
//    Element r = this.docCopy.getRootElement();
//    for ( Iterator i = r.elementIterator(); i.hasNext(); ) {
//      Element e = (Element) i.next();
//      e.add(xmlns);
//    }
//    r.add(xmlns);
//    this.docCopy.getRootElement().addNamespace("", xmlns);
    return docCopy;
  }

  // write XML document to stdout
  public void writeSout () throws Exception {
    // Pretty print the document to System.out
    OutputFormat format = OutputFormat.createPrettyPrint();
    XMLWriter writer = new XMLWriter(System.out, format);
    writer.write(this.get());
  }

}
