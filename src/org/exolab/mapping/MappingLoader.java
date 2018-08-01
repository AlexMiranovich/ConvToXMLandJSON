package org.exolab.mapping;

import java.util.Iterator;
import java.util.List;
import org.exec.mapping.BindingType;

public interface MappingLoader {
   BindingType getBindingType();
   String getSourceType();
   void clear();
   void setClassLoader(ClassLoader paramClassLoader);
   ClassLoader getClassLoader();
   ClassDescriptor getDescriptor(String paramString);
   Iterator<ClassDescriptor> descriptorIterator();
   List<ClassDescriptor> getDescriptors();
}
