package org.exolab.mapping;

public abstract class FieldHandlerFactory {
  public abstract Class[] getSupportedTypes();
  public abstract boolean isSupportedType(Class paramClass);
  public abstract GeneralizedFieldHandler createFieldHandler(Class paramClass) throws MappingException;
}
