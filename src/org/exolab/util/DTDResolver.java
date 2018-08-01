package org.exolab.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.exolab.net.util.URIUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DTDResolver implements EntityResolver {
  private final DTDInfo[] _dtdInfo = new DTDInfo[]{
          new DTDInfo("-//EXOLAB/Exec Mapping DTD Version 1.0//EN",
                  "http://exec.exolab.org/mapping.dtd", "exec.exolab.org",
                  "exec", "/org/exolab/mapping/mapping.dtd"),
          new DTDInfo("-//EXOLAB/Exec Mapping Schema Version 1.0//EN",
                  "http://exec.exolab.org/mapping.xsd", "exec.exolab.org",
                  "exec", "/org/exolab/mapping/mapping.xsd"),
          new DTDInfo("-//EXOLAB/Exec Mapping DTD Version 1.0//EN",
                  "http://exec.org/mapping.dtd", "exec.org", "exec",
                  "/org/exolab/mapping/mapping.dtd"),
          new DTDInfo("-//EXOLAB/Exec Mapping Schema Version 1.0//EN",
                  "http://exec.org/mapping.xsd", "exec.org", "exec ",
                  "/org/exolab/mapping/mapping.xsd"),
          new DTDInfo("-//EXOLAB/Exec JDO Configuration DTD Version 1.0//EN",
                  "http://exec.exolab.org/jdo-conf.dtd", "exec.exolab.org",
                  "exec", "/org/exec/jdo/conf/jdo-conf.dtd"),
          new DTDInfo("-//EXOLAB/exec JDO Configuration Schema Version 1.0//EN",
                  "http://exec.exolab.org/jdo-conf.xsd", "exec.exolab.org",
                  "exec", "/org/exec/jdo/conf/jdo-conf.xsd"),
          new DTDInfo("-//EXOLAB/Exec JDO Configuration DTD Version 1.0//EN",
                  "http://exec.org/jdo-conf.dtd", "exec.org", "exec",
                  "/org/exec/jdo/conf/jdo-conf.dtd"),
          new DTDInfo("-//EXOLAB/Exec JDO Configuration Schema Version 1.0//EN",
                  "http://exec.org/jdo-conf.xsd", "exec.org", "exec",
                  "/org/exec/jdo/conf/jdo-conf.xsd"),
          new DTDInfo("-//W3C//DTD XMLSCHEMA 19991216//EN",
                  "http://www.w3.org/TR/2000/WD-xmlschema-1-20000225/structures.dtd",
                  (String)null, (String)null, "/org/exolab/util/resources/structures.dtd"),
          new DTDInfo((String)null, "http://www.w3.org/TR/2000/WD-xmlschema-2-20000225/datatypes.dtd",
                  (String)null, (String)null, "/org/exolab/util/resources/datatypes.dtd"),
          new DTDInfo((String)null, "http://www.w3.org/TR/2000/WD-xmlschema-1-20000225/structures.xsd",
                  (String)null, (String)null, "/org/exolab/util/resources/structures.xsd")};
  private EntityResolver _resolver;
  private URL _baseUrl;

  public DTDResolver(EntityResolver resolver) {
    this._resolver = resolver;
  }

  public DTDResolver() {
  }

  public void setBaseURL(URL baseUrl) {
    this._baseUrl = baseUrl;
  }

  public URL getBaseURL() {
    return this._baseUrl;
  }

  public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
    InputSource source = null;

    for(int i = 0; i < this._dtdInfo.length; ++i) {
      if (publicId != null && publicId.equals(this._dtdInfo[i].publicId)) {
        source = new InputSource(this.getClass().getResourceAsStream(this._dtdInfo[i].resource));
        source.setPublicId(publicId);
        return source;
      }

      if (systemId != null && systemId.equals(this._dtdInfo[i].systemId)) {
        source = new InputSource(this.getClass().getResourceAsStream(this._dtdInfo[i].resource));
        source.setSystemId(systemId);
        return source;
      }
    }

    if (this._resolver != null) {
      source = this._resolver.resolveEntity(publicId, systemId);
      if (source != null) {
        return source;
      }
    }

    if (systemId != null && this._baseUrl != null) {
      URL url;
      String absURL;
      try {
        url = new URL(this._baseUrl, systemId);
        source = new InputSource(url.openStream());
        source.setSystemId(systemId);
        return source;
      } catch (MalformedURLException var10) {
        try {
          absURL = URIUtils.resolveAsString(systemId, this._baseUrl.toString());
          url = new URL(absURL);
          source = new InputSource(url.openStream());
          source.setSystemId(systemId);
          return source;
        } catch (MalformedURLException var9) {
          ;
        }
      } catch (FileNotFoundException var11) {
        try {
          absURL = URIUtils.resolveAsString(systemId, this._baseUrl.toString());
          url = new URL(absURL);
          source = new InputSource(url.openStream());
          source.setSystemId(systemId);
          return source;
        } catch (MalformedURLException var8) {
          ;
        }
      }

      return null;
    } else {
      return null;
    }
  }

  static class DTDInfo {
    private final String publicId;
    private final String systemId;
    private final String resource;

    DTDInfo(String publicId, String systemId, String namespace, String prefix, String resource) {
      this.publicId = publicId;
      this.systemId = systemId;
      this.resource = resource;
    }
  }
}
