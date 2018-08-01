package org.exolab.mapping;

import org.exec.core.nature.PropertyHolder;

public interface ClassDescriptor
  extends PropertyHolder {
  Class getJavaClass();
  FieldDescriptor[] getFields();
  ClassDescriptor getExtends();
  FieldDescriptor getIdentity();
}
