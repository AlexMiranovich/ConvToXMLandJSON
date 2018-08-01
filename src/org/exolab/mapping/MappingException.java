package org.exolab.mapping;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.exec.core.util.Messages;

public class MappingException extends Exception {
  private static final long serialVersionUID = 841446747748696044L;
  private Exception _except;
  public MappingException(String message)
  {
    super(Messages.message(message));
  }
  public MappingException(String message, Exception e) {
    super(Messages.message(message));
    setException(e);
  }
  public MappingException(String message, Object arg1)
  {
    super(Messages.format(message, arg1));
  }
  public MappingException(String message, Object arg1, Object arg2)
  {
    super(Messages.format(message, arg1, arg2));
  }
  public MappingException(String message, Object arg1, Object arg2, Object arg3) {
    super(Messages.format(message, arg1, arg2, arg3));
  }
  public MappingException(Exception except) {
    super(Messages.format("mapping.nested", except.toString()));
    this._except = except;
  }
  public Exception getException()
  {
    return this._except;
  }
  private void setException(Exception e)
  {
    this._except = e;
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
