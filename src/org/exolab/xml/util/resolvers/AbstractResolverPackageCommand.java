package org.exolab.xml.util.resolvers;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.xml.ResolverException;
import org.exolab.xml.util.ResolverPackageCommand;

public abstract class AbstractResolverPackageCommand implements ResolverPackageCommand {
  private static final Log LOG = LogFactory.getLog(AbstractResolverPackageCommand.class);
  public final Map resolve(String packageName, Map properties)
    throws ResolverException {
    String pName;
    if ((packageName == null) || ("".equals(packageName))) {
      LOG.debug("Package name is empty! Anyhow, giving it a try...");
      pName = "";
    }
    else {
      pName = packageName;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Now in resolve method: " + getClass().getName() + " resolving: " + packageName);
    }
    ClassLoader classLoader = (ClassLoader)properties.get("org.exolab.xml.util.ResolverStrategy.ClassLoader");
    if (classLoader == null) {
      LOG.debug("No domain class loader set, taking it from class.getClassLoader().");
      classLoader = getClass().getClassLoader();
    }
    return internalResolve(pName, classLoader, properties);
  }
  protected final boolean isEmptyPackageName(String packageName) {
    return (packageName == null) || (packageName.length() == 0) || ("".equals(packageName));
  }
  protected abstract Map internalResolve(String paramString, ClassLoader paramClassLoader, Map paramMap)
    throws ResolverException;
}
