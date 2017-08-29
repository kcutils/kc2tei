package kc2tei.elements;

/**
 * A TimeMark has a name and a time value.
 * It can be compared to other TimeMarks.
 */

public class TimeMark implements Comparable<TimeMark> {

  private String name;
  private Float time;

  private TimeMark () {
    this.setName(null);
    this.setTime((Float) null);
  }

  public TimeMark (String time) {
    this();
    this.setTime(time);
  }

  TimeMark (Float time) {
    this();
    this.setTime(time);
  }

  TimeMark (String name, String time) {
    this(time);
    this.setName(name);
  }

  TimeMark (String name, Float time) {
    this(time);
    this.setName(name);
  }

  String getName () {
    return name;
  }

  public void setName (String name) {
    if (name != null) {
      name = name.replaceAll("\\s", "");
    }
    this.name = name;
  }

  Float getTime () {
    return time;
  }

  // keep time setters private, so that they cannot be modified later
  private void setTime (String time) {
    if (time != null) {
      time = time.replaceAll("\\s", "");
      this.time = Float.parseFloat(time);
    }
  }

  private void setTime (Float time) {
    this.time = time;
  }

  /**
   * Return this TimeMark if it equals specified TimeMark.
   * Return the specified TimeMark only if it equals this TimeMark and has a name while this TimeMark does not have a name.
   * <p>
   * This method can be handy if you want to avoid "duplicate" TimeMark objects in a list.
   *
   * @param t the TimeMark to be compared
   * @return TimeMark which is considered more complete
   */
  public TimeMark returnMoreComplete (TimeMark t) {
    TimeMark rval = this;
    if (t != null && this.equals(t) && this.getName() == null && t.getName() != null) {
      rval = t;
    }
    return rval;
  }

  /**
   * A specified TimeMark is equal to this TimeMark if it has the same time
   * and either the same name or one of both names is null.
   *
   * @param t the TimeMark to be compared
   * @return true if TimeMarks are considered equal
   * false otherwise
   */

  boolean equals (TimeMark t) {
    return t != null && this.getTime().equals(t.getTime()) && (
                        (this.getName() == null || t.getName() == null) ||
                         this.getName().equals(t.getName()));
  }

  boolean isGreater (TimeMark t) {
    return t != null && this.getTime() > t.getTime();
  }

  boolean isSmaller (TimeMark t) {
    return !equals(t) && !isGreater(t);
  }

  boolean isGreaterOrEqual (TimeMark t) {
    return equals(t) || isGreater(t);
  }

  boolean isSmallerOrEqual (TimeMark t) {
    return equals(t) || isSmaller(t);
  }

  @Override
  public int compareTo (TimeMark o) throws NullPointerException, ClassCastException {
    if (o == null) {
      throw (new NullPointerException());
    }
    if (isSmaller(o)) {
      return -1;
    }

    if (isGreater(o)) {
      return 1;
    }

    return 0;
  }

  public String toString () {
    String rval = "";

    if (name != null) {
      rval = rval + name;
    }

    if (time != null) {
      rval = rval + " " + time;
    }

    return rval;
  }

}
