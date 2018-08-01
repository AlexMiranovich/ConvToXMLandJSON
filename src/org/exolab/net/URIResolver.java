package org.exolab.net;

public interface URIResolver {
  URILocation resolve(String paramString1, String paramString2)
    throws URIException;
  URILocation resolveURN(String paramString)
    throws URIException;
}
