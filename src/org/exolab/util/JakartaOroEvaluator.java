package org.exolab.util;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Pattern;

public class JakartaOroEvaluator implements RegExpEvaluator {
  private static final String BOL = "^";
  private static final String EOL = "$";
  private Perl5Pattern _regexp = null;
  public void setExpression(String rexpr) {
    if (rexpr != null) {
      try {
        Perl5Compiler compiler = new Perl5Compiler();
        this._regexp = ((Perl5Pattern)compiler.compile("^" + rexpr + "$", 16));
      } catch (MalformedPatternException ex) {
        String err = "RegExp Syntax error: ";
        err = err + ex.getMessage();
        err = err + " ; error occured with the following regular expression: " + rexpr;
        throw new IllegalArgumentException(err);
      }
    } else {
      this._regexp = null;
    }
  }
  public boolean matches(String value) {
    Perl5Matcher matcher = new Perl5Matcher();
    return matcher.contains(value, this._regexp);
  }
}
