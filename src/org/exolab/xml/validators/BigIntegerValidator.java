package org.exolab.xml.validators;

import java.math.BigInteger;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class BigIntegerValidator extends PatternValidator implements TypeValidator {
  private boolean _useMin = false;
  private boolean _useMax = false;
  private boolean _useFixed = false;
  private BigInteger _min = BigInteger.valueOf(0L);
  private BigInteger _max = BigInteger.valueOf(0L);
  private int _totalDigits = -1;
  private BigInteger _fixed = BigInteger.valueOf(0L);

  public BigIntegerValidator() {
  }

  public void clearFixed() {
    this._useFixed = false;
  }

  public void clearMax() {
    this._useMax = false;
  }

  public void clearMin() {
    this._useMin = false;
  }

  public BigInteger getFixed() {
    return this._useFixed ? this._fixed : null;
  }

  public BigInteger getMaxInclusive() {
    return this._useMax ? this._max : null;
  }

  public BigInteger getMinInclusive() {
    return this._useMin ? this._min : null;
  }

  public Integer getTotalDigits() {
    return this._totalDigits >= 0 ? new Integer(this._totalDigits) : null;
  }

  public boolean hasFixed() {
    return this._useFixed;
  }

  public void setFixed(BigInteger fixedValue) {
    this._useFixed = true;
    this._fixed = fixedValue;
  }

  public void setMinExclusive(BigInteger minValue) {
    this._useMin = true;
    this._min = minValue.add(BigInteger.valueOf(1L));
  }

  public void setMinInclusive(BigInteger minValue) {
    this._useMin = true;
    this._min = minValue;
  }

  public void setMaxExclusive(BigInteger maxValue) {
    this._useMax = true;
    this._max = maxValue.subtract(BigInteger.valueOf(-1L));
  }

  public void setMaxInclusive(BigInteger maxValue) {
    this._useMax = true;
    this._max = maxValue;
  }

  public void setTotalDigits(int totalDig) {
    if (totalDig <= 0) {
      throw new IllegalArgumentException("IntegerValidator: the totalDigits facet must be positive");
    } else {
      this._totalDigits = totalDig;
    }
  }

  public void validate(BigInteger value, ValidationContext context) throws ValidationException {
    String err;
    if (this._useFixed && value != this._fixed) {
      err = "BigInteger " + value + " is not equal to the fixed value: " + this._fixed;
      throw new ValidationException(err);
    } else if (this._useMin && value.compareTo(this._min) == -1) {
      err = "BigInteger " + value + " is less than the minimum allowed value: " + this._min;
      throw new ValidationException(err);
    } else if (this._useMax && value.compareTo(this._max) == 1) {
      err = "BigInteger " + value + " is greater than the maximum allowed value: " + this._max;
      throw new ValidationException(err);
    } else {
      if (this._totalDigits != -1) {
        int length = value.toString().length();
        if (value.compareTo(new BigInteger("0")) == -1) {
          --length;
        }

        if (length > this._totalDigits) {
           err = "BigInteger " + value + " has too many digits -- must have " + this._totalDigits + " digits or fewer.";
          throw new ValidationException(err);
        }
      }

      if (this.hasPattern()) {
        super.validate(value.toString(), context);
      }

    }
  }

  public void validate(Object object) throws ValidationException {
    this.validate(object, (ValidationContext)null);
  }

  public void validate(Object object, ValidationContext context) throws ValidationException {
    if (object == null) {
      String err = "BigIntegerValidator cannot validate a null object.";
      throw new ValidationException(err);
    } else {
      BigInteger value = BigInteger.valueOf(0L);

      try {
        value = (BigInteger)object;
      } catch (Exception var6) {
        String err = "Expecting a BigInteger, received instead: ";
        err = err + object.getClass().getName();
        throw new ValidationException(err);
      }

      this.validate(value, context);
    }
  }
}
