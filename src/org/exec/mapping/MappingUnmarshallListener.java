package org.exec.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.xml.UnmarshalListener;
import org.exolab.mapping.Mapping;
import org.exolab.mapping.xml.Include;
import org.exolab.util.DTDResolver;

public final class MappingUnmarshallListener implements UnmarshalListener {
  private static final Log LOG = LogFactory.getLog(MappingUnmarshallListener.class);
  private final MappingUnmarshaller _unmarshaller;
  private final Mapping _mapping;
  private final DTDResolver _resolver;
  
  public MappingUnmarshallListener(MappingUnmarshaller unmarshaller, Mapping mapping, DTDResolver resolver)
  {
    this._unmarshaller = unmarshaller;
    this._mapping = mapping;
    this._resolver = resolver;
  }

  public void initialized(Object target, Object parent) {}
  
  public void attributesProcessed(Object target, Object parent) {}
  
  public void fieldAdded(String fieldName, Object parent, Object child) {}
  
  public void unmarshalled(Object target, Object parent)
  {
    if ((target instanceof Include))
    {
      Include include = (Include)target;
      try
      {
        this._unmarshaller.loadMappingInternal(this._mapping, this._resolver, include.getHref());
      }
      catch (Exception ex)
      {
        LOG.warn("Problem", ex);
      }
    }
  }

  public void setUnmarshalListener(UnmarshalListener listener) {

  }
}
