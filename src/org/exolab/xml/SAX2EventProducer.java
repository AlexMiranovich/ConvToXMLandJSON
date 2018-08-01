package org.exolab.xml;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public interface SAX2EventProducer {
  void setContentHandler(ContentHandler paramContentHandler);
  void start()
    throws SAXException;
}
