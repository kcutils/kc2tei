
public abstract class TimedAnnotationElement<T> implements Comparable<TimedAnnotationElement> {
  private TimeMark startTime = null;
  private T content = null;
  private TimeMark endTime = null;

  public TimedAnnotationElement (T content) {
    this.setContent(content);
  }

  public TimedAnnotationElement (TimeMark startTime, T content) {
    this.setStartTime(startTime);
    this.setContent(content);
  }

  public TimeMark getStartTime() {
    return startTime;
  }

  public void setStartTime(TimeMark startTime) {
    this.startTime = startTime;
  }

  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }

  public TimeMark getEndTime() {
    return endTime;
  }

  public void setEndTime(TimeMark endTime) {
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

  @Override
  public int compareTo (TimedAnnotationElement o) throws NullPointerException, ClassCastException {

    if (o == null) {
      throw new NullPointerException();
    }

    // TODO: couldn't that be done better?
    if (this.getClass() != Word.class) {
      if (this.getStartTime().compareTo(o.getStartTime()) < 0 && this.getEndTime().compareTo(o.getEndTime()) <= 0) {
        return -1;
      }
      if (this.getStartTime().compareTo(o.getStartTime()) > 0 && this.getEndTime().compareTo(o.getEndTime()) >= 0) {
        return 1;
      }
      if (this.getStartTime().compareTo(o.getStartTime()) == 0 && this.getEndTime().compareTo(o.getEndTime()) < 0) {
        return -1;
      }
      if (this.getStartTime().compareTo(o.getStartTime()) == 0 && this.getEndTime().compareTo(o.getEndTime()) > 0) {
        return 1;
      }
      // this.getStartTime().compareTo(o.getStartTime()) == 0 && this.getEndTime().compareTo(o.getEndTime()) == 0
      return 0;
    } else {
      // we want a word to appear before all labels in list that are relevant for this word
      if (this.getStartTime().compareTo(o.getStartTime()) == 0 && this.getStartTime().compareTo(o.getEndTime()) <= 0 ) {
        return -1;
      }
      return this.getStartTime().compareTo(o.getStartTime());
    }
  }
}
