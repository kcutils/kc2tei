import com.thaiopensource.validate.SchemaReader;
import com.thaiopensource.validate.ValidationDriver;
import com.thaiopensource.validate.auto.AutoSchemaReader;
import org.xml.sax.InputSource;

import java.io.*;

public class XMLVerificator {

  private TEIDoc doc = null;
  private File rngSchemaFile = null;

  private SchemaReader sr = null;
  private ValidationDriver vd = null;
  private InputSource inRng = null;
  private InputSource inDoc = null;

  public XMLVerificator (TEIDoc doc, File rngSchemaFile) throws Exception {
    this.doc = doc;
    this.rngSchemaFile = rngSchemaFile;

    sr = new AutoSchemaReader();
    vd = new ValidationDriver(sr);
    inRng = ValidationDriver.fileInputSource(rngSchemaFile);
    inRng.setEncoding("UTF-8");
    vd.loadSchema(inRng);

    StringReader docStr = new StringReader(doc.toString());
    inDoc = new InputSource(docStr);
    inDoc.setEncoding("UTF-8");
  }

  public Boolean validate () throws Exception {
    Boolean rval = false;
    rval = vd.validate(inDoc);
    return rval;
  }
}
