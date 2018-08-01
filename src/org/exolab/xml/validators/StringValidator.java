package org.exolab.xml.validators;

import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public class StringValidator extends PatternValidator implements TypeValidator {
  private boolean _required = false;
  private String _whiteSpace = "preserve";
  private int _length = 0;
  private int _minLength = 0;
  private int _maxLength = -1;
  private String _fixed = null;
  public void clearFixed()
  {
    this._fixed = null;
  }
  public void setFixed(String fixedValue)
  {
    this._fixed = fixedValue;
  }
  public void setFixedValue(String fixedValue)
  {
    setFixed(fixedValue);
  }
  public void setMaxLength(int maxLength)
  {
    this._maxLength = maxLength;
  }
  public void setMinLength(int minLength)
  {
    this._minLength = minLength;
  }
  public void setLength(int length) {
    this._length = length;
    setMaxLength(length);
    setMinLength(length);
  }
  public void setRequired(boolean required)
  {
    this._required = required;
  }
  public void setWhiteSpace(String value) {
    if (value.equals("preserve")) {
      this._whiteSpace = value;
    }
    else if (value.equals("replace")) {
      this._whiteSpace = value;
    }
    else if (value.equals("collapse")) {
      this._whiteSpace = value;
    }
    else {
      System.out.println("Warning: '" + value + "' is a bad entry for the whiteSpace value");
      
      this._whiteSpace = "preserve";
    }
  }
  public String normalize(String value) {
    if (value == null) {
      return null;
    }
    if (value.length() == 0) {
      return value;
    }
    char[] chars = value.toCharArray();
    int length = chars.length;
    for (int i = 0; i < length; i++) {
      switch (chars[i]) {
      case '\t': 
      case '\n': 
      case '\r': 
        chars[i] = ' ';
      }
    }
    if (this._whiteSpace.equals("collapse")) {
      char[] temp = new char[chars.length];
      int tempCount = 0;
      int i = 0;
      while (i < length - 1) {
        if (chars[i] == ' ') {
          temp[tempCount] = chars[i];
          tempCount++;
          
          i++;
          while ((i < length - 1) && (chars[i] == ' ')) {
            i++;
          }
        }
        else {
          temp[tempCount] = chars[i];
          tempCount++;
          i++;
        }
      }
      if (chars[i] != ' ') {
        temp[tempCount] = chars[i];
      }
      tempCount++;length = tempCount;
      chars = temp;
    }
    return new String(chars, 0, length);
  }
  public void validate(String value, ValidationContext context)
    throws ValidationException {
    if (value == null) {
      if ((this._required) && (!isNillable())) {
        String err = "this is a required field and cannot be null.";
        throw new ValidationException(err);
      }
      return;
    }
    if ((this._fixed != null) && (!this._fixed.equals(value))) {
      String err = "strings of this type must be equal to the fixed value of " + this._fixed;
      throw new ValidationException(err);
    }
    int len = value.length();
    if ((this._length > 0) && (len != this._length)) {
      String err = "Strings of this type must have a length of " + this._length + " characters";
      throw new ValidationException(err);
    }
    if ((this._minLength > 0) && (len < this._minLength)) {
      String err = "Strings of this type must have a minimum length of " + this._minLength + " characters";
      
      throw new ValidationException(err);
    }
    if ((this._maxLength >= 0) && (len > this._maxLength)) {
      String err = "Strings of this type must have a maximum length of " + this._maxLength + " characters";
      
      throw new ValidationException(err);
    }
    if (hasPattern()) {
      super.validate(value, context);
    }
    if (!this._whiteSpace.equals("preserve")) {
      normalize(value);
    }
  }
  public void validate(Object object)
    throws ValidationException {
    validate(object, (ValidationContext)null);
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if (object == null) {
      if (this._required) {
        String err = "this is a required field and cannot be null.";
        throw new ValidationException(err);
      }
      return;
    }
    validate(object.toString(), context);
  }
}
