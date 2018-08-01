package org.exolab.xml.util;

import org.exec.xml.XMLNaming;
import org.exolab.xml.AbstractXMLNaming;

public final class DefaultNaming extends AbstractXMLNaming implements XMLNaming {
  public static final short LOWER_CASE_STYLE = 0;
  public static final short MIXED_CASE_STYLE = 1;
  private static short _style = 0;
  public void setStyle(short style) {
    switch (style) {
    case 0: 
    case 1: 
      _style = style;
      break;
    default: 
      throw new IllegalArgumentException("Invalid option for DefaultNaming#setStyle.");
    }
  }
  public String createXMLName(Class c) {
    UnsupportedOperationException e = new UnsupportedOperationException("Method has moved to JavaNaming!");
    throw e;
  }
  public String toXMLName(String name) {
    if (name == null) {
      return null;
    }
    if (name.length() == 0) {
      return name;
    }
    if (name.length() == 1) {
      return name.toLowerCase();
    }
    if ((Character.isUpperCase(name.charAt(0))) && (Character.isUpperCase(name.charAt(1)))) {
      return name;
    }
    StringBuffer cbuff = new StringBuffer(name);
    cbuff.setCharAt(0, Character.toLowerCase(cbuff.charAt(0)));
    boolean ucPrev = false;
    for (int i = 1; i < cbuff.length(); i++) {
      char ch = cbuff.charAt(i);
      if (Character.isUpperCase(ch)) {
        if (!ucPrev) {
          ucPrev = true;
          if (_style == 0) { cbuff.insert(i, '-');
            i++;
            cbuff.setCharAt(i, Character.toLowerCase(ch));
          }
          else {
            i++;
          }
        }
      }
      else if (ch == '.') {
        ucPrev = true;
      } else {
        ucPrev = false;
      }
    }
    return cbuff.toString();
  }

  public void setNaming(XMLNaming xmlNaming) {

  }
}
