package org.exec.xml;

import org.exec.mapping.BindingType;
import org.exolab.xml.ClassDescriptorResolverFactory;
import org.exolab.xml.Introspector;
import org.exolab.xml.XMLClassDescriptorResolver;
import org.exolab.xml.util.ResolverStrategy;
import org.exolab.xml.util.resolvers.ExecXMLStrategy;

public class BackwardCompatibilityContext extends AbstractInternalContext implements InternalContext {
  public BackwardCompatibilityContext() {
    this.setClassLoader(this.getClass().getClassLoader());
    XMLClassDescriptorResolver cdr =
            (XMLClassDescriptorResolver)ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);
    cdr.setInternalContext(this);
    this.setXMLClassDescriptorResolver(cdr);
    Introspector introspector = new Introspector();
    introspector.setInternalContext(this);
    this.setIntrospector(introspector);
    cdr.setIntrospector(introspector);
    ResolverStrategy resolverStrategy = new ExecXMLStrategy();
    this.setResolverStrategy(resolverStrategy);
    cdr.setResolverStrategy(resolverStrategy);
  }
}
