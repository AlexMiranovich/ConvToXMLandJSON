package org.exolab.xml.validators;

import org.exec.xml.InternalContext;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class IntegerValidator extends PatternValidator implements TypeValidator {
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
  public Long getFixed() {
    if (this._useFixed) {
      return new Long(this._fixed);
    }
    return null;
  }
  public Long getMaxInclusive() {
    if (this._useMax) {
      return new Long(this._max);
    }
    return null;
  }
  public Long getMinInclusive() {
    if (this._useMin) {
      return new Long(this._min);
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
  public void setFixed(long fixedValue) {
    this._useFixed = true;
    this._fixed = fixedValue;
  }
  public void setFixed(int fixedValue) {
    this._useFixed = true;
    this._fixed = fixedValue;
  }
  public void setFixed(Long fixedValue) {
    this._useFixed = true;
    this._fixed = fixedValue.intValue();
  }
  public void setMinExclusive(long minValue) {
    this._useMin = true;
    this._min = (minValue + 1L);
  }
  public void setMinExclusive(int minValue) {
    this._useMin = true;
    this._min = (minValue + 1);
  }
  public void setMinInclusive(long minValue) {
    this._useMin = true;
    this._min = minValue;
  }
  public void setMinInclusive(int minValue) {
    this._useMin = true;
    this._min = minValue;
  }
  public void setMaxExclusive(long maxValue) {
    this._useMax = true;
    this._max = (maxValue - 1L);
  }
  public void setMaxExclusive(int maxValue) {
    this._useMax = true;
    this._max = (maxValue - 1);
  }
  public void setMaxInclusive(long maxValue) {
    this._useMax = true;
    this._max = maxValue;
  }
  public void setMaxInclusive(int maxValue) {
    this._useMax = true;
    this._max = maxValue;
  }
  public void setTotalDigits(int totalDig) {
    if (totalDig <= 0) {
      throw new IllegalArgumentException("IntegerValidator: the totalDigits facet must be positive");
    }
    this._totalDigits = totalDig;
  }
  public void validate(long i, ValidationContext context)
    throws ValidationException {
    if ((this._useFixed) && (i != this._fixed))
    {
      String err = "long " + i + " is not equal to the fixed value: " + this._fixed;
      throw new ValidationException(err);
    }
    if ((this._useMin) && (i < this._min))
    {
      String err = "long " + i + " is less than the minimum allowed value: " + this._min;
      throw new ValidationException(err);
    }
    if ((this._useMax) && (i > this._max))
    {
      String err = "long " + i + " is greater than the maximum allowed value: " + this._max;
      throw new ValidationException(err);
    }
    if (this._totalDigits != -1)
    {
      int length = Long.toString(i).length();
      if (i < 0L) {
        length--;
      }
      if (length > this._totalDigits)
      {
        String err = "long " + i + " has too many digits -- must have " + this._totalDigits + " digits or fewer.";
        
        throw new ValidationException(err);
      }
    }
    if (hasPattern()) {
      super.validate(Long.toString(i), context);
    }
  }
  public void validate(Object object)
    throws ValidationException {
    validate(object, (ValidationContext)null);
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if (object == null) {
      String err = "IntegerValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    long value = 0L;
    try {
      value = ((Long)object).longValue();
    }
    catch (Exception ex) {
      String lenientProperty = context.getInternalContext().getStringProperty("org.exolab.xml.lenient.integer.validation");
      if (Boolean.valueOf(lenientProperty).booleanValue())
      {
        try
        {
          value = ((Integer)object).longValue();
        }
        catch (Exception e)
        {
          String err = "Expecting a Long/Integer, received instead: ";
          err = err + object.getClass().getName();
          throw new ValidationException(err);
        }
      } else {
        String err = "Expecting an Long, received instead: ";
        err = err + object.getClass().getName();
        throw new ValidationException(err);
      }
    }
    validate(value, context);
  }
}
