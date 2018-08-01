package org.exec.mapping;

import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingLoader;

public interface MappingLoaderFactory {
   String getName();
   MappingLoader getMappingLoader() throws MappingException, MappingException;
   String getSourceType();
   BindingType getBindingType();
}
