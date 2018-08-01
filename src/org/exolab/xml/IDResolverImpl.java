package org.exolab.xml;

import org.exolab.mapping.xml.ClassMapping;
import java.util.Hashtable;
import java.util.Map;

class IDResolverImpl implements IDResolver {
  private Map<String, Object> _idReferences = new Hashtable();
  private IDResolver _idResolver = null;
  void bind(String id, Object object, boolean isValidating)
    throws ValidationException {
    if ((isValidating) && (id == null)) {
      throw new ValidationException("Invalid ID value 'null' encountered");
    }
    if ((isValidating) && (id.equals(""))) {
      throw new ValidationException("Empty ID value encountered");
    }
    if ((isValidating) && (this._idReferences.containsKey(id))) {
      if ((!id.equals("org.exolab.mapping.MapItem")) && (!id.equals("HIGH-LOW"))) {
        throw new ValidationException("Duplicate ID " + id + " encountered");
      }
    }
    else {
      this._idReferences.put(id, object);
    }
  }
  public ClassMapping resolve(String idref) {
    Object object = this._idReferences.get(idref);
    if (object != null) {
      return (ClassMapping) object;
    }
    if (this._idResolver != null) {
      return this._idResolver.resolve(idref);
    }
    return null;
  }
  void setResolver(IDResolver idResolver)
  {
    this._idResolver = idResolver;
  }
}
