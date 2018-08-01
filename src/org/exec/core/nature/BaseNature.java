package org.exec.core.nature;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
public abstract class BaseNature implements Nature {
  private PropertyHolder _holder = null;
  protected BaseNature(PropertyHolder holder) {
    if (holder == null) {
      throw new NullPointerException("Holder must be set");
    }
    if (holder.hasNature(getId())) {
      this._holder = holder;
    } else {
      throw new IllegalStateException(getId() + " Nature must be set before");
    }
  }
  protected final Object getProperty(String key)
  {
    return this._holder.getProperty(addPrefix(key));
  }
  protected final void setProperty(String key, Object property) {
    if (key != null) {
      this._holder.setProperty(addPrefix(key), property);
    }
  }
  private String addPrefix(String key) {
    StringBuffer buf = new StringBuffer();
    buf.append(getId());
    buf.append(key);
    return buf.toString();
  }
  protected final boolean getBooleanPropertyDefaultFalse(String propertyName) {
    Boolean b = (Boolean)getProperty(propertyName);
    if (b == null) {
      return false;
    }
    return b.booleanValue();
  }
  protected final PropertyHolder getHolder()
  {
    return this._holder;
  }
  protected List getPropertyAsList(String property) {
    List list = (List)getProperty(property);
    if (list == null) {
      list = new LinkedList();
      setProperty(property, list);
    }
    return list;
  }
  protected Map getPropertyAsMap(String property) {
    Map map = (Map)getProperty(property);
    if (map == null) {
      map = new HashMap();
      setProperty(property, map);
    }
    return map;
  }
}
