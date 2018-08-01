package org.exolab.mapping.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.mapping.xml.types.FieldMappingCollectionType;
import org.exolab.xml.MarshalException;
import org.exolab.xml.Marshaller;
import org.exolab.xml.Unmarshaller;
import org.exolab.xml.ValidationException;
import org.exolab.xml.Validator;
import org.xml.sax.ContentHandler;

public class FieldMapping implements Serializable {
  private String _name;
  private String _type;
  private boolean _required = false;
  private boolean _has_required;
  private boolean _transient = false;
  private boolean _has_transient;
  private boolean _nillable = false;
  private boolean _has_nillable;
  private boolean _direct = false;
  private boolean _has_direct;
  private boolean _lazy = false;
  private boolean _has_lazy;
  private boolean _container;
  private boolean _has_container;
  private String _getMethod;
  private String _hasMethod;
  private String _setMethod;
  private String _createMethod;
  private String _handler;
  private FieldMappingCollectionType _collection;
  private String _comparator;
  private boolean _identity = false;
  private boolean _has_identity;
  private String _description;
  private Sql _sql;
  private BindXml _bindXml;
  private Ldap _ldap;

  public FieldMapping() {
  }

  public void deleteContainer() {
    this._has_container = false;
  }

  public void deleteDirect() {
    this._has_direct = false;
  }

  public void deleteIdentity() {
    this._has_identity = false;
  }

  public void deleteLazy() {
    this._has_lazy = false;
  }

  public void deleteNillable() {
    this._has_nillable = false;
  }

  public void deleteRequired() {
    this._has_required = false;
  }

  public void deleteTransient() {
    this._has_transient = false;
  }

  public BindXml getBindXml() {
    return this._bindXml;
  }

  public FieldMappingCollectionType getCollection() {
    return this._collection;
  }

  public String getComparator() {
    return this._comparator;
  }

  public boolean getContainer() {
    return this._container;
  }

  public String getCreateMethod() {
    return this._createMethod;
  }

  public String getDescription() {
    return this._description;
  }

  public boolean getDirect() {
    return this._direct;
  }

  public String getGetMethod() {
    return this._getMethod;
  }

  public String getHandler() {
    return this._handler;
  }

  public String getHasMethod() {
    return this._hasMethod;
  }

  public boolean getIdentity() {
    return this._identity;
  }

  public boolean getLazy() {
    return this._lazy;
  }

  public Ldap getLdap() {
    return this._ldap;
  }

  public String getName() {
    return this._name;
  }

  public boolean getNillable() {
    return this._nillable;
  }

  public boolean getRequired() {
    return this._required;
  }

  public String getSetMethod() {
    return this._setMethod;
  }

  public Sql getSql() {
    return this._sql;
  }

  public boolean getTransient() {
    return this._transient;
  }

  public String getType() {
    return this._type;
  }

  public boolean hasContainer() {
    return this._has_container;
  }

  public boolean hasDirect() {
    return this._has_direct;
  }

  public boolean hasIdentity() {
    return this._has_identity;
  }

  public boolean hasLazy() {
    return this._has_lazy;
  }

  public boolean hasNillable() {
    return this._has_nillable;
  }

  public boolean hasRequired() {
    return this._has_required;
  }

  public boolean hasTransient() {
    return this._has_transient;
  }

  public boolean isContainer() {
    return this._container;
  }

  public boolean isDirect() {
    return this._direct;
  }

  public boolean isIdentity() {
    return this._identity;
  }

  public boolean isLazy() {
    return this._lazy;
  }

  public boolean isNillable() {
    return this._nillable;
  }

  public boolean isRequired() {
    return this._required;
  }

  public boolean isTransient() {
    return this._transient;
  }

  public boolean isValid() {
    try {
      this.validate();
      return true;
    } catch (ValidationException var2) {
      return false;
    }
  }

  public void marshal(Writer out) throws MarshalException, ValidationException {
    Marshaller.marshal(this, out);
  }

  public void marshal(ContentHandler handler) throws IOException, MarshalException, ValidationException {
    Marshaller.marshal(this, handler);
  }

  public void setBindXml(BindXml bindXml) {
    this._bindXml = bindXml;
  }

  public void setCollection(FieldMappingCollectionType collection) {
    this._collection = collection;
  }

  public void setComparator(String comparator) {
    this._comparator = comparator;
  }

  public void setContainer(boolean container) {
    this._container = container;
    this._has_container = true;
  }

  public void setCreateMethod(String createMethod) {
    this._createMethod = createMethod;
  }

  public void setDescription(String description) {
    this._description = description;
  }

  public void setDirect(boolean direct) {
    this._direct = direct;
    this._has_direct = true;
  }

  public void setGetMethod(String getMethod) {
    this._getMethod = getMethod;
  }

  public void setHandler(String handler) {
    this._handler = handler;
  }

  public void setHasMethod(String hasMethod) {
    this._hasMethod = hasMethod;
  }

  public void setIdentity(boolean identity) {
    this._identity = identity;
    this._has_identity = true;
  }

  public void setLazy(boolean lazy) {
    this._lazy = lazy;
    this._has_lazy = true;
  }

  public void setLdap(Ldap ldap) {
    this._ldap = ldap;
  }

  public void setName(String name) {
    this._name = name;
  }

  public void setNillable(boolean nillable) {
    this._nillable = nillable;
    this._has_nillable = true;
  }

  public void setRequired(boolean required) {
    this._required = required;
    this._has_required = true;
  }

  public void setSetMethod(String setMethod) {
    this._setMethod = setMethod;
  }

  public void setSql(Sql sql) {
    this._sql = sql;
  }

  public void setTransient(boolean _transient) {
    this._transient = _transient;
    this._has_transient = true;
  }

  public void setType(String type) {
    this._type = type;
  }

  public static FieldMapping unmarshal(Reader reader) throws MarshalException, ValidationException {
    return (FieldMapping)Unmarshaller.unmarshal(FieldMapping.class, reader);
  }

  public void validate() throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
