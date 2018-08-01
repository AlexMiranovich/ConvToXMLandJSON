package org.exolab.net;

import java.io.IOException;
import java.io.Reader;

public abstract class URILocation {
  public abstract String getAbsoluteURI();
  public abstract String getBaseURI();
  public abstract Reader getReader()
    throws IOException;
  public abstract String getRelativeURI();
  public String toString()
  {
    return getAbsoluteURI();
  }
}
