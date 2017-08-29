public class TimeMark implements Comparable<TimeMark> {

  private String name = null;
  private Float time = null;

  public TimeMark(String t) {
    this.setTime(t);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name != null) {
      name = name.replaceAll("\\s", "");
    }
    this.name = name;
  }

  public Float getTime() {
    return time;
  }

  public void setTime(String time) {
    if (time != null) {
      time = time.replaceAll("\\s", "");
      this.time = Float.parseFloat(time);
    }
  }

  // each object has at least a time
  // this object is considered more complete than any other that is equal
  // expect if the object compared with has a name
  public TimeMark returnMoreComplete (TimeMark t) {
    TimeMark rval = null;
    if (this.equals(t)) {
      rval = this;
      if (this.getName() == null && t.getName() != null) {
        rval = t;
      }
    }
    return rval;
  }

  /**
   *
   * @param  t the TimeMark to be compared
   * @return true if this TimeMark same name and time as specified TimeMark,
   *         false otherwise.
   */

  public boolean equals (TimeMark t) {
    Boolean rval = false;
    if (t != null) {
      rval = (this.getName() != null && t.getName() != null && this.getName().equals(t.getName()) && this.getTime().equals(t.getTime())) || ((this.getName() == null || t.getName() == null) && this.getTime().equals(t.getTime()));
    }
    return rval;
  }

  /**
   *
   * @param  t the TimeMark to be compared.
   * @return true if this TimeMark has greater time than specified TimeMark,
   *         false otherwise.
   */

  public boolean isGreater (TimeMark t) {
    Boolean rval = false;
    if (t != null) {
      rval = this.getTime() > t.getTime();
    }
    return rval;
  }

  public boolean isSmaller (TimeMark t) {
    return (! equals(t)) && (! isGreater(t));
  }

  public boolean isGreaterOrEqual (TimeMark t) {
    return equals(t) || isGreater(t);
  }

  public boolean isSmallerOrEqual (TimeMark t) {
    return equals(t) || isSmaller(t);
  }

  @Override
  public int compareTo (TimeMark o) throws NullPointerException, ClassCastException {
    if (o == null) {
      throw(new NullPointerException());
    }
    if (isSmaller(o)) {
      return -1;
    }

    if (isGreater(o)) {
      return 1;
    }

    return 0;
  }

  public String toString() {
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
