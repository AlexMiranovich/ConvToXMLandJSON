package org.exolab.mapping;

import java.util.Properties;

public interface ConfigurableFieldHandler extends FieldHandler {
  void setConfiguration(Properties paramProperties) throws ValidityException;
}
