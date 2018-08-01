package org.exolab.xml.util.resolvers;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.xml.ResolverException;
import org.exolab.xml.XMLClassDescriptor;
import org.exolab.xml.util.ResolverStrategy;

public class ExecXMLStrategy implements ResolverStrategy {
  private static final Log LOG = LogFactory.getLog(ExecXMLStrategy.class);
  private Map _properties;
  public ExecXMLStrategy()
  {
    this._properties = new HashMap();
  }
  public void setProperty(String key, Object value) {
    if (this._properties == null) {
      this._properties = new HashMap();
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Setting property: " + key + " to value: " + value);
    }
    this._properties.put(key, value);
  }
  public ClassDescriptor resolveClass(ResolverStrategy.ResolverResults resolverResults, String className)
    throws ResolverException {
    if ((className == null) || ("".equals(className))) {
      String message = "Class name to resolve must not be null or empty!";
      LOG.warn(message);
      throw new IllegalArgumentException(message);
    }
    XMLClassDescriptor descriptor = getDescriptor(resolverResults, className);
    return descriptor;
  }
  private XMLClassDescriptor getDescriptor(ResolverStrategy.ResolverResults resolverResults, String className)
    throws ResolverException {
    String packageName = ResolveHelpers.getPackageName(className);
    XMLClassDescriptor descriptor = resolverResults.getDescriptor(className);
    if (descriptor != null) {
      return descriptor;
    }
    resolverResults.addAllDescriptors(new ByMappingLoader().resolve(className, this._properties));
    descriptor = resolverResults.getDescriptor(className);
    if (descriptor != null) {
      return descriptor;
    }
    resolvePackage(resolverResults, packageName);
    descriptor = resolverResults.getDescriptor(className);
    if (descriptor != null) {
      return descriptor;
    }
    resolverResults.addAllDescriptors(new ByDescriptorClass().resolve(className, this._properties));
    descriptor = resolverResults.getDescriptor(className);
    if (descriptor != null) {
      return descriptor;
    }
    resolverResults.addAllDescriptors(new ByIntrospection().resolve(className, this._properties));
    descriptor = resolverResults.getDescriptor(className);
    if (descriptor != null) {
      return descriptor;
    }
    resolverResults.addDescriptor(className, null);
    return null;
  }
  public void resolvePackage(ResolverStrategy.ResolverResults resolverResults, String packageName)
    throws ResolverException {
    resolverResults.addAllDescriptors(new ByCDR().resolve(packageName, this._properties));
    resolverResults.addAllDescriptors(new ByPackageMapping().resolve(packageName, this._properties));
  }
}
