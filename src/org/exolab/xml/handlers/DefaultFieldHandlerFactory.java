package org.exolab.xml.handlers;

import java.sql.Time;
import java.sql.Timestamp;
import org.exolab.mapping.FieldHandlerFactory;
import org.exolab.mapping.GeneralizedFieldHandler;
import org.exolab.mapping.MappingException;

public class DefaultFieldHandlerFactory extends FieldHandlerFactory {
  private static final Class[] SUPPORTED_CLASSES = { Time.class, Timestamp.class };
  public Class[] getSupportedTypes()
  {
    return (Class[])SUPPORTED_CLASSES.clone();
  }
  public boolean isSupportedType(Class type) {
    for (int i = 0; i < SUPPORTED_CLASSES.length; i++) {
      if (SUPPORTED_CLASSES[i].isAssignableFrom(type)) {
        return true;
      }
    }
    return false;
  }
  public GeneralizedFieldHandler createFieldHandler(Class type)
    throws MappingException {
    if (type == null) {
      return null;
    }
    if (Time.class.isAssignableFrom(type)) {
      return new SQLTimeFieldHandler();
    }
    if (Timestamp.class.isAssignableFrom(type)) {
      return new ValueOfFieldHandler(type);
    }
    return null;
  }
}
