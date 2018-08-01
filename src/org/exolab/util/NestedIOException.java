package org.exolab.util;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class NestedIOException extends IOException {
  private static final long serialVersionUID = -4698274786487914369L;
  private Exception _exception = null;
  private boolean _localTrace = false;
  public NestedIOException() {}
  public NestedIOException(String message)
  {
    super(message);
  }
  public NestedIOException(Exception exception) {
    super(exception.getMessage());
    this._exception = exception;
  }
  public NestedIOException(String message, Exception exception) {
    super(message);
    this._exception = exception;
  }
  public Exception getException()
  {
    return this._exception;
  }
  public void setLocalStackTraceOnly(boolean localTrace)
  {
    this._localTrace = localTrace;
  }
  public String toString() {
    StringBuffer sb = new StringBuffer("NestedIOException: ");
    if (getMessage() != null) {
      sb.append(getMessage());
    }
    if ((this._exception != null) && (this._exception.getMessage() != null))
    {
      sb.append(" { nested error: ");
      sb.append(this._exception.getMessage());
      sb.append('}');
    }
    return sb.toString();
  }
  public void printStackTrace() {
    if ((this._exception == null) || (this._localTrace)) {
      super.printStackTrace();
    } else {
      this._exception.printStackTrace();
    }
  }
  public void printStackTrace(PrintWriter printer) {
    if ((this._localTrace) || (this._exception == null)) {
      super.printStackTrace(printer);
    } else {
      this._exception.printStackTrace(printer);
    }
  }
  public void printStackTrace(PrintStream printer) {
    if ((this._localTrace) || (this._exception == null)) {
      super.printStackTrace(printer);
    } else {
      this._exception.printStackTrace(printer);
    }
  }
}
