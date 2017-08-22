import java.util.ArrayList;
import java.util.List;

public class TimeMarkSet {

  private List<TimeMark> list = new ArrayList<>();

  public TimeMark addAndReturn (TimeMark t) {

    TimeMark rval = null;

    Boolean inList = false;

    if (t != null) {

      // check if t is already in list in some way
      for (TimeMark tm : list) {
        if (t.equals(tm)) {
          rval = tm.returnMoreComplete(t);
          inList = true;
          break;
        }
      }

      // add t to list if not in list
      if (! inList) {
        list.add(t);
        rval = t;
      }

    }

    return rval;
  }

  public String toString () {
    String rval = "";
    for (TimeMark tm : list) {
      rval = rval + tm.toString() + "\n";
    }
    return rval;
  }

  public List<TimeMark> getList () {
    return list;
  }
}
