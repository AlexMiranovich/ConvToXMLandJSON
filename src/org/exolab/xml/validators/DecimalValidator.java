package org.exolab.xml.validators;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class DecimalValidator extends PatternValidator implements TypeValidator {
  private static Method _bdMethodToPlainString = null;
  static {
    try {
      _bdMethodToPlainString = BigDecimal.class.getMethod("toPlainString", (Class[])null);
    }
    catch (NoSuchMethodException e) {}
  }
  private BigDecimal _fixed = null;
  private BigDecimal _min = null;
  private BigDecimal _max = null;
  private int _totalDigits = -1;
  private int _fractionDigits = -1;
  private boolean _hasMinExclusive = false;
  private boolean _hasMaxExclusive = false;
  public void clearFixed()
  {
    this._fixed = null;
  }
  public void clearMax() {
    this._max = null;
    this._hasMaxExclusive = false;
  }
  public void clearMin() {
    this._min = null;
    this._hasMinExclusive = false;
  }
  public BigDecimal getFixed()
  {
    return this._fixed;
  }
  public BigDecimal getMaxInclusive()
  {
    return this._hasMaxExclusive ? null : this._max;
  }
  public BigDecimal getMaxExclusive()
  {
    return this._hasMaxExclusive ? this._max : null;
  }
  public BigDecimal getMinInclusive()
  {
    return this._hasMinExclusive ? null : this._min;
  }
  public BigDecimal getMinExclusive()
  {
    return this._hasMinExclusive ? this._min : null;
  }
  public boolean hasFixed()
  {
    return this._fixed != null;
  }
  public void setFixed(BigDecimal fixedValue)
  {
    this._fixed = fixedValue;
  }
  public void setMinExclusive(BigDecimal minValue) {
    if (minValue == null) {
      throw new IllegalArgumentException("argument 'minValue' must not be null.");
    }
    this._min = minValue;
    this._hasMinExclusive = true;
  }
  public void setMinInclusive(BigDecimal minValue) {
    this._min = minValue;
    this._hasMinExclusive = false;
  }
  public void setMaxExclusive(BigDecimal maxValue) {
    if (maxValue == null) {
      throw new IllegalArgumentException("argument 'maxValue' must not be null.");
    }
    this._max = maxValue;
    this._hasMaxExclusive = true;
  }
  public void setMaxInclusive(BigDecimal maxValue) {
    this._max = maxValue;
    this._hasMaxExclusive = false;
  }
  public void setTotalDigits(int totalDig) {
    if (totalDig <= 0) {
      throw new IllegalArgumentException("DecimalValidator: the totalDigits facet must be positive");
    }
    this._totalDigits = totalDig;
  }
  public void setFractionDigits(int fractionDig) {
    if (fractionDig < 0) {
      throw new IllegalArgumentException("DecimalValidator: the fractionDigits facet must be positive");
    }
    this._fractionDigits = fractionDig;
  }
  public void validate(BigDecimal bd, ValidationContext context)
    throws ValidationException {
    if ((this._fixed != null) && (!bd.equals(this._fixed))) {
      String err = "BigDecimal " + bd + " is not equal to the fixed value: " + this._fixed;
      throw new ValidationException(err);
    }
    if (this._min != null) {
      if (bd.compareTo(this._min) == -1) {
        String err = "BigDecimal " + bd + " is less than the minimum allowed value: " + this._min;
        throw new ValidationException(err);
      }
      if ((bd.compareTo(this._min) == 0) && (this._hasMinExclusive)) {
        String err = "BigDecimal " + bd + " cannot be equal to the minimum exclusive value: " + this._min;
        throw new ValidationException(err);
      }
    }
    if (this._max != null) {
      if (bd.compareTo(this._max) == 1) {
        String err = "BigDecimal " + bd + " is greater than the maximum allowed value: " + this._max;
        throw new ValidationException(err);
      }
      if ((bd.compareTo(this._max) == 0) && (this._hasMaxExclusive)) {
        String err = "BigDecimal " + bd + " cannot be equal to the maximum exclusive value: " + this._max;
        throw new ValidationException(err);
      }
    }
    BigDecimal clean = stripTrailingZeros(bd);
    if (this._totalDigits != -1) {
      String temp = toStringForBigDecimal(clean);
      int length = temp.length();
      if (temp.indexOf('-') == 0) {
        length--;
      }
      if (temp.indexOf('.') != -1) {
        length--;
      }
      if (length > this._totalDigits) {
        String err = "BigDecimal " + bd + " has too many significant digits -- must have " + this._totalDigits + " or fewer";
        throw new ValidationException(err);
      }
      temp = null;
    }
    if ((this._fractionDigits != -1) && (clean.scale() > this._fractionDigits)) {
      String err = "BigDecimal " + bd + " has too many fraction digits -- must have " + this._fractionDigits + " fraction digits or fewer";
      throw new ValidationException(err);
    }
    if (hasPattern()) {
      super.validate(toStringForBigDecimal(bd), context);
    }
  }
  public void validate(Object object)
    throws ValidationException {
    validate(object, (ValidationContext)null);
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if (object == null) {
      String err = "decimalValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    BigDecimal value = null;
    if ((object instanceof BigDecimal)) {
      value = (BigDecimal)object; } else {
      try {
        value = new BigDecimal(object.toString());
      }
      catch (Exception ex) {
        String err = "Expecting a decimal, received instead: " + object.getClass().getName();
        throw new ValidationException(err);
      }
    }
    validate(value, context);
  }
  private String toStringForBigDecimal(BigDecimal bd) {
    if (_bdMethodToPlainString != null) {
      try {
        return (String)_bdMethodToPlainString.invoke(bd, (Object[])null);
      }
      catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}
    }
    return bd.toString();
  }
  private BigDecimal stripTrailingZeros(BigDecimal bd) {
    BigDecimal ret = null;
    try {
      for (int i = bd.scale(); i >= 0; i--) {
        ret = bd.setScale(i);
      }
    }
    catch (ArithmeticException e) {}
    return ret == null ? bd : ret;
  }
}
