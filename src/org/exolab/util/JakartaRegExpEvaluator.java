package org.exolab.util;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.exec.core.util.Messages;

public class JakartaRegExpEvaluator implements RegExpEvaluator {
  private static final String BOL = "^(";
  private static final String EOL = ")$";
  private RE _regexp;
  public void setExpression(String rexpr) {
    if (rexpr != null) {
      try {
        this._regexp = new RE("^(" + rexpr + ")$");
      } catch (RESyntaxException ex) {
        String message = Messages.format("regexp.eval.error", rexpr);
        IllegalArgumentException iae = new IllegalArgumentException(message);
        iae.initCause(ex);
        throw iae;
      }
    } else {
      this._regexp = null;
    }
  }
  public boolean matches(String value) {
    if (this._regexp != null) {
      return this._regexp.match(value);
    }
    return true;
  }
}
