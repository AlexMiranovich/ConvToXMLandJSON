package org.exolab.xml;

import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.MappingLoader;

public interface ClassDescriptorResolver {
   MappingLoader getMappingLoader();
   ClassDescriptor resolve(Class paramClass) throws ResolverException;
   void setMappingLoader(MappingLoader paramMappingLoader);
}
