package org.exolab.xml.validators;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.exec.xml.InternalContext;
import org.exolab.util.RegExpEvaluator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public abstract class PatternValidator {
  private LinkedList<String> _patterns = new LinkedList();
  private boolean _nillable = false;
  private RegExpEvaluator _regex = null;
  public PatternValidator() {}
  public PatternValidator(String pattern)
  {
    this._patterns.add(pattern);
  }
  public List<String> getPatterns()
  {
    return Collections.unmodifiableList(this._patterns);
  }
  public boolean isNillable()
  {
    return this._nillable;
  }
  public boolean hasPattern()
  {
    return !this._patterns.isEmpty();
  }
  public void setNillable(boolean nillable)
  {
    this._nillable = nillable;
  }
  public void addPattern(String pattern)
  {
    this._patterns.add(pattern);
  }
  public void clearPatterns()
  {
    this._patterns.clear();
  }
  public void validate(String str, ValidationContext context)
    throws ValidationException {
    if (this._patterns.isEmpty()) {
      return;
    }
    if (context == null) {
      throw new IllegalArgumentException("PatternValidator given a null context");
    }
    if (this._regex == null) {
      initEvaluator(context);
    }
    for (String pattern : this._patterns) {
      this._regex.setExpression(pattern);
      if (this._regex.matches(str)) {
        return;
      }
    }
    StringBuffer buff = new StringBuffer("'" + str + "'");
    if (this._patterns.size() == 1) {
      buff.append(" does not match the required regular expression: ");
      buff.append("\"");
      buff.append((String)this._patterns.getFirst());
      buff.append("\"");
    }
    else {
      buff.append(" does not match any of the following regular expressions: ");
      for (String pattern : this._patterns) {
        buff.append("\"");
        buff.append(pattern);
        buff.append("\"");
      }
    }
    throw new ValidationException(buff.toString());
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if (object == null) {
      if (!this._nillable) {
        String err = "PatternValidator cannot validate a null object.";
        throw new ValidationException(err);
      }
      return;
    }
    validate(object.toString(), context);
  }
  private void initEvaluator(ValidationContext context) {
    this._regex = context.getInternalContext().getRegExpEvaluator();
    if ((this._regex == null) && (hasPattern())) {
      throw new IllegalStateException("You are trying to use regular expressions without having specified a regular expression evaluator. Please use the exec.properties file to define such.");
    }
  }

    public abstract void setWhiteSpace(String preserve);
}
