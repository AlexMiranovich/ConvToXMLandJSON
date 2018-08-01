package org.exolab.xml.validators;

import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class DoubleValidator extends PatternValidator implements TypeValidator {
  private boolean _useMinInclusive = false;
  private boolean _useMinExclusive = false;
  private boolean _useMaxInclusive = false;
  private boolean _useMaxExclusive = false;
  private boolean _useFixed = false;
  private double _minInclusive = 0.0D;
  private double _minExclusive = 0.0D;
  private double _maxInclusive = 0.0D;
  private double _maxExclusive = 0.0D;
  private double _fixed = 0.0D;
  public void clearFixed()
  {
    this._useFixed = false;
  }
  public void clearMax() {
    this._useMaxExclusive = false;
    this._useMaxInclusive = false;
  }
  
  public void clearMin()
  {
    this._useMinExclusive = false;
    this._useMinInclusive = false;
  }
  
  public Double getFixed()
  {
    if (this._useFixed) {
      return new Double(this._fixed);
    }
    return null;
  }
  
  public Double getMaxInclusive()
  {
    if (this._useMaxInclusive) {
      return new Double(this._maxInclusive);
    }
    return null;
  }
  
  public Double getMaxExclusive()
  {
    if (this._useMaxExclusive) {
      return new Double(this._maxExclusive);
    }
    return null;
  }
  
  public Double getMinInclusive()
  {
    if (this._useMinInclusive) {
      return new Double(this._minInclusive);
    }
    return null;
  }
  
  public Double getMinExclusive()
  {
    if (this._useMinExclusive) {
      return new Double(this._minExclusive);
    }
    return null;
  }
  
  public boolean hasFixed()
  {
    return this._useFixed;
  }
  
  public void setFixed(double fixedValue)
  {
    this._fixed = fixedValue;
    this._useFixed = true;
  }
  
  public void setMinExclusive(double minValue)
  {
    this._minExclusive = minValue;
    this._useMinExclusive = true;
  }
  
  public void setMinInclusive(double minValue)
  {
    this._minInclusive = minValue;
    this._useMinInclusive = true;
  }
  
  public void setMaxExclusive(double maxValue)
  {
    this._maxExclusive = maxValue;
    this._useMaxExclusive = true;
  }
  
  public void setMaxInclusive(double maxValue)
  {
    this._maxInclusive = maxValue;
    this._useMaxInclusive = true;
  }
  
  public void validate(double d, ValidationContext context)
    throws ValidationException
  {
    if ((this._useFixed) && (d != this._fixed))
    {
      String err = "double " + d + " is not equal to the fixed value: " + this._fixed;
      throw new ValidationException(err);
    }
    if ((this._useMinInclusive) && (d < this._minInclusive))
    {
      String err = "double " + d + " is less than the minimum allowed value: " + this._minInclusive;
      
      throw new ValidationException(err);
    }
    if ((this._useMinExclusive) && (d <= this._minExclusive))
    {
      String err = "double " + d + " is less than or equal to the maximum exclusive value: " + this._minExclusive;
      
      throw new ValidationException(err);
    }
    if ((this._useMaxInclusive) && (d > this._maxInclusive))
    {
      String err = "double " + d + " is greater than the maximum allowed value: " + this._maxInclusive;
      
      throw new ValidationException(err);
    }
    if ((this._useMaxExclusive) && (d >= this._maxExclusive))
    {
      String err = "double " + d + " is greater than or equal to the maximum exclusive value: " + this._maxExclusive;
      
      throw new ValidationException(err);
    }
    if (hasPattern()) {
      super.validate(Double.toString(d), context);
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
      String err = "doubleValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    double value = 0.0D;
    try
    {
      value = new Double(object.toString()).doubleValue();
    }
    catch (Exception ex)
    {
      String err = "Expecting a double, received instead: ";
      err = err + object.getClass().getName();
      throw new ValidationException(err);
    }
    validate(value, context);
  }
}
