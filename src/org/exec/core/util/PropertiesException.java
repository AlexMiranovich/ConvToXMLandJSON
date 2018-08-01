package org.exec.core.util;

import org.exec.core.exceptions.ExecRuntimeException;


public final class PropertiesException extends ExecRuntimeException {
  private static final long serialVersionUID = 4446761026170253291L;
  public PropertiesException() {}
  public PropertiesException(String message)
  {
    super(message);
  }
  public PropertiesException(Throwable cause)
  {
    super(cause);
  }
  public PropertiesException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
