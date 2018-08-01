package org.exolab.xml.util.resolvers;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.xml.Introspector;
import org.exolab.xml.MarshalException;
import org.exolab.xml.ResolverException;
import org.exolab.xml.XMLClassDescriptor;

public class ByIntrospection extends AbstractResolverClassCommand {
  private static final Log LOG = LogFactory.getLog(ByIntrospection.class);
  protected Map internalResolve(String className, ClassLoader classLoader, Map properties)
    throws ResolverException {
    Boolean useIntrospector = (Boolean)properties.get("org.exolabor.xml.util.ResolverStrategy.useIntrospection");
    HashMap results = new HashMap();
    if (classLoader == null) {
      LOG.debug("No class loader available.");
      return results;
    }
    if ((useIntrospector != null) && (!useIntrospector.booleanValue())) {
      LOG.debug("Introspection is disabled!");
      return results;
    }
    Introspector introspector = (Introspector)properties.get("org.exolab.xml.util.ResolverStrategy.Introspector");
    if (introspector == null) {
      String message = "No Introspector defined in properties!";
      LOG.warn(message);
      throw new IllegalStateException(message);
    }
    Class clazz = ResolveHelpers.loadClass(classLoader, className);
    if (clazz != null) {
      try {
        XMLClassDescriptor descriptor = introspector.generateClassDescriptor(clazz);
        if (descriptor != null) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Found descriptor: " + descriptor);
          }
          results.put(clazz.getName(), descriptor);
        }
      } catch (MarshalException e) {
        String message = "Failed to generate class descriptor for: " + clazz + " with exception: " + e;
        
        LOG.warn(message);
        throw new ResolverException(message);
      }
    }
    return results;
  }
}
