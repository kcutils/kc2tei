import kctotei.elements.Label;
import labels.node.ATlabel;
import labels.node.PTlabel;
import labels.node.Switch;
import org.junit.Assert;
import org.junit.Test;

public class LabelTest {

  @Test
  public void addStringAndDescrToStringTest (){

    Label l = new Label(new ATlabel());

    Assert.assertEquals(null, l.addStringAndDescrToString(null,null,null));
    Assert.assertEquals("", l.addStringAndDescrToString(null,null,""));
    Assert.assertEquals("", l.addStringAndDescrToString("", "", ""));
    Assert.assertEquals("", l.addStringAndDescrToString("", "a", ""));
    Assert.assertEquals("", l.addStringAndDescrToString("a", "", ""));
    Assert.assertEquals("b", l.addStringAndDescrToString("", "", "b"));
    Assert.assertEquals("b", l.addStringAndDescrToString("", "a", "b"));
    Assert.assertEquals("b", l.addStringAndDescrToString("a", "", "b"));
    Assert.assertEquals("  desc: a", l.addStringAndDescrToString("a", "desc", ""));
    Assert.assertEquals("b  desc: a", l.addStringAndDescrToString("a", "desc", "b"));
  }

  @Test
  public void addTrueBooleansAndDescrToStringTest () {
    Label l = new Label(new ATlabel());

    Assert.assertEquals(null, l.addTrueBooleansAndDescrToString(false, "",null));
    Assert.assertEquals(null, l.addTrueBooleansAndDescrToString(true, "",null));
    Assert.assertEquals("  d", l.addTrueBooleansAndDescrToString(true, "d",null));
    Assert.assertEquals("", l.addTrueBooleansAndDescrToString(false, "",""));
    Assert.assertEquals("", l.addTrueBooleansAndDescrToString(true, "",""));
    Assert.assertEquals("  desc", l.addTrueBooleansAndDescrToString(true, "desc",""));
    Assert.assertEquals("b  desc", l.addTrueBooleansAndDescrToString(true, "desc","b"));
  }
}
