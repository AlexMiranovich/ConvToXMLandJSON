package org.exolab.xml;

import org.exec.xml.BackwardCompatibilityContext;
import org.exec.xml.XMLNaming;

public abstract class AbstractXMLNaming implements XMLNaming {
  public abstract String createXMLName(Class paramClass);
  public abstract String toXMLName(String paramString);
  public static final XMLNaming getInstance()
  {
    return new BackwardCompatibilityContext().getXMLNaming();
  }
}
