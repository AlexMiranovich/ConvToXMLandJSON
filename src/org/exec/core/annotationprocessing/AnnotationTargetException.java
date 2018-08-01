package org.exec.core.annotationprocessing;

public class AnnotationTargetException extends Exception {
  public AnnotationTargetException() {}
  public AnnotationTargetException(String message, Throwable cause)
  {
    super(message, cause);
  }
  public AnnotationTargetException(String message)
  {
    super(message);
  }
  public AnnotationTargetException(Throwable cause)
  {
    super(cause);
  }
}
