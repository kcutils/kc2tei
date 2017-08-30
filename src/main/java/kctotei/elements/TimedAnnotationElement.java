package kctotei.elements;

/**
 * A TimedAnnotationElement consists of
 * a TimeMark that marks the beginning,
 * some content and
 * a TimeMark that marks the end.
 *
 * @param <T> type of content
 * @see TimeMark
 */

public abstract class TimedAnnotationElement<T> implements Comparable<TimedAnnotationElement> {
  private TimeMark startTime;
  private T content;
  private TimeMark endTime;

  TimedAnnotationElement (T content) {
    this.setStartTime(null);
    this.setContent(content);
    this.setEndTime(null);
  }

  TimedAnnotationElement (TimeMark startTime, T content) {
    this(content);
    this.setStartTime(startTime);
  }

  public TimeMark getStartTime () {
    return startTime;
  }

  public void setStartTime (TimeMark startTime) {
    this.startTime = startTime;
  }

  public T getContent () {
    return content;
  }

  void setContent (T content) {
    this.content = content;
  }

  public TimeMark getEndTime () {
    return endTime;
  }

  public void setEndTime (TimeMark endTime) {
    this.endTime = endTime;
  }

  public String toString () {
    String rval = "";

    if (this.getStartTime() != null) {
      rval = rval + this.getStartTime().toString();
    }

    if (this.getContent() != null) {
      rval = rval + " " + this.getContent().getClass().toString() + ": " + this.getContent().toString();
    }

    if (this.getEndTime() != null) {
      rval = rval + " " + this.getEndTime().toString();
    }

    return rval;
  }

  /**
   * @param o the TimedAnnotationElement to compare with
   * @return -1 if this TimedAnnotationElement is "smaller" than the specified TimedAnnotationElement,
   *          0 if they are "equal",
   *          1 if this TimedAnnotationElement is "bigger" than the specified one.
   * @throws ClassCastException on casting problems (as defined by interface)
   */
  @Override
  public int compareTo (TimedAnnotationElement o) throws ClassCastException, NullPointerException {

    // TODO: couldn't that be done better?
    // we want a word to appear before all labels in list that are relevant for this word

    if (o == null) {
      throw new NullPointerException();
    }

    // same beginning
    if (this.getStartTime().compareTo(o.getStartTime()) == 0) {
      // same end
      if (this.getEndTime().compareTo(o.getEndTime()) == 0) {
        return 0;
      }
      // smaller end
      if (this.getEndTime().compareTo(o.getEndTime()) < 0) {
        return -1;
      }
      // bigger end
      if (this.getEndTime().compareTo(o.getEndTime()) > 0) {
        return 1;
      }
    }

    // smaller beginning
    if (this.getStartTime().compareTo(o.getStartTime()) < 0) {
      // same end or smaller end or bigger end
      return -1;
    }

    // bigger beginning
    if (this.getStartTime().compareTo(o.getStartTime()) > 0) {
      // same end or smaller end or bigger end
      return 1;
    }

    return 0;
  }
}
