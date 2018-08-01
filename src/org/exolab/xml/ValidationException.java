package org.exolab.xml;

import org.exolab.xml.location.Location;

public class ValidationException extends XMLException {
  private static final long serialVersionUID = 2220902174700444631L;
  private Location _location = null;
  private ValidationException _next = null;
  public ValidationException() {}
  public ValidationException(String message)
  {
    super(message);
  }
  public ValidationException(String message, int errorCode)
  {
    super(message, errorCode);
  }
  public ValidationException(Throwable exception)
  {
    super(exception);
  }
  public ValidationException(String message, Throwable exception)
  {
    super(message, exception);
  }
  public ValidationException(String message, Exception exception, int errorCode) {
    super(message, exception, errorCode);
  }
  public Location getLocation()
  {
    return this._location;
  }
  public ValidationException getNext()
  {
    return this._next;
  }
  public void setLocation(Location location)
  {
    this._location = location;
  }
  protected boolean remove(ValidationException exception) {
    if (exception == null) {
      return false;
    }
    ValidationException previous = this;
    for (ValidationException current = this._next; current != null; current = current._next) {
      if (current == exception) {
        previous._next = current._next;
        current._next = null;
        return true;
      }
      previous = current;
    }
    return false;
  }
  protected void setLast(ValidationException exception) {
    if (exception == null) {
      return;
    }
    ValidationException current = this;
    while (current._next != null) {
      current = current._next;
    }
    current._next = exception;
  }
  public void setNext(ValidationException exception)
  {
    this._next = exception;
  }
  public String toString() {
    StringBuffer sb = new StringBuffer();
    if (getNext() != null) {
      int count = 1;
      for (ValidationException vx = this; vx != null; vx = vx.getNext()) {
        if (count > 1) {
          sb.append("\n\n");
        }
        sb.append(count);
        sb.append(". ");
        dumpOneException(sb, vx);
        count++;
      }
    }
    else {
      dumpOneException(sb, this);
    }
    return sb.toString();
  }
  private void dumpOneException(StringBuffer sb, ValidationException vx) {
    sb.append("ValidationException: ");
    String message = vx.getMessage();
    if (message != null) {
      sb.append(message);
    }
    Location location = vx.getLocation();
    if (location != null) {
      sb.append(";\n   - location of error: ");
      sb.append(location.toString());
    }
    Throwable t = vx.getCause();
    if (t != null) {
      sb.append("\n");
      sb.append(t.getMessage());
    }
  }
}
