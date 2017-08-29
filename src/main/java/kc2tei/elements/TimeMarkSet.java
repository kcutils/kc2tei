package kc2tei.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * A TimeMarkSet is a set of TimeMark objects.
 * As a set it only contains elements which are not identical, here: equal.
 *
 * @see TimeMark
 */

public class TimeMarkSet {

  private List<TimeMark> list;

  TimeMarkSet () {
    this.setList(new ArrayList<>());
  }

  public void setList (List<TimeMark> list) {
    this.list = list;
  }

  public List<TimeMark> getList () {
    return list;
  }

  /**
   * Add a TimeMark object to the set and return the object that the set contains.
   * Avoids "duplicate" elements in the set.
   *
   * @param t the TimeMark object to add
   * @return the TimeMark object that is equal or identical to the TimeMark object that should be added and which is part of the set
   */
  TimeMark addAndReturn (TimeMark t) {
    TimeMark rval = null;

    // do not add null objects to the set
    if (t != null) {

      // check if the TimeMark is already in list in some way
      for (TimeMark tm : this.getList()) {
        if (t.equals(tm)) {

          // get the more complete TimeMark
          rval = tm.returnMoreComplete(t);

          // remove the old TimeMark
          this.getList().remove(tm);

          break;
        }
      }

      if (rval == null) {
        rval = t;
      }

      // add TimeMark to list
      this.getList().add(rval);
    }

    return rval;
  }

  public String toString () {
    String rval = "";

    for (TimeMark tm : this.getList()) {
      rval = rval + tm.toString() + "\n";
    }

    return rval;
  }
}