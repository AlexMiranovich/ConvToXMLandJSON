package org.exolab.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public final class ExecIllegalStateException extends IllegalStateException {
  private static final long serialVersionUID = 2351884252990815335L;
  private Throwable _cause;
  public ExecIllegalStateException() {}
  public ExecIllegalStateException(String message)
  {
    super(message);
  }
  public ExecIllegalStateException(Throwable cause)
  {
    this._cause = cause;
  }
  public ExecIllegalStateException(String message, Throwable cause) {
    super(message);
    this._cause = cause;
  }
  public Throwable getCause()
  {
    return this._cause;
  }
  public void printStackTrace() {
    super.printStackTrace();
    
    if (this._cause != null) {
      System.err.print("Caused by: ");
      this._cause.printStackTrace();
    }
  }
  public void printStackTrace(PrintStream s) {
    super.printStackTrace(s);
    if (this._cause != null) {
      s.print("Caused by: ");
      this._cause.printStackTrace(s);
    }
  }
  public void printStackTrace(PrintWriter w) {
    super.printStackTrace(w);
    if (this._cause != null) {
      w.print("Caused by: ");
      this._cause.printStackTrace(w);
    }
  }
}
