package org.exolab.net.util;

import java.io.File;
import org.exolab.net.URIException;
import org.exolab.net.URILocation;
import org.exolab.net.URIResolver;

public final class URIResolverImpl implements URIResolver {
  private static final String HTTP_PROTOCOL = "http:";
  private static final String FILE_PROTOCOL = "file:";
  public URILocation resolve(String href, String documentBase) throws URIException {
    URILocation uriLocation = null;
    boolean stripHostSeparator = File.separatorChar == '\\';
    boolean absolute = false;
    if ((href.startsWith("http:")) || (href.startsWith("file:"))) {
      absolute = true;
    }if (!absolute) {
      if (stripHostSeparator) {
        if (href.startsWith("//")) {
          absolute = true;
        }
      }
      else if (href.startsWith("///")) {
        absolute = true;
      }
    }if (!absolute) {
      if (href.startsWith("./")) {
        href = href.substring(2);
      }if (documentBase != null) {
        while (href.startsWith("../")) {
          href = href.substring(3);
          documentBase = documentBase.substring(0, documentBase.lastIndexOf('/'));
          documentBase = documentBase.substring(0, documentBase.lastIndexOf('/') + 1);
        }
      }
    }try {
      uriLocation = new URILocationImpl(href, documentBase);
    } catch (RuntimeException ex) {
      throw new URIException(ex.getMessage(), ex);
    }
    return uriLocation;
  }
  public URILocation resolveURN(String urn) throws URIException {
    return null;
  }
}
