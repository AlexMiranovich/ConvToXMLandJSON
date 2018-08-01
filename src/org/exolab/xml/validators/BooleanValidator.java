package org.exolab.xml.validators;

import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class BooleanValidator extends PatternValidator implements TypeValidator {
  private boolean _useFixed = false;
  private boolean _fixed = false;
  public void clearFixed()
  {
    this._useFixed = false;
  }
  public Boolean getFixed() {
    if (this._useFixed) {
      return Boolean.valueOf(this._fixed);
    }
    return null;
  }
  public boolean hasFixed()
  {
    return this._useFixed;
  }
  public void setFixed(boolean fixedValue) {
    this._useFixed = true;
    this._fixed = fixedValue;
  }
  public void setFixed(Boolean fixedValue) {
    this._useFixed = true;
    this._fixed = fixedValue.booleanValue();
  }
  public void validate(boolean b, ValidationContext context)
    throws ValidationException {
    if ((this._useFixed) && (b != this._fixed)) {
      String err = "boolean " + b + " is not equal to the fixed value: " + this._fixed;
      throw new ValidationException(err);
    }
    if (hasPattern()) {
      super.validate(String.valueOf(b), context);
    }
  }
  public void validate(Object object) throws ValidationException {
    validate(object, (ValidationContext)null);
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if (object == null) {
      String err = "BooleanValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    boolean value = false;
    try {
      value = ((Boolean)object).booleanValue();
    } catch (Exception ex) {
      String err = "Expecting a Boolean, received instead: ";
      err = err + object.getClass().getName();
      throw new ValidationException(err);
    }
    validate(value, context);
  }
}
