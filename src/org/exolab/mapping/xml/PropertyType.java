package org.exolab.mapping.xml;

import java.io.Serializable;
import org.exolab.xml.ValidationException;
import org.exolab.xml.Validator;

public abstract class PropertyType implements Serializable {
  private String _name;
  private String _value;
  public String getName()
  {
    return this._name;
  }
  public String getValue()
  {
    return this._value;
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
  public void setName(String name)
  {
    this._name = name;
  }
  public void setValue(String value)
  {
    this._value = value;
  }
  public void validate()
    throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
