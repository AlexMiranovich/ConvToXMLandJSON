package org.exolab.mapping;

import org.exec.core.nature.PropertyHolder;

public  interface FieldDescriptor extends PropertyHolder {
   void setContainingClassDescriptor(ClassDescriptor paramClassDescriptor);
   ClassDescriptor getContainingClassDescriptor();
   String getFieldName();
   Class getFieldType();
   ClassDescriptor getClassDescriptor();
   FieldHandler getHandler();
   boolean isTransient();
   boolean isImmutable();
   boolean isRequired();
   boolean isMultivalued();
}
