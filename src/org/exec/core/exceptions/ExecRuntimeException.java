package org.exec.core.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ExecRuntimeException extends RuntimeException {
  private static final long serialVersionUID = 3984585622253325513L;
  private Throwable _cause = null;
  private boolean _initCause = false;
  public ExecRuntimeException() {}
  public ExecRuntimeException(String message) {
    super(message);
  }
  public ExecRuntimeException(Throwable cause) {
    super(cause == null ? null : cause.getMessage());
    this._cause = cause;
    this._initCause = true;
  }
  public ExecRuntimeException(String message, Throwable cause) {
    super(message);
    this._cause = cause;
    this._initCause = true;
  }
  public final Throwable initCause(Throwable cause) {
    if (cause == this) throw new IllegalArgumentException();
    if (this._initCause) throw new IllegalStateException();
    this._cause = cause;
    this._initCause = true;
    return this;
  }
  public final Throwable getCause()
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
  public final void printStackTrace(PrintStream s) {
    super.printStackTrace(s);
    if (this._cause != null) {
      s.print("Caused by: ");
      this._cause.printStackTrace(s);
    }
  }
  public final void printStackTrace(PrintWriter w) {
    super.printStackTrace(w);
    if (this._cause != null) {
      w.print("Caused by: ");
      this._cause.printStackTrace(w);
    }
  }
}
