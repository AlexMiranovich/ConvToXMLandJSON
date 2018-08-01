package org.exolab.xml;

import org.exolab.xml.location.Location;
import org.exolab.xml.location.FileLocation;

public class XMLException extends ExecException {
  private static final long serialVersionUID = 7512918645754995146L;
  private Location _location = null;
  private int errorCode = -1;
  public XMLException() {}
  public XMLException(String message)
  {
    super(message);
  }
  public XMLException(Throwable exception)
  {
    super(exception);
  }
  public XMLException(String message, int errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
  public XMLException(String message, Throwable exception)
  {
    super(message, exception);
  }
  public XMLException(String message, Throwable exception, int errorCode) {
    super(message, exception);
    this.errorCode = errorCode;
  }
  public void setLocation(FileLocation location)
  {
    this._location = location;
  }
  public String toString() {
    StringBuffer buff = new StringBuffer();
    buff.append(getClass().getName());
    String msg = getMessage();
    if (msg != null) {
      buff.append(": ").append(msg);
    }
    if (this._location != null) {
      buff.append("{").append(this._location).append("}");
    }
    return buff.toString();
  }
  public int getErrorCode()
  {
    return this.errorCode;
  }
  public void setErrorCode(int errorCode)
  {
    this.errorCode = errorCode;
  }
}
