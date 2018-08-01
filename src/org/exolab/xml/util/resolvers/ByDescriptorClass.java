package org.exolab.xml.util.resolvers;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.xml.ResolverException;

public class ByDescriptorClass extends AbstractResolverClassCommand {
  private static final Log LOG = LogFactory.getLog(ByDescriptorClass.class);
  protected Map internalResolve(String className, ClassLoader classLoader, Map properties)
    throws ResolverException {
    HashMap results = new HashMap();
    if (classLoader == null) {
      LOG.debug("No class loader available.");
      return results;
    }
    StringBuffer descriptorClassName = new StringBuffer(className);
    descriptorClassName.append("Descriptor");
    Class descriptorClass = ResolveHelpers.loadClass(classLoader, descriptorClassName.toString());
    if (descriptorClass == null) {
      int offset = descriptorClassName.lastIndexOf(".");
      if (offset != -1) {
        descriptorClassName.insert(offset, ".");
        descriptorClassName.insert(offset + 1, "descriptors");
        descriptorClass = ResolveHelpers.loadClass(classLoader, descriptorClassName.toString());
      }
    }
    if (descriptorClass != null) {
      try {
        LOG.debug("Found descriptor: " + descriptorClass);
        results.put(className, descriptorClass.newInstance());
      } catch (InstantiationException e) {
        LOG.debug("Ignored exception: " + e + "at loading descriptor class for: " + className);
      } catch (IllegalAccessException e) {
        LOG.debug("Ignored exception: " + e + "at loading descriptor class for: " + className);
      }
    }
    return results;
  }
}
