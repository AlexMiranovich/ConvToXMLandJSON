package org.exolab.xml.validators;

import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class FloatValidator extends PatternValidator implements TypeValidator {
  private boolean _useMinInclusive = false;
  private boolean _useMinExclusive = false;
  private boolean _useMaxInclusive = false;
  private boolean _useMaxExclusive = false;
  private boolean _useFixed = false;
  private float _minInclusive = 0.0F;
  private float _minExclusive = 0.0F;
  private float _maxInclusive = 0.0F;
  private float _maxExclusive = 0.0F;
  private float _fixed = 0.0F;
  
  public void clearFixed()
  {
    this._useFixed = false;
  }
  
  public void clearMax()
  {
    this._useMaxExclusive = false;
    this._useMaxInclusive = false;
  }
  
  public void clearMin()
  {
    this._useMinExclusive = false;
    this._useMinInclusive = false;
  }
  
  public Float getFixed()
  {
    if (this._useFixed) {
      return new Float(this._fixed);
    }
    return null;
  }
  
  public Float getMaxInclusive()
  {
    if (this._useMaxInclusive) {
      return new Float(this._maxInclusive);
    }
    return null;
  }
  
  public Float getMaxExclusive()
  {
    if (this._useMaxExclusive) {
      return new Float(this._maxExclusive);
    }
    return null;
  }
  
  public Float getMinInclusive()
  {
    if (this._useMinInclusive) {
      return new Float(this._minInclusive);
    }
    return null;
  }
  
  public Float getMinExclusive()
  {
    if (this._useMinExclusive) {
      return new Float(this._minExclusive);
    }
    return null;
  }
  
  public boolean hasFixed()
  {
    return this._useFixed;
  }
  
  public void setFixed(float fixedValue)
  {
    this._fixed = fixedValue;
    this._useFixed = true;
  }
  
  public void setMinExclusive(float minValue)
  {
    this._minExclusive = minValue;
    this._useMinExclusive = true;
  }
  
  public void setMinInclusive(float minValue)
  {
    this._minInclusive = minValue;
    this._useMinInclusive = true;
  }
  
  public void setMaxExclusive(float maxValue)
  {
    this._maxExclusive = maxValue;
    this._useMaxExclusive = true;
  }
  
  public void setMaxInclusive(float maxValue)
  {
    this._maxInclusive = maxValue;
    this._useMaxInclusive = true;
  }
  
  public void validate(float d, ValidationContext context)
    throws ValidationException
  {
    if ((this._useFixed) && (d != this._fixed))
    {
      String err = "float " + d + " is not equal to the fixed value of " + this._fixed;
      throw new ValidationException(err);
    }
    if ((this._useMinInclusive) && (d < this._minInclusive))
    {
      String err = "float " + d + " is less than the minimum allowed value: " + this._minInclusive;
      throw new ValidationException(err);
    }
    if ((this._useMinExclusive) && (d <= this._minExclusive))
    {
      String err = "float " + d + " is less than or equal to the minimum exclusive value: " + this._minExclusive;
      
      throw new ValidationException(err);
    }
    if ((this._useMaxInclusive) && (d > this._maxInclusive))
    {
      String err = "float " + d + " is greater than the maximum allowed value: " + this._maxInclusive;
      
      throw new ValidationException(err);
    }
    if ((this._useMaxExclusive) && (d >= this._maxExclusive))
    {
      String err = "float " + d + " is greater than or equal to the maximum exclusive value: " + this._maxExclusive;
      
      throw new ValidationException(err);
    }
    if (hasPattern()) {
      super.validate(Float.toString(d), context);
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
      String err = "floatValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    float value = 0.0F;
    try
    {
      value = new Float(object.toString()).floatValue();
    }
    catch (Exception ex)
    {
      String err = "Expecting a float, received instead: ";
      err = err + object.getClass().getName();
      throw new ValidationException(err);
    }
    validate(value, context);
  }
}
