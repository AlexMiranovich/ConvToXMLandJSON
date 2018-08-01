package org.exolab.xml.validators;

import java.text.ParseException;
import org.exolab.types.Duration;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class DurationValidator extends PatternValidator implements TypeValidator {
  private Duration _maxInclusive;
  private Duration _maxExclusive;
  private Duration _minInclusive;
  private Duration _minExclusive;
  private Duration _fixed;
  public void clearFixed()
  {
    this._fixed = null;
  }
  public void clearMax() {
    this._maxInclusive = null;
    this._maxExclusive = null;
  }
  public void clearMin() {
    this._minInclusive = null;
    this._minExclusive = null;
  }
  public Duration getFixed()
  {
    return this._fixed;
  }
  public Duration getMaxInclusive()
  {
    return this._maxInclusive;
  }
  public Duration getMaxExclusive()
  {
    return this._maxExclusive;
  }
  public Duration getMinInclusive()
  {
    return this._minInclusive;
  }
  public Duration getMinExclusive()
  {
    return this._minExclusive;
  }
  public boolean hasFixed()
  {
    return this._fixed != null;
  }
  public void setFixed(Duration fixedValue)
  {
    this._fixed = fixedValue;
  }
  public void setMinExclusive(Duration minValue) {
    this._minExclusive = minValue;
    this._minInclusive = null;
  }
  public void setMinInclusive(Duration minValue) {
    this._minInclusive = minValue;
    this._minExclusive = null;
  }
  public void setMaxExclusive(Duration maxValue) {
    this._maxExclusive = maxValue;
    this._maxInclusive = null;
  }
  public void setMaxInclusive(Duration maxValue) {
    this._maxInclusive = maxValue;
    this._maxExclusive = null;
  }
  public void validate(Duration duration)
    throws ValidationException {
    validate(duration, (ValidationContext)null);
  }
  public void validate(Duration duration, ValidationContext context)
    throws ValidationException {
    boolean isThereMinInclusive = this._minInclusive != null;
    boolean isThereMinExclusive = this._minExclusive != null;
    boolean isThereMaxInclusive = this._maxInclusive != null;
    boolean isThereMaxExclusive = this._maxExclusive != null;
    if ((isThereMinExclusive) && (isThereMinInclusive)) {
      throw new ValidationException("Both minInclusive and minExclusive are defined");
    }
    if ((isThereMaxExclusive) && (isThereMaxInclusive)) {
      throw new ValidationException("Both maxInclusive and maxExclusive are defined");
    }
    if ((this._fixed != null) && (!duration.equal(this._fixed)))
    {
      String err = "Duration " + duration + " is not equal to the fixed value: " + this._fixed;
      throw new ValidationException(err);
    }
    if ((isThereMinInclusive) && (this._minInclusive.isGreater(duration)))
    {
      String err = "Duration " + duration + " is less than the minimum allowed value: " + this._minInclusive;
      
      throw new ValidationException(err);
    }
    if ((isThereMinExclusive) && ((this._minExclusive.isGreater(duration)) || (duration.equals(this._minExclusive))))
    {
      String err = "Duration " + duration + " is less than or equal to the minimum exclusive value: " + this._minExclusive;
      
      throw new ValidationException(err);
    }
    if ((isThereMaxInclusive) && (duration.isGreater(this._maxInclusive)))
    {
      String err = "Duration " + duration + " is greater than the maximum allowed value " + this._maxInclusive;
      
      throw new ValidationException(err);
    }
    if ((isThereMaxExclusive) && ((duration.isGreater(this._maxExclusive)) || (duration.equals(this._maxExclusive))))
    {
      String err = "Duration " + duration + " is greater than or equal to the maximum exclusive value: " + this._maxExclusive;
      
      throw new ValidationException(err);
    }
    if (hasPattern()) {
      super.validate(duration.toString(), context);
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
      String err = "durationValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    if ((object instanceof String)) {
      try
      {
        Duration duration = Duration.parseDuration((String)object);
        validate(duration, context);
        return;
      }
      catch (ParseException pe)
      {
        String err = "String provided fails to parse into a Duration: " + (String)object;
        throw new ValidationException(err, pe);
      }
    }
    Duration value = null;
    try
    {
      value = (Duration)object;
    }
    catch (Exception ex)
    {
      String err = "Expecting a duration, received instead: " + object.getClass().getName();
      throw new ValidationException(err);
    }
    validate(value, context);
  }
}
