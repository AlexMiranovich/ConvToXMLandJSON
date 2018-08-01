package org.exec.core.exception;

public class IllegalClassDescriptorInitialization extends RuntimeException {
  private static final long serialVersionUID = 1L;
  public IllegalClassDescriptorInitialization() {}
  public IllegalClassDescriptorInitialization(String message)
  {
    super(message);
  }
  public IllegalClassDescriptorInitialization(Throwable cause)
  {
    super(cause);
  }
  public IllegalClassDescriptorInitialization(String message, Throwable cause)
  {
    super(message, cause);
  }
}
