import java.util.HashMap;

public class KCSampaToIPAConverter {
 
  private HashMap<String,String> convertTable = new HashMap<>();

  // for checks/debug
  private Integer noHits = 0;
  private Boolean debugMode = false;

  public KCSampaToIPAConverter () {

    // init hashmap

    // german vowels
    convertTable.put("a", "\u0061");
    convertTable.put("a:", "\u0061\u02D0");
    convertTable.put("e:", "\u0065\u02D0");
    convertTable.put("E", "\u025B");
    convertTable.put("i:", "\u0069\u02D0");
    convertTable.put("I", "\u026A");
    convertTable.put("o:", "\u006F\u02D0");
    convertTable.put("O", "\u0254");
    convertTable.put("u:", "\u0075\u02D0");
    convertTable.put("U", "\u028A");
    convertTable.put("y:", "\u0079\u02D0");
    convertTable.put("Y", "\u028F");
    convertTable.put("E:", "\u025B\u02D0");
    convertTable.put("2:", "\u00F8\u02D0");
    convertTable.put("9", "\u0153");
    // nasal vowels
    convertTable.put("a~", "\u0061\u0303");
    convertTable.put("E~", "\u025B\u0303");
    convertTable.put("O~", "\u0254\u0303");
    convertTable.put("9~", "\u0153\u0303");
    // diphthongs
    convertTable.put("aI", "\u0061\u026A");
    convertTable.put("aU", "\u0061\u028A");
    convertTable.put("OY", "\u0254\u028F");
    // schwa/r
    convertTable.put("@", "\u0259");
    convertTable.put("6", "\u0250");

    // german consonants
    convertTable.put("b", "\u0062");
    convertTable.put("d", "\u0064");
    convertTable.put("f", "\u0066");
    convertTable.put("g", "\u0261");
    convertTable.put("h", "\u0068");
    convertTable.put("j", "\u006A");
    convertTable.put("k", "\u006B");
    convertTable.put("l", "\u006C");
    convertTable.put("m", "\u006D");
    convertTable.put("n", "\u006E");
    convertTable.put("p", "\u0070");
    convertTable.put("r", "\u0072");
    convertTable.put("s", "\u0073");
    convertTable.put("t", "\u0074");
    convertTable.put("v", "\u0076");
    convertTable.put("z", "\u007A");
    convertTable.put("C", "\u00E7");
    convertTable.put("x", "\u0078");
    convertTable.put("S", "\u0283");
    convertTable.put("Z", "\u0292");
    convertTable.put("N", "\u014B");
    convertTable.put("Q", "\u0294");

    // aspirations
    convertTable.put("ph", "\u0070\u02B0");
    convertTable.put("th", "\u0074\u02B0");
    convertTable.put("kh", "\u006B\u02B0");
    convertTable.put("bh", "\u0062\u02B0");
    convertTable.put("dh", "\u0064\u02B0");
    convertTable.put("gh", "\u0261\u02B0");

    // syllabic schwar
    convertTable.put("=6", "\u0250\u0329");

    // schwar reductions
    convertTable.put("a6", "\u0061\u0250");
    convertTable.put("a:6", "\u0061\u02D0\u0250");
    convertTable.put("e:6", "\u0065\u02D0\u0250");
    convertTable.put("E6", "\u025B\u0250");
    convertTable.put("E:6", "\u025B\u02D0\u0250");
    convertTable.put("i:6", "\u0069\u02D0\u0250");
    convertTable.put("I6", "\u026A\u0250");
    convertTable.put("Y6", "\u028F\u0250");
    convertTable.put("o:6", "\u006F\u02D0\u0250");
    convertTable.put("O6", "\u0254\u0250");
    convertTable.put("96", "\u0153\u0250");
    convertTable.put("U6", "\u028A\u0250");
    convertTable.put("y:6", "\u0079\u02D0\u0250");
    convertTable.put("u:6", "\u0075\u02D0\u0250");
    convertTable.put("2:6", "\u00F8\u02D0\u0250");

    // diacritica
    convertTable.put("creaked", "\u0330");

  }

  public String getUnicodeByASCII (String in) {
    String rval = "";

    if (convertTable.get(in) != null) {
      rval = convertTable.get(in);
    } else {
      noHits++;
      if (debugMode) {
        rval = "XSAMPA: " + in;
      } else {
        rval = in;
      }
    }

    return rval;
  }

  public Integer getNoHits () {
    return noHits;
  }

  public void setDebugMode (Boolean b) {
    this.debugMode = b;
  }
}
