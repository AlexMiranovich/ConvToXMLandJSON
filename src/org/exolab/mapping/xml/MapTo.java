package org.exolab.mapping.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.xml.MarshalException;
import org.exolab.xml.Marshaller;
import org.exolab.xml.Unmarshaller;
import org.exolab.xml.ValidationException;
import org.exolab.xml.Validator;
import org.xml.sax.ContentHandler;

public class MapTo implements Serializable {
  private String _table;
  private String _xml;
  private String _nsUri;
  private String _nsPrefix;
  private boolean _elementDefinition = false;
  private boolean _has_elementDefinition;
  private String _ldapDn;
  private String _ldapOc;
  public void deleteElementDefinition()
  {
    this._has_elementDefinition = false;
  }
  public boolean getElementDefinition()
  {
    return this._elementDefinition;
  }
  public String getLdapDn()
  {
    return this._ldapDn;
  }
  public String getLdapOc()
  {
    return this._ldapOc;
  }
  public String getNsPrefix()
  {
    return this._nsPrefix;
  }
  public String getNsUri()
  {
    return this._nsUri;
  }
  public String getTable()
  {
    return this._table;
  }
  public String getXml()
  {
    return this._xml;
  }
  public boolean hasElementDefinition()
  {
    return this._has_elementDefinition;
  }
  public boolean isElementDefinition()
  {
    return this._elementDefinition;
  }
  public boolean isValid() {
    try {
      validate();
    }
    catch (ValidationException vex) {
      return false;
    }
    return true;
  }
  public void marshal(Writer out)
    throws MarshalException, ValidationException {
    Marshaller.marshal(this, out);
  }
  public void marshal(ContentHandler handler) throws IOException, MarshalException, ValidationException {
    Marshaller.marshal(this, handler);
  }
  public void setElementDefinition(boolean elementDefinition) {
    this._elementDefinition = elementDefinition;
    this._has_elementDefinition = true;
  }
  public void setLdapDn(String ldapDn)
  {
    this._ldapDn = ldapDn;
  }
  public void setLdapOc(String ldapOc)
  {
    this._ldapOc = ldapOc;
  }
  public void setNsPrefix(String nsPrefix)
  {
    this._nsPrefix = nsPrefix;
  }
  public void setNsUri(String nsUri)
  {
    this._nsUri = nsUri;
  }
  public void setTable(String table)
  {
    this._table = table;
  }
  public void setXml(String xml)
  {
    this._xml = xml;
  }
  public static MapTo unmarshal(Reader reader)
    throws MarshalException, ValidationException {
    return (MapTo)Unmarshaller.unmarshal(MapTo.class, reader);
  }
  public void validate()
    throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
