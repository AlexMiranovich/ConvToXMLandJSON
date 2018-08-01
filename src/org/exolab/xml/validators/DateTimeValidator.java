package org.exolab.xml.validators;

import java.text.ParseException;
import org.exolab.types.DateTime;
import org.exolab.types.DateTimeBase;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class DateTimeValidator extends PatternValidator implements TypeValidator {
  private DateTimeBase _maxInclusive;
  private DateTimeBase _maxExclusive;
  private DateTimeBase _minInclusive;
  private DateTimeBase _minExclusive;
  private DateTimeBase _fixed;
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
  public DateTimeBase getFixed()
  {
    return this._fixed;
  }
  public DateTimeBase getMaxInclusive()
  {
    return this._maxInclusive;
  }
  public DateTimeBase getMaxExclusive()
  {
    return this._maxExclusive;
  }
  public DateTimeBase getMinInclusive()
  {
    return this._minInclusive;
  }
  public DateTimeBase getMinExclusive()
  {
    return this._minExclusive;
  }
  public boolean hasFixed()
  {
    return this._fixed != null;
  }
  public void setFixed(DateTimeBase fixedValue)
  {
    this._fixed = fixedValue;
  }
  public void setMinExclusive(DateTimeBase minValue) {
    this._minExclusive = minValue;
    this._minInclusive = null;
  }
  public void setMinInclusive(DateTimeBase minValue) {
    this._minExclusive = null;
    this._minInclusive = minValue;
  }
  public void setMaxExclusive(DateTimeBase maxValue) {
    this._maxExclusive = maxValue;
    this._maxInclusive = null;
  }
  public void setMaxInclusive(DateTimeBase maxValue) {
    this._maxExclusive = null;
    this._maxInclusive = maxValue;
  }
  public void validate(DateTimeBase dateTime)
    throws ValidationException {
    validate(dateTime, (ValidationContext)null);
  }
  public void validate(DateTimeBase dateTime, ValidationContext context)
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
    if (this._fixed != null) {
      int comparison = dateTime.compareTo(this._fixed);
      if (comparison == -1) {
        String err = dateTime.getClass().getName() + " " + dateTime + " comparison to the fixed value " + this._fixed + " is indeterminate";
        
        throw new ValidationException(err);
      }
      if (comparison != 1) {
        String err = dateTime.getClass().getName() + " " + dateTime + " is not equal to the fixed value: " + this._fixed;
        
        throw new ValidationException(err);
      }
    }
    if ((isThereMinInclusive) && (dateTime.compareTo(this._minInclusive) != 2) && (!dateTime.equals(this._minInclusive))) {
      String err = dateTime.getClass().getName() + " " + dateTime + " is less than the minimum allowed value: " + this._minInclusive;
      throw new ValidationException(err);
    }
    if ((isThereMinExclusive) && (dateTime.compareTo(this._minExclusive) != 2)) {
      String err = dateTime.getClass().getName() + " " + dateTime + " is less than or equal to the minimum (exclusive) value: " + this._minExclusive;
      throw new ValidationException(err);
    }
    if ((isThereMaxInclusive) && (dateTime.compareTo(this._maxInclusive) != 0) && (!dateTime.equals(this._maxInclusive))) {
      String err = dateTime.getClass().getName() + " " + dateTime + " is greater than the maximum allowed value: " + this._maxInclusive;
      throw new ValidationException(err);
    }
    if ((isThereMaxExclusive) && (dateTime.compareTo(this._maxExclusive) != 0)) {
      String err = dateTime.getClass().getName() + " " + dateTime + " is greater than or equal to the maximum (exclusive) value: " + this._maxExclusive;
      throw new ValidationException(err);
    }
    if (hasPattern()) {
      super.validate(dateTime.toString(), context);
    }
  }
  public void validate(Object object)
    throws ValidationException {
    validate(object, (ValidationContext)null);
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if (object == null) {
      String err = "DateTimeValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    if ((object instanceof String)) {
      try {
        DateTime dateTime = new DateTime((String)object);
        validate(dateTime, context);
        return;
      }
      catch (ParseException pe) {
        String err = "String provided fails to parse into a DateTime: " + (String)object;
        throw new ValidationException(err, pe);
      }
    }
    DateTimeBase value = null;
    try {
      value = (DateTimeBase)object;
    }
    catch (Exception ex) {
      String err = ex.toString() + "\nExpecting a DateTime, received instead: " + object.getClass().getName();
      throw new ValidationException(err);
    }
    validate(value, context);
  }
}
