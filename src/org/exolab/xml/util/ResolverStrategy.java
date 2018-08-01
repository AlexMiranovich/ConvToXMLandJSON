package org.exolab.xml.util;

import java.util.Map;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.xml.ResolverException;
import org.exolab.xml.XMLClassDescriptor;

public interface ResolverStrategy {
   String PROPERTY_CLASS_LOADER = "org.exolab.xml.util.ResolverStrategy.ClassLoader";
   String PROPERTY_USE_INTROSPECTION = "org.exolab.xml.util.ResolverStrategy.useIntrospection";
   String PROPERTY_INTROSPECTOR = "org.exolab.xml.util.ResolverStrategy.Introspector";
   String PROPERTY_LOAD_PACKAGE_MAPPINGS = "org.exolab.xml.util.ResolverStrategy.LoadPackageMappings";
   String PROPERTY_MAPPING_LOADER = "org.exolab.xml.util.ResolverStrategy.MappingLoader";
    void setProperty(String paramString, Object paramObject);
    ClassDescriptor resolveClass(ResolverResults paramResolverResults, String paramString)
    throws ResolverException;
    void resolvePackage(ResolverResults paramResolverResults, String paramString)
    throws ResolverException;
    interface ResolverResults {
      void addDescriptor(String paramString, XMLClassDescriptor paramXMLClassDescriptor);
      void addAllDescriptors(Map paramMap);
      XMLClassDescriptor getDescriptor(String paramString);
  }
}
