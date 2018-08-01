package org.exolab.mapping;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.core.util.Messages;
import org.exec.mapping.MappingSource;
import org.exolab.mapping.xml.MappingRoot;
import org.exolab.net.util.URIUtils;
import org.exolab.util.DTDResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class Mapping {
  private static final Log LOG = LogFactory.getLog(Mapping.class);
  private static final String DEFAULT_SOURCE_TYPE = "ExecXmlMapping";
  private final List _mappings = new ArrayList();
  private final Set _processed = new HashSet();
  private final MappingRoot _root = new MappingRoot();
  private final ClassLoader _classLoader;
  private DTDResolver _resolver = new DTDResolver();
  public Mapping(ClassLoader loader) {
    if (loader == null) {
      this._classLoader = getClass().getClassLoader();
    } else {
      this._classLoader = loader;
    }
  }
  public Mapping()
  {
    this(null);
  }
  public List getMappingSources()
    throws MappingException {
    return Collections.unmodifiableList(this._mappings);
  }
  public void markAsProcessed(Object id)
  {
    this._processed.add(id);
  }
  public boolean processed(Object id)
  {
    return this._processed.contains(id);
  }
  public MappingRoot getRoot()
  {
    return this._root;
  }
  public ClassLoader getClassLoader()
  {
    return this._classLoader;
  }
  public void setEntityResolver(EntityResolver resolver)
  {
    this._resolver = new DTDResolver(resolver);
  }
  public void setBaseURL(String url) {
    String location = url;
    if (location != null) {
      int idx = location.lastIndexOf('/');
      if (idx < 0) {
        idx = location.lastIndexOf('\\');
      }
      if (idx >= 0) {
        int extIdx = location.indexOf('.', idx);
        if (extIdx > 0) {
          location = location.substring(0, idx);
        }
      }
    }
    try {
      this._resolver.setBaseURL(new URL(location));
    }
    catch (MalformedURLException except) {
      try {
        LOG.info(Messages.format("mapping.wrongURL", location));
        this._resolver.setBaseURL(new URL("file", null, location));
      }
      catch (MalformedURLException except2) {}
    }
  }
  public void loadMapping(String url)
    throws IOException, MappingException {
    loadMapping(url, "ExecXmlMapping");
  }
  public void loadMapping(String url, String type)
    throws IOException, MappingException {
    String location = url;
    if (this._resolver.getBaseURL() == null) {
      setBaseURL(location);
      location = URIUtils.getRelativeURI(location);
    }
    try {
      InputSource source = this._resolver.resolveEntity(null, location);
      if (source == null) {
        source = new InputSource(location);
      }
      if (source.getSystemId() == null) {
        source.setSystemId(location);
      }
      LOG.info(Messages.format("mapping.loadingFrom", location));
      loadMapping(source, type);
    }
    catch (SAXException ex) {
      throw new MappingException(ex);
    }
  }
  public void loadMapping(URL url)
    throws IOException, MappingException {
    loadMapping(url, "ExecXmlMapping");
  }
  public void loadMapping(URL url, String type)
    throws IOException, MappingException {
    try {
      if (this._resolver.getBaseURL() == null) {
        this._resolver.setBaseURL(url);
      }
      InputSource source = this._resolver.resolveEntity(null, url.toExternalForm());
      if (source == null) {
        source = new InputSource(url.toExternalForm());
        source.setByteStream(url.openStream());
      }
      else {
        source.setSystemId(url.toExternalForm());
      }
      LOG.info(Messages.format("mapping.loadingFrom", url.toExternalForm()));
      loadMapping(source, type);
    }
    catch (SAXException ex) {
      throw new MappingException(ex);
    }
  }
  public void loadMapping(InputSource source)
  {
    loadMapping(source, "ExecXmlMapping");
  }
  public void loadMapping(InputSource source, String type) {
    this._mappings.add(new MappingSource(source, type, this._resolver));
  }
}
