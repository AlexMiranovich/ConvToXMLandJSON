package org.exolab.xml;

import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;

public interface EventProducer {
   void setDocumentHandler(DocumentHandler paramDocumentHandler);
   void start() throws SAXException;
}
