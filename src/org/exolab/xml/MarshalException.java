package org.exolab.xml;

public class MarshalException extends XMLException {
  private static final long serialVersionUID = -1648679783713336948L;
  public static final String BASE_CLASS_OR_VOID_ERR = "The marshaller cannot marshal/unmarshal types of Void.class, Class.class or Object.class";
  public static final String NON_SERIALIZABLE_ERR = "The marshaller cannot unmarshal non primitive types that do not implement java.io.Serializable";
  public MarshalException() {}
  public MarshalException(String message)
  {
    super(message);
  }
  public MarshalException(String message, int errorCode)
  {
    super(message, errorCode);
  }
  public MarshalException(Throwable exception)
  {
    super(exception);
  }
  public MarshalException(String message, Throwable exception)
  {
    super(message, exception);
  }
  public MarshalException(String message, Throwable exception, int errorCode)
  {
    super(message, exception, errorCode);
  }
}
