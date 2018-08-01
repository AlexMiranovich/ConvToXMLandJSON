package org.exec.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.core.util.AbstractProperties;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingLoader;

public final class MappingLoaderRegistry {
  private static final Log LOG = LogFactory.getLog(MappingLoaderRegistry.class);
  private final List _mappingLoaderFactories = new ArrayList();
  private final List _mappingLoaders = new ArrayList();
  public MappingLoaderRegistry(AbstractProperties properties) {
    Object[] objects = properties.getObjectArray("org.exec.mapping.loaderFactories", getClass().getClassLoader());
    for (int i = 0; i < objects.length; i++) {
      this._mappingLoaderFactories.add(objects[i]);
    }
  }
  public void clear() {
    Iterator iter = this._mappingLoaders.iterator();
    while (iter.hasNext()) {
      ((MappingLoader)iter.next()).clear();
    }
  }
  public MappingLoader getMappingLoader(String sourceType, BindingType bindingType)
    throws MappingException {
    Iterator iter = this._mappingLoaderFactories.iterator();
    while (iter.hasNext()) {
      MappingLoaderFactory loaderFactory = (MappingLoaderFactory)iter.next();
      if ((loaderFactory.getSourceType().equals(sourceType)) && (loaderFactory.getBindingType() == bindingType)) {
        MappingLoader mappingLoader = loaderFactory.getMappingLoader();
        this._mappingLoaders.add(mappingLoader);
        return mappingLoader;
      }
    }
    String msg = "No mapping loader/factory for: SourceType=" + sourceType + " / BindingType=" + bindingType;
    LOG.error(msg);
    throw new MappingException(msg);
  }
  public Collection getMappingLoaderFactories() {
    return Collections.unmodifiableCollection(this._mappingLoaderFactories);
  }
}
