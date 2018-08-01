package org.exolab.xml.validators;

import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class LongValidator extends PatternValidator implements TypeValidator {
  private boolean _useMin = false;
  private boolean _useMax = false;
  private boolean _useFixed = false;
  private long _min = 0L;
  private long _max = 0L;
  private int _totalDigits = -1;
  private long _fixed = 0L;
  
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
  
  public Long getFixed()
  {
    if (this._useFixed) {
      return new Long(this._fixed);
    }
    return null;
  }
  
  public Long getMaxInclusive()
  {
    if (this._useMax) {
      return new Long(this._max);
    }
    return null;
  }
  
  public Long getMinInclusive()
  {
    if (this._useMin) {
      return new Long(this._min);
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
  
  public void setFixed(long fixedValue)
  {
    this._useFixed = true;
    this._fixed = fixedValue;
  }
  
  public void setMinExclusive(long minValue)
  {
    this._useMin = true;
    this._min = (minValue + 1L);
  }
  
  public void setMinInclusive(long minValue)
  {
    this._useMin = true;
    this._min = minValue;
  }
  
  public void setMaxExclusive(long maxValue)
  {
    this._useMax = true;
    this._max = (maxValue - 1L);
  }
  
  public void setMaxInclusive(long maxValue)
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
  
  public void validate(long value, ValidationContext context)
    throws ValidationException
  {
    if ((this._useFixed) && (value != this._fixed))
    {
      String err = "long " + value + " is not equal to the fixed value: " + this._fixed;
      throw new ValidationException(err);
    }
    if ((this._useMin) && (value < this._min))
    {
      String err = "long " + value + " is less than the minimum allowed value: " + this._min;
      throw new ValidationException(err);
    }
    if ((this._useMax) && (value > this._max))
    {
      String err = "long " + value + " is greater than the maximum allowed value: " + this._max;
      throw new ValidationException(err);
    }
    if (this._totalDigits != -1)
    {
      int length = Long.toString(value).length();
      if (value < 0L) {
        length--;
      }
      if (length > this._totalDigits)
      {
        String err = "long " + value + " has too many digits -- must be " + this._totalDigits + " digits or fewer.";
        
        throw new ValidationException(err);
      }
    }
    if (hasPattern()) {
      super.validate(Long.toString(value), context);
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
      String err = "LongValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    long value = 0L;
    try
    {
      value = ((Long)object).longValue();
    }
    catch (Exception ex)
    {
      String err = "Expecting a Long, received instead: ";
      err = err + object.getClass().getName();
      throw new ValidationException(err);
    }
    validate(value, context);
  }
}
