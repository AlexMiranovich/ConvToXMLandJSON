package org.exolab.xml.validators;

import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class IntValidator extends PatternValidator implements TypeValidator {
  private boolean _useMin = false;
  private boolean _useMax = false;
  private boolean _useFixed = false;
  private int _min = 0;
  private int _max = 0;
  private int _totalDigits = -1;
  private int _fixed = 0;
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
  public Integer getFixed() {
    if (this._useFixed) {
      return new Integer(this._fixed);
    }
    return null;
  }
  
  public Integer getMaxInclusive()
  {
    if (this._useMax) {
      return new Integer(this._max);
    }
    return null;
  }
  
  public Integer getMinInclusive()
  {
    if (this._useMin) {
      return new Integer(this._min);
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
  
  public void setFixed(int fixedValue)
  {
    this._useFixed = true;
    this._fixed = fixedValue;
  }
  
  public void setFixed(Integer fixedValue)
  {
    this._useFixed = true;
    this._fixed = fixedValue.intValue();
  }
  
  public void setMinExclusive(int minValue)
  {
    this._useMin = true;
    this._min = (minValue + 1);
  }
  
  public void setMinInclusive(int minValue)
  {
    this._useMin = true;
    this._min = minValue;
  }
  
  public void setMaxExclusive(int maxValue)
  {
    this._useMax = true;
    this._max = (maxValue - 1);
  }
  
  public void setMaxInclusive(int maxValue)
  {
    this._useMax = true;
    this._max = maxValue;
  }
  
  public void setTotalDigits(int totalDig)
  {
    if (totalDig <= 0) {
      throw new IllegalArgumentException("IntegerValidator: the totalDigits facet must be positive");
    }
    this._totalDigits = totalDig;
  }
  
  public void validate(int i, ValidationContext context)
    throws ValidationException
  {
    if ((this._useFixed) && (i != this._fixed))
    {
      String err = "int " + i + " is not equal to the fixed value: " + this._fixed;
      throw new ValidationException(err);
    }
    if ((this._useMin) && (i < this._min))
    {
      String err = "int " + i + " is less than the minimum allowed value: " + this._min;
      throw new ValidationException(err);
    }
    if ((this._useMax) && (i > this._max))
    {
      String err = "int " + i + " is greater than the maximum allowed value: " + this._max;
      throw new ValidationException(err);
    }
    if (this._totalDigits != -1)
    {
      int length = Integer.toString(i).length();
      if (i < 0) {
        length--;
      }
      if (length > this._totalDigits)
      {
        String err = "int " + i + " has too many digits -- must have " + this._totalDigits + " digits or fewer.";
        
        throw new ValidationException(err);
      }
    }
    if (hasPattern()) {
      super.validate(Integer.toString(i), context);
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
      String err = "IntValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    int value = 0;
    try
    {
      value = ((Integer)object).intValue();
    }
    catch (Exception ex)
    {
      String err = "Expecting an Integer, received instead an instance of: ";
      err = err + object.getClass().getName();
      throw new ValidationException(err);
    }
    validate(value, context);
  }
}
