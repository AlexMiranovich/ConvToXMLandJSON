package org.exolab.xml.handlers;

import java.lang.reflect.Array;
import java.util.StringTokenizer;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationException;
import org.exolab.xml.XMLFieldHandler;

public class CollectionFieldHandler extends XMLFieldHandler {
  private final FieldHandler _handler;
  private final TypeValidator _validator;
  public CollectionFieldHandler(FieldHandler fieldHandler)
  {
    this(fieldHandler, null);
  }
  public CollectionFieldHandler(FieldHandler fieldHandler, TypeValidator validator) {
    if (fieldHandler == null) {
      String err = "The FieldHandler argument passed to the constructor of CollectionFieldHandler must not be null.";
      throw new IllegalArgumentException(err);
    }
    this._handler = fieldHandler;
    this._validator = validator;
  }
  public void setValue(Object target, Object value) throws IllegalStateException {
    if (value == null) {
      return;
    }
    if (!(value instanceof String)) {
      this._handler.setValue(target, value);
      return;
    }
    StringTokenizer temp = new StringTokenizer((String)value, " ");
    int size = temp.countTokens();
    for (int i = 0; i < size; i++) {
      String tempValue = temp.nextToken();
      try {
        if (this._validator != null) {
          this._validator.validate(tempValue, null);
        }
      }
      catch (ValidationException e) {
        throw new IllegalStateException(e.getMessage());
      }
      this._handler.setValue(target, tempValue);
    }
  }
  public Object getValue(Object target) throws IllegalStateException {
    Object temp = this._handler.getValue(target);
    if (temp == null) {
      return temp;
    }
    if (!temp.getClass().isArray()) {
      return temp.toString();
    }
    int size = Array.getLength(temp);
    if (size == 0) {
      return null;
    }
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < size; i++) {
      if (i > 0) {
        result.append(' ');
      }
      Object obj = Array.get(temp, i);
      result.append(obj.toString());
    }
    return result.toString();
  }
  public void resetValue(Object target)
    throws IllegalStateException {
    this._handler.resetValue(target);
  }
  public void checkValidity(Object object)
    throws ValidityException, IllegalStateException {}
  public Object newInstance(Object parent)
    throws IllegalStateException {
    return null;
  }
  public boolean equals(Object obj) {
    if ((obj == null) || (!(obj instanceof XMLFieldHandler))) {
      return false;
    }
    return this._handler.getClass().isInstance(obj);
  }
}
