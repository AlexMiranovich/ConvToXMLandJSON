package org.exolab.xml.util.resolvers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.xml.ResolverException;

public class ByCDR extends AbstractResolverPackageCommand {
  private static final Log LOG = LogFactory.getLog(ByCDR.class);
  private ArrayList _loadedPackages = new ArrayList();
  private Properties getProperties(URL url) throws IOException {
    Properties cdrList = new Properties();
    InputStream stream = url.openStream();
    cdrList.load(stream);
    stream.close();
    return cdrList;
  }
  protected Map internalResolve(String packageName, ClassLoader classLoader, Map properties) throws ResolverException {
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
    URL url = classLoader.getResource(ResolveHelpers.getQualifiedFileName(".exec.cdr", packageName));
    if (url == null) {
      return results;
    }
    try {
      Properties cdrList = getProperties(url);
      
      Enumeration classes = cdrList.keys();
      while (classes.hasMoreElements()) {
        String clazzName = (String)classes.nextElement();
        String descriptorClassName = (String)cdrList.get(clazzName);
        try {
          Class descriptorClass = classLoader.loadClass(descriptorClassName);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Found descriptor: " + descriptorClass);
          }
          if (descriptorClass != null) {
            results.put(clazzName, descriptorClass.newInstance());
          } else if (LOG.isDebugEnabled()) {
            LOG.debug("Loading of descriptor class: " + descriptorClassName + " for class: " + clazzName + " has failed - continue without");
          }
        } catch (Exception e) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Ignored problem at loading of: " + descriptorClassName + " with exception: " + e);
          }
        }
      }
    } catch (IOException iox) {
      String message = "Failed to load package: " + packageName + " with exception: " + iox;
      LOG.warn(message);
      throw new ResolverException(message);
    }
    return results;
  }
}
