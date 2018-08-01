package org.exec.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingLoader;

public abstract class AbstractMappingLoaderFactory implements MappingLoaderFactory {
  public static final Log LOG = LogFactory.getLog(AbstractMappingLoaderFactory.class);
  private static final String SOURCE_TYPE = "ExecXmlMapping";
  public AbstractMappingLoaderFactory() { }
  public final String getSourceType() {
    return "ExecXmlMapping";
  }
  public abstract String getClassname();
  public final MappingLoader getMappingLoader() throws MappingException {
    MappingLoader mappingLoader = null;
    try {
      ClassLoader loader = this.getClass().getClassLoader();
      Class cls = loader.loadClass(this.getClassname());
      Class[] types = new Class[]{ClassLoader.class};
      Object[] args = new Object[]{loader};
      mappingLoader = (MappingLoader)cls.getConstructor(types).newInstance(args);
    } catch (Exception var6) {
      LOG.error("Problem instantiating mapping loader factory implementation: " + this.getClassname(), var6);
    }
    return mappingLoader;
  }
}
