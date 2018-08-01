package org.exolab.xml;

import org.exec.mapping.BindingType;
import org.exolab.xml.util.XMLClassDescriptorResolverImpl;
import org.exolab.xml.util.resolvers.ExecXMLStrategy;

public class ClassDescriptorResolverFactory {
  public static ClassDescriptorResolver createClassDescriptorResolver(BindingType type) {
    if (type == BindingType.XML) {
      XMLClassDescriptorResolver resolver = new XMLClassDescriptorResolverImpl();
      resolver.setResolverStrategy(new ExecXMLStrategy());
      return resolver;
    }
    return null;
  }
}
