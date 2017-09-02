import kctotei.elements.TimeMark;
import kctotei.elements.TimeMarkSet;
import org.junit.Assert;
import org.junit.Test;

public class TimeMarkSetTest {

  @Test
  public void testAddAndReturn () {

    TimeMarkSet tSet = new TimeMarkSet();

    TimeMark t1 = new TimeMark("0.3");
    TimeMark t2 = new TimeMark("T2", "0.3");
    TimeMark t3 = new TimeMark("0.4");
    TimeMark t4 = new TimeMark("T4", "0.4");

    Assert.assertTrue(tSet.addAndReturn(null) == null);

    Assert.assertTrue(tSet.getList().size() == 0);

    Assert.assertTrue(tSet.addAndReturn(t2) == t2);
    Assert.assertTrue(tSet.getList().size() == 1);
    Assert.assertTrue(tSet.getList().get(0) == t2);

    Assert.assertTrue(tSet.addAndReturn(t1) == t2);
    Assert.assertTrue(tSet.getList().size() == 1);
    Assert.assertTrue(tSet.getList().get(0) == t2);

    Assert.assertTrue(tSet.addAndReturn(t3) == t3);
    Assert.assertTrue(tSet.getList().size() == 2);
    Assert.assertTrue(tSet.getList().get(0) == t2);
    Assert.assertTrue(tSet.getList().get(1) == t3);

    Assert.assertTrue(tSet.addAndReturn(t4) == t4);
    Assert.assertTrue(tSet.getList().size() == 2);
    Assert.assertTrue(tSet.getList().get(0) == t2);
    Assert.assertTrue(tSet.getList().get(1) == t4);

    Assert.assertTrue(tSet.addAndReturn(t3) == t4);
    Assert.assertTrue(tSet.getList().size() == 2);
    Assert.assertTrue(tSet.getList().get(0) == t2);
    Assert.assertTrue(tSet.getList().get(1) == t4);

  }
}
