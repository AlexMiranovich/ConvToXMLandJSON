package org.exolab.xml.validators;

import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public class SimpleTypeValidator implements TypeValidator {
  private int _minOccurs = 0;
  private int _maxOccurs = -1;
  private TypeValidator _validator = null;
  public SimpleTypeValidator() {}
  public SimpleTypeValidator(TypeValidator validator)
  {
    this._validator = validator;
  }
  public void setMaxOccurs(int maxOccurs)
  {
    this._maxOccurs = maxOccurs;
  }
  public void setMinOccurs(int minOccurs)
  {
    this._minOccurs = minOccurs;
  }
  public void setValidator(TypeValidator validator)
  {
    this._validator = validator;
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    boolean required = this._minOccurs > 0;
    if ((object == null) && (required)) {
      String err = "This field is required and cannot be null.";
      throw new ValidationException(err);
    }
    if (object != null) {
      Class type = object.getClass();
      int size = 1;
      boolean byteArray = false;
      if (type.isArray()) {
        byteArray = type.getComponentType() == Byte.TYPE;
        if (!byteArray) {
          size = Array.getLength(object);
        }
      }
      if (size < this._minOccurs) {
        String err = "A minimum of " + this._minOccurs + " instance(s) of this field is required.";
        throw new ValidationException(err);
      }
      if ((this._maxOccurs >= 0) && (size > this._maxOccurs)) {
        String err = "A maximum of " + this._maxOccurs + " instance(s) of this field are allowed.";
        throw new ValidationException(err);
      }
      if (this._validator == null) {
        return;
      }
      if ((isPrimitive(type)) || (type == String.class)) {
        this._validator.validate(object, context);
      } else if (!byteArray) {
        if (type.isArray()) {
          size = Array.getLength(object);
          for (int i = 0; i < size; i++) {
            this._validator.validate(Array.get(object, i), context);
          }
        }
        else if ((object instanceof Enumeration)) {
          Enumeration enumeration = (Enumeration)object;
          while (enumeration.hasMoreElements()) {
            this._validator.validate(enumeration.nextElement(), context);
          }
        }
        else if ((object instanceof Vector)) {
          Vector vector = (Vector)object;
          for (int i = 0; i < vector.size(); i++) {
            this._validator.validate(vector.elementAt(i), context);
          }
        } else {
          this._validator.validate(object, context);
        }
      }
    }
  }
  private boolean isPrimitive(Class type) {
    if (type.isPrimitive()) {
      return true;
    }
    return (type == Boolean.class) || (type == Byte.class) || (type == Character.class) || (type == Double.class) || (type == Float.class) || (type == Integer.class) || (type == Long.class) || (type == Short.class);
  }
}
