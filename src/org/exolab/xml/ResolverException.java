package org.exolab.xml;

public class ResolverException extends XMLException {
  private static final long serialVersionUID = -8800218775708296399L;
  public ResolverException() {}
  public ResolverException(String message) { super(message); }
  public ResolverException(Throwable exception)
  {
    super(exception);
  }
  public ResolverException(String message, int errorCode)
  {
    super(message, errorCode);
  }
  public ResolverException(String message, Throwable exception)
  {
    super(message, exception);
  }
  public ResolverException(String message, Throwable exception, int errorCode)
  {
    super(message, exception, errorCode);
  }
}
