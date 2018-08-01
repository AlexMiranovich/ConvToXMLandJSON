package org.exolab.mapping;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.exec.core.util.Messages;

public class ValidityException extends Exception {
  private static final long serialVersionUID = 6928906878046428690L;
  private Exception _except;
  public ValidityException(String message)
  {
    super(Messages.message(message));
  }
  public ValidityException(String message, Object arg1)
  {
    super(Messages.format(message, arg1));
  }
  public ValidityException(String message, Object arg1, Object arg2)
  {
    super(Messages.format(message, arg1, arg2));
  }
  public ValidityException(String message, Object arg1, Object arg2, Object arg3) {
    super(Messages.format(message, arg1, arg2, arg3));
  }
  public ValidityException(Exception except) {
    super(Messages.format("mapping.nested", except.toString()));
    this._except = except;
  }
  public Exception getException()
  {
    return this._except;
  }
  public void printStackTrace() {
    if (this._except == null) {
      super.printStackTrace();
    } else {
      this._except.printStackTrace();
    }
  }
  public void printStackTrace(PrintStream print) {
    if (this._except == null) {
      super.printStackTrace(print);
    } else {
      this._except.printStackTrace(print);
    }
  }
  public void printStackTrace(PrintWriter print) {
    if (this._except == null) {
      super.printStackTrace(print);
    } else {
      this._except.printStackTrace(print);
    }
  }
}
