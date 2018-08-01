package org.exec.mapping;

import org.exolab.mapping.xml.ClassMapping;
import org.exolab.mapping.xml.MappingRoot;
import org.exolab.xml.IDResolver;

public final class MappingUnmarshallIDResolver implements IDResolver {
  private MappingRoot _mapping = null;
  public void setMapping(MappingRoot mapping)
  {
    this._mapping = mapping;
  }
  public ClassMapping resolve(String idref) {
    if (this._mapping == null) {
      return null;
    }
    for (int i = 0; i < this._mapping.getClassMappingCount(); i++) {
      ClassMapping clsMap = this._mapping.getClassMapping(i);
      if (idref.equals(clsMap.getName())) {
        return clsMap;
      }
    }
    return null;
  }
}
