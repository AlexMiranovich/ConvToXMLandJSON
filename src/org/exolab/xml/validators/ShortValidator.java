package org.exolab.xml.validators;

import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class ShortValidator extends PatternValidator implements TypeValidator {
  private boolean _useMin = false;
  private boolean _useMax = false;
  private boolean _useFixed = false;
  private short _min = 0;
  private short _max = 0;
  private int _totalDigits = -1;
  private short _fixed = 0;
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
  public Short getFixed() {
    if (this._useFixed) {
      return new Short(this._fixed);
    }
    return null;
  }
  public Short getMaxInclusive() {
    if (this._useMax) {
      return new Short(this._max);
    }
    return null;
  }
  
  public Short getMinInclusive()
  {
    if (this._useMin) {
      return new Short(this._min);
    }
    return null;
  }
  
  public Integer getTotalDigits()
  {
    if (this._totalDigits >= 0) {
      return new Integer(this._totalDigits);
    }
    return null;
  }
  
  public boolean hasFixed()
  {
    return this._useFixed;
  }
  
  public void setFixed(short fixedValue)
  {
    this._useFixed = true;
    this._fixed = fixedValue;
  }
  
  public void setMinExclusive(short minValue)
  {
    this._useMin = true;
    this._min = ((short)(minValue + 1));
  }
  
  public void setMinInclusive(short minValue)
  {
    this._useMin = true;
    this._min = minValue;
  }
  
  public void setMaxExclusive(short maxValue)
  {
    this._useMax = true;
    this._max = ((short)(maxValue - 1));
  }
  
  public void setMaxInclusive(short maxValue)
  {
    this._useMax = true;
    this._max = maxValue;
  }
  
  public void setTotalDigits(int totalDig)
  {
    if (totalDig <= 0) {
      throw new IllegalArgumentException("ShortValidator: the totalDigits facet must be positive");
    }
    this._totalDigits = totalDig;
  }
  
  public void validate(short s, ValidationContext context)
    throws ValidationException
  {
    if ((this._useFixed) && (s != this._fixed))
    {
      String err = "short " + s + " is not equal to the fixed value: " + this._fixed;
      throw new ValidationException(err);
    }
    if ((this._useMin) && (s < this._min))
    {
      String err = "short " + s + " is less than the minimum allowed value: " + this._min;
      throw new ValidationException(err);
    }
    if ((this._useMax) && (s > this._max))
    {
      String err = "short " + s + " is greater than the maximum allowed value: " + this._max;
      throw new ValidationException(err);
    }
    if (this._totalDigits != -1)
    {
      int length = Short.toString(s).length();
      if (s < 0) {
        length--;
      }
      if (length > this._totalDigits)
      {
        String err = "short " + s + " has too many digits -- must have " + this._totalDigits + " digits or fewer.";
        
        throw new ValidationException(err);
      }
    }
    if (hasPattern()) {
      super.validate(Short.toString(s), context);
    }
  }
  
  public void validate(Object object)
    throws ValidationException
  {
    validate(object, (ValidationContext)null);
  }
  
  public void validate(Object object, ValidationContext context)
    throws ValidationException
  {
    if (object == null)
    {
      String err = "ShortValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    short value = 0;
    try
    {
      value = ((Short)object).shortValue();
    }
    catch (Exception ex)
    {
      String err = "Expecting a Short, received instead: " + object.getClass().getName();
      throw new ValidationException(err);
    }
    validate(value, context);
  }
}
