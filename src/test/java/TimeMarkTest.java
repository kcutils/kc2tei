import kctotei.elements.TimeMark;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TimeMarkTest {

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void testSetTimeAndName () {
    String floatString = "   0.5";
    Float floatFloat = 0.5f;

    String setName = "     T5";
    String name = "T5";

    TimeMark t = new TimeMark(floatString);
    Assert.assertTrue(t.getTime().equals(floatFloat) );

    TimeMark t1 = new TimeMark(setName, floatString);
    Assert.assertTrue(t1.getName().equals(name));
  }

  @Test
  public void testReturnMoreComplete () {
    TimeMark t1 = new TimeMark("0.5");
    TimeMark t2 = new TimeMark("T2", "0.5");
    TimeMark t3 = new TimeMark("0.5");

    Assert.assertTrue(t1.returnMoreComplete(t1) == t1);
    Assert.assertTrue(t1.returnMoreComplete(t2) == t2);
    Assert.assertTrue(t1.returnMoreComplete(t3) == t1);

    Assert.assertTrue(t2.returnMoreComplete(t2) == t2);
    Assert.assertTrue(t2.returnMoreComplete(t1) == t2);
  }

  @Test
  public void testIsEqual() {
    TimeMark t1 = new TimeMark("0.5");
    TimeMark t2 = new TimeMark("T2", "0.5");
    TimeMark t3 = new TimeMark("0.5");
    TimeMark t4 = new TimeMark("T2", "0.5");
    TimeMark t5 = new TimeMark("T5", "0.5");
    TimeMark t6 = new TimeMark("T2", "0.4");
    TimeMark t7 = new TimeMark("0.6");

    Assert.assertTrue(! t1.isEqual(null));
    Assert.assertTrue(t1.isEqual(t1));
    Assert.assertTrue(t1.isEqual(t2));
    Assert.assertTrue(t1.isEqual(t3));
    Assert.assertTrue(t2.isEqual(t4));
    Assert.assertTrue(t4.isEqual(t2));
    Assert.assertTrue(! t4.isEqual(t5));
    Assert.assertTrue(! t4.isEqual(t6));
    Assert.assertTrue(! t7.isEqual(t1));
  }

  @Test
  public void testIsGreater () {
    TimeMark t1 = new TimeMark("0.5");
    TimeMark t2 = new TimeMark("T2", "0.5");
    TimeMark t3 = new TimeMark("T3", "0.4");
    TimeMark t4 = new TimeMark("0.4");

    Assert.assertTrue(! t1.isGreater(null));
    Assert.assertTrue(! t1.isGreater(t1));
    Assert.assertTrue(! t1.isGreater(t2));
    Assert.assertTrue(t1.isGreater(t3));
    Assert.assertTrue(t1.isGreater(t4));
    Assert.assertTrue(! t2.isGreater(t1));
    Assert.assertTrue(! t2.isGreater(t2));
    Assert.assertTrue(t2.isGreater(t3));
    Assert.assertTrue(t2.isGreater(t4));
    Assert.assertTrue(! t3.isGreater(t1));
    Assert.assertTrue(! t3.isGreater(t2));
    Assert.assertTrue(! t4.isGreater(t1));
    Assert.assertTrue(! t4.isGreater(t2));
  }

  @Test
  public void testIsSmaller () {
    TimeMark t1 = new TimeMark("0.5");
    TimeMark t2 = new TimeMark("T2", "0.5");
    TimeMark t3 = new TimeMark("T3", "0.4");
    TimeMark t4 = new TimeMark("0.4");

    Assert.assertTrue(! t1.isSmaller(null));
    Assert.assertTrue(! t1.isSmaller(t1));
    Assert.assertTrue(! t1.isSmaller(t2));
    Assert.assertTrue(! t1.isSmaller(t3));
    Assert.assertTrue(! t1.isSmaller(t4));
    Assert.assertTrue(! t2.isSmaller(t1));
    Assert.assertTrue(! t2.isSmaller(t2));
    Assert.assertTrue(! t2.isSmaller(t3));
    Assert.assertTrue(! t2.isSmaller(t4));
    Assert.assertTrue(t3.isSmaller(t1));
    Assert.assertTrue(t3.isSmaller(t2));
    Assert.assertTrue(t4.isSmaller(t1));
    Assert.assertTrue(t4.isSmaller(t2));
  }

  @Test
  public void testIsGreaterOrEqual () {
    TimeMark t1 = new TimeMark("0.5");
    TimeMark t2 = new TimeMark("T2", "0.5");
    TimeMark t3 = new TimeMark("T3", "0.4");
    TimeMark t4 = new TimeMark("0.4");

    Assert.assertTrue(! t1.isGreaterOrEqual(null));
    Assert.assertTrue(t1.isGreaterOrEqual(t1));
    Assert.assertTrue(t1.isGreaterOrEqual(t2));
    Assert.assertTrue(t1.isGreaterOrEqual(t3));
    Assert.assertTrue(t1.isGreaterOrEqual(t4));
    Assert.assertTrue(t2.isGreaterOrEqual(t1));
    Assert.assertTrue(t2.isGreaterOrEqual(t2));
    Assert.assertTrue(t2.isGreaterOrEqual(t3));
    Assert.assertTrue(t2.isGreaterOrEqual(t4));
    Assert.assertTrue(! t3.isGreaterOrEqual(t1));
    Assert.assertTrue(! t3.isGreaterOrEqual(t2));
    Assert.assertTrue(! t4.isGreaterOrEqual(t1));
    Assert.assertTrue(! t4.isGreaterOrEqual(t2));
  }

  @Test
  public void testIsSmallerOrEqual () {
    TimeMark t1 = new TimeMark("0.5");
    TimeMark t2 = new TimeMark("T2", "0.5");
    TimeMark t3 = new TimeMark("T3", "0.4");
    TimeMark t4 = new TimeMark("0.4");

    Assert.assertTrue(! t1.isSmallerOrEqual(null));
    Assert.assertTrue(t1.isSmallerOrEqual(t1));
    Assert.assertTrue(t1.isSmallerOrEqual(t2));
    Assert.assertTrue(! t1.isSmallerOrEqual(t3));
    Assert.assertTrue(! t1.isSmallerOrEqual(t4));
    Assert.assertTrue(t2.isSmallerOrEqual(t1));
    Assert.assertTrue(t2.isSmallerOrEqual(t2));
    Assert.assertTrue(! t2.isSmallerOrEqual(t3));
    Assert.assertTrue(! t2.isSmallerOrEqual(t4));
    Assert.assertTrue(t3.isSmallerOrEqual(t1));
    Assert.assertTrue(t3.isSmallerOrEqual(t2));
    Assert.assertTrue(t4.isSmallerOrEqual(t1));
    Assert.assertTrue(t4.isSmallerOrEqual(t2));
  }

  @Test
  public void testCompareTo () {
    TimeMark t1 = new TimeMark("0.5");
    TimeMark t2 = new TimeMark("T2", "0.5");
    TimeMark t3 = new TimeMark("T3", "0.4");
    TimeMark t4 = new TimeMark("0.4");

    Assert.assertTrue(t1.compareTo(t1) == 0);
    Assert.assertTrue(t1.compareTo(t2) == 0);
    Assert.assertTrue(t1.compareTo(t3) > 0);
    Assert.assertTrue(t1.compareTo(t4) > 0);
    Assert.assertTrue(t2.compareTo(t2) == 0);
    Assert.assertTrue(t2.compareTo(t1) == 0);
    Assert.assertTrue(t2.compareTo(t3) > 0);
    Assert.assertTrue(t2.compareTo(t4) > 0);
    Assert.assertTrue(t3.compareTo(t1) < 0);
    Assert.assertTrue(t3.compareTo(t2) < 0);
    Assert.assertTrue(t4.compareTo(t1) < 0);
    Assert.assertTrue(t4.compareTo(t2) < 0);

    exception.expect(NullPointerException.class);
    t1.compareTo(null);

  }
}
