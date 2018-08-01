package org.exolab.exceptions;

public class ExecException extends Exception {
  private static final long serialVersionUID = -5963804406955523505L;
  public ExecException() {}
  public ExecException(String message)
  {
    super(message);
  }
  public ExecException(String message, Throwable cause)
  {
    super(message, cause);
  }
  public ExecException(Throwable cause) {
    super(cause);
  }
}
