package org.exolab.mapping;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.exec.core.util.Messages;

public class MappingRuntimeException extends IllegalStateException {
  private static final long serialVersionUID = 238861866150334375L;
  private Throwable _exception;
  public MappingRuntimeException(String message)
  {
    super(Messages.message(message));
  }
  public MappingRuntimeException(String message, Object[] args)
  {
    super(Messages.format(message, args));
  }
  public MappingRuntimeException(Throwable exception) {
    super(Messages.format("mapping.nested", exception.toString()));
    this._exception = exception;
  }
  public MappingRuntimeException(Throwable exception, String message) {
    super(Messages.format("mapping.nested", message));
    this._exception = exception;
  }
  public Throwable getException()
  {
    return this._exception;
  }
  public void printStackTrace() {
    if (this._exception == null) {
      super.printStackTrace();
    } else {
      this._exception.printStackTrace();
    }
  }
  public void printStackTrace(PrintStream print) {
    if (this._exception == null) {
      super.printStackTrace(print);
    } else {
      this._exception.printStackTrace(print);
    }
  }
  public void printStackTrace(PrintWriter print) {
    if (this._exception == null) {
      super.printStackTrace(print);
    } else {
      this._exception.printStackTrace(print);
    }
  }
}
