import java.util.HashMap;
import java.util.Map;

public class KCSampaToIPAConverter {
  HashMap<String,String> convertTable = new HashMap<>();


  String test = "\u263A";

  public KCSampaToIPAConverter() {

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
    convertTable.put("a~", "\u0061\u0303");
    convertTable.put("E~", "\u025B\u0303");
    convertTable.put("O~", "\u0254\u0303");
    convertTable.put("9~", "\u0153\u0303");
    convertTable.put("aI", "\u0061\u026A");
    convertTable.put("aU", "\u0061\u028A");
    convertTable.put("OY", "\u0254\u028F");
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

    // schwar reductions
    convertTable.put("a6", "\u0061\u0250");
    convertTable.put("a:6", "\u0061\u02D0\u0250");
    convertTable.put("e:6", "\u0065\u02D0\u0250");
    convertTable.put("E6", "\u025B\u0250");
    convertTable.put("i:6", "\u0069\u02D0\u0250");
    convertTable.put("Y6", "\u028F\u0250");
    convertTable.put("o:6", "\u006F\u02D0\u0250");





//    for (Map.Entry<String,String> e : convertTable.entrySet()) {
//      System.out.println(e.getKey() + " -> " + e.getValue());
//    }
  }

  public String getUnicodeByASCII (String in) {
    String out = "";

    if (convertTable.get(in) != null) {
      out = convertTable.get(in);
    } else {
      // DEBUG
      //out = "XSAMPA: " + in;
      out = in;
    }

    return out;
  }
}
