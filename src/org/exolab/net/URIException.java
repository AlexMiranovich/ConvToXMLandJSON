package org.exolab.net;

import java.io.PrintStream;
import java.io.PrintWriter;

public final class URIException extends Exception {
  private static final long serialVersionUID = 4230299234562430190L;
  private Exception _exception = null;
  public URIException(String message)
  {
    super(message);
  }
  public URIException(String message, Exception exception) {
    super(message);
    if ((exception instanceof URIException)) {
      this._exception = ((URIException)exception)._exception;
    } else {
      this._exception = exception;
    }
  }
  public URIException(Exception exception)
  {
    this(exception.getMessage(), exception);
  }
  public Exception getException()
  {
    return this._exception;
  }
  public void printStackTrace() {
    if (this._exception != null) {
      this._exception.printStackTrace();
    } else {
      super.printStackTrace();
    }
  }
  public void printStackTrace(PrintStream s) {
    if (this._exception != null) {
      this._exception.printStackTrace(s);
    } else {
      super.printStackTrace(s);
    }
  }
  public void printStackTrace(PrintWriter s) {
    if (this._exception != null) {
      this._exception.printStackTrace(s);
    } else {
      super.printStackTrace(s);
    }
  }
}
