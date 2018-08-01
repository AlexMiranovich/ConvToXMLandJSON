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

public class Container implements Serializable {
  private String _name;
  private String _type;
  private boolean _required = false;
  private boolean _has_required;
  private boolean _direct = false;
  private boolean _has_direct;
  private String _getMethod;
  private String _setMethod;
  private String _createMethod;
  private String _description;
  private FieldMapping _fieldMapping;
  public void deleteDirect()
  {
    this._has_direct = false;
  }
  public void deleteRequired()
  {
    this._has_required = false;
  }
  public String getCreateMethod()
  {
    return this._createMethod;
  }
  public String getDescription()
  {
    return this._description;
  }
  public boolean getDirect()
  {
    return this._direct;
  }
  public FieldMapping getFieldMapping()
  {
    return this._fieldMapping;
  }
  public String getGetMethod()
  {
    return this._getMethod;
  }
  public String getName()
  {
    return this._name;
  }
  public boolean getRequired()
  {
    return this._required;
  }
  public String getSetMethod()
  {
    return this._setMethod;
  }
  public String getType()
  {
    return this._type;
  }
  public boolean hasDirect()
  {
    return this._has_direct;
  }
  public boolean hasRequired()
  {
    return this._has_required;
  }
  public boolean isDirect()
  {
    return this._direct;
  }
  public boolean isRequired()
  {
    return this._required;
  }
  public boolean isValid() {
    try {
      validate();
    }
    catch (ValidationException vex)
    {
      return false;
    }
    return true;
  }
  public void marshal(Writer out) throws MarshalException, ValidationException {
    Marshaller.marshal(this, out);
  }
  public void marshal(ContentHandler handler)
    throws IOException, MarshalException, ValidationException {
    Marshaller.marshal(this, handler);
  }
  public void setCreateMethod(String createMethod)
  {
    this._createMethod = createMethod;
  }

  public void setDescription(String description) {
    this._description = description;
  }
  public void setDirect(boolean direct) {
    this._direct = direct;
    this._has_direct = true;
  }
  public void setFieldMapping(FieldMapping fieldMapping) {
    this._fieldMapping = fieldMapping;
  }
  public void setGetMethod(String getMethod) {
    this._getMethod = getMethod;
  }
  public void setName(String name) {
    this._name = name;
  }
  public void setRequired(boolean required) {
    this._required = required;
    this._has_required = true;
  }
  public void setSetMethod(String setMethod) {
    this._setMethod = setMethod;
  }
  public void setType(String type) {
    this._type = type;
  }
  public static Container unmarshal(Reader reader)
    throws MarshalException, ValidationException {
    return (Container)Unmarshaller.unmarshal(Container.class, reader);
  }
  public void validate()
    throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
