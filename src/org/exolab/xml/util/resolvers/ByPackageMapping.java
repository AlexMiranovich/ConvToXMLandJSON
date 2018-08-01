package org.exolab.xml.util.resolvers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.mapping.BindingType;
import org.exec.mapping.MappingUnmarshaller;
import org.exolab.mapping.Mapping;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingLoader;
import org.exolab.xml.ResolverException;
import org.exolab.xml.XMLClassDescriptor;

public class ByPackageMapping extends AbstractResolverPackageCommand {
  private static final Log LOG = LogFactory.getLog(ByPackageMapping.class);
  private ArrayList _loadedPackages = new ArrayList();
  private Mapping loadMapping(String packageName, ClassLoader classLoader)
    throws MappingException {
    URL url = classLoader.getResource(ResolveHelpers.getQualifiedFileName(".exec.xml", packageName));
    if (url == null) {
      return null;
    }try {
      Mapping mapping = new Mapping(classLoader);
      mapping.loadMapping(url);
      return mapping;
    } catch (IOException ioex) {
      throw new MappingException(ioex);
    }
  }
  protected Map internalResolve(String packageName, ClassLoader classLoader, Map properties)
    throws ResolverException {
    HashMap results = new HashMap();
    if ((!isEmptyPackageName(packageName)) && (this._loadedPackages.contains(packageName))) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Package: " + packageName + " has already been loaded.");
      }
      return results;
    }
    if (!isEmptyPackageName(packageName)) {
      this._loadedPackages.add(packageName);
    }
    try {
      Mapping mapping = loadMapping(packageName, classLoader);
      if (mapping != null) {
        MappingUnmarshaller unmarshaller = new MappingUnmarshaller();
        MappingLoader mappingLoader = unmarshaller.getMappingLoader(mapping, BindingType.XML);
        Iterator descriptors = mappingLoader.descriptorIterator();
        while (descriptors.hasNext()) {
          XMLClassDescriptor descriptor = (XMLClassDescriptor)descriptors.next();
          if (LOG.isDebugEnabled()) {
            LOG.debug("Found descriptor: " + descriptor);
          }
          results.put(descriptor.getJavaClass().getName(), descriptor);
        }
      }
    } catch (MappingException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Ignored exception: " + e + " while loading mapping for package: " + packageName);
      }
    }
    return results;
  }
}
