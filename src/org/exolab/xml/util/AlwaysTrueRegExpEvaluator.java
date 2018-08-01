package org.exolab.xml.util;

import org.exolab.util.RegExpEvaluator;

public class AlwaysTrueRegExpEvaluator implements RegExpEvaluator {
  public void setExpression(String rexpr) {}
  public boolean matches(String value)
  {
    return true;
  }
}
