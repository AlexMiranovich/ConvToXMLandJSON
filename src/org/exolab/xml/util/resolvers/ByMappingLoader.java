package org.exolab.xml.util.resolvers;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.mapping.MappingLoader;
import org.exolab.xml.ResolverException;
import org.exolab.xml.XMLClassDescriptor;

public class ByMappingLoader extends AbstractResolverClassCommand {
  private static final Log LOG = LogFactory.getLog(ByMappingLoader.class);
  protected Map internalResolve(String className, ClassLoader classLoader, Map properties)
    throws ResolverException {
    MappingLoader mappingLoader = (MappingLoader)properties.get("org.exolab.xml.util.ResolverStrategy.MappingLoader");
    HashMap results = new HashMap();
    if (mappingLoader == null) {
      LOG.debug("No mapping loader specified");
      return results;
    }
    XMLClassDescriptor descriptor = (XMLClassDescriptor)mappingLoader.getDescriptor(className);
    if (descriptor != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Found descriptor: " + descriptor);
      }
      results.put(className, descriptor);
    }
    return results;
  }
}
