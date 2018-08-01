package org.exolab.xml;

public class ExecException extends Exception {
  private static final long serialVersionUID = -1648679783713336948L;
  private int _errorCode = -1;
  private String _message = null;
  public ExecException(Throwable exception) {}
  public ExecException(String message) {
    super(message);
    this._message = message;
  }
  public ExecException(int errorCode)
  {
    this._errorCode = errorCode;
  }
  public ExecException(String message, int errorCode) {
    super(message);
    this._message = message;
    this._errorCode = errorCode;
  }

  public ExecException(String message, Throwable exception) {
  }

  public ExecException() {
  }

  public int getErrorCode()
  {
    return this._errorCode;
  }
  public String getMessage() {
    if (this._message == null) {
      return "";
    }
    return this._message;
  }
  public void setErrorCode(int errorCode)
  {
    this._errorCode = errorCode;
  }
  public void setMessage(String message)
  {
    this._message = message;
  }
}
