package org.exolab.xml.util.resolvers;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.xml.ResolverException;
import org.exolab.xml.util.ResolverClassCommand;

public abstract class AbstractResolverClassCommand implements ResolverClassCommand {
  private static final Log LOG = LogFactory.getLog(AbstractResolverClassCommand.class);
  public final Map resolve(String className, Map properties)
    throws ResolverException {
    if ((className == null) || ("".equals(className))) {
      String message = "No class to resolve specified";
      LOG.warn(message);
      throw new IllegalArgumentException(message);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Now in method: " + getClass().getName() + " resolving: " + className);
    }
    ClassLoader classLoader = (ClassLoader)properties.get("org.exolab.xml.util.ResolverStrategy.ClassLoader");
    return internalResolve(className, classLoader, properties);
  }
  protected abstract Map internalResolve(String paramString, ClassLoader paramClassLoader, Map paramMap)
    throws ResolverException;
}
