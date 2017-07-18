
public abstract class TimedAnnotationElement<T> {
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
}
