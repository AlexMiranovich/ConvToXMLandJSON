package org.exolab.xml.validators;

import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class ByteValidator extends PatternValidator implements TypeValidator {
  private boolean _useMin = false;
  private boolean _useMax = false;
  private boolean _useFixed = false;
  private byte _min = 0;
  private byte _max = 0;
  private int _totalDigits = -1;
  private byte _fixed = 0;
  public void clearFixed()
  {
    this._useFixed = false;
  }
  public void clearMax()
  {
    this._useMax = false;
  }
  public void clearMin()
  {
    this._useMin = false;
  }
  public Byte getFixed() {
    if (this._useFixed) {
      return new Byte(this._fixed);
    }
    return null;
  }
  public Byte getMaxInclusive() {
    if (this._useMax) {
      return new Byte(this._max);
    }
    return null;
  }
  public Byte getMinInclusive() {
    if (this._useMin) {
      return new Byte(this._min);
    }
    return null;
  }
  public Integer getTotalDigits() {
    if (this._totalDigits >= 0) {
      return new Integer(this._totalDigits);
    }
    return null;
  }
  public boolean hasFixed()
  {
    return this._useFixed;
  }
  public void setFixed(byte fixedValue) {
    this._useFixed = true;
    this._fixed = fixedValue;
  }
  public void setMinExclusive(byte minValue) {
    this._useMin = true;
    this._min = ((byte)(minValue + 1));
  }
  public void setMinInclusive(byte minValue) {
    this._useMin = true;
    this._min = minValue;
  }
  public void setMaxExclusive(byte maxValue) {
    this._useMax = true;
    this._max = ((byte)(maxValue - 1));
  }
  public void setMaxInclusive(byte maxValue) {
    this._useMax = true;
    this._max = maxValue;
  }
  public void setTotalDigits(int totalDig) {
    if (totalDig <= 0) {
      throw new IllegalArgumentException("IntegerValidator: the totalDigits facet must be positive");
    }
    this._totalDigits = totalDig;
  }
  public void validate(byte b, ValidationContext context)
    throws ValidationException {
    if ((this._useFixed) && (b != this._fixed)) {
      String err = "byte " + b + " is not equal to the fixed value: " + this._fixed;
      throw new ValidationException(err);
    }
    if ((this._useMin) && (b < this._min)) {
      String err = "byte " + b + " is less than the minimum allowed value: " + this._min;
      throw new ValidationException(err);
    }
    if ((this._useMax) && (b > this._max)) {
      String err = "byte " + b + " is greater than the maximum allowed value: " + this._max;
      throw new ValidationException(err);
    }
    if (this._totalDigits != -1) {
      int length = Byte.toString(b).length();
      if (b < 0) {
        length--;
      }
      if (length > this._totalDigits) {
        String err = "byte " + b + " has too many digits -- must have " + this._totalDigits + " digits or fewer.";
        throw new ValidationException(err);
      }
    }
    if (hasPattern()) {
      super.validate(Byte.toString(b), context);
    }
  }
  public void validate(Object object) throws ValidationException {
    validate(object, (ValidationContext)null);
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if (object == null) {
      String err = "ByteValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    byte value = 0;
    try {
      value = ((Byte)object).byteValue();
    }
    catch (Exception ex) {
      String err = "Expecting a Byte, received instead: ";
      err = err + object.getClass().getName();
      throw new ValidationException(err);
    }
    validate(value, context);
  }
}
