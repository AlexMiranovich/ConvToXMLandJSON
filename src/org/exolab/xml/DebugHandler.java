package org.exolab.xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class DebugHandler
  implements DocumentHandler
{
  private Writer _out = null;
  private DocumentHandler _handler = null;
  
  public DebugHandler(DocumentHandler handler)
  {
    this(handler, null);
  }
  
  public DebugHandler(DocumentHandler handler, Writer out)
  {
    if (out == null) {
      this._out = new PrintWriter(System.out);
    }
    this._handler = handler;
  }
  
  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    try
    {
      this._out.write(ch, start, length);
      this._out.flush();
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    if (this._handler != null) {
      this._handler.characters(ch, start, length);
    }
  }
  
  public void endDocument()
    throws SAXException
  {
    try
    {
      this._out.write("#endDocument\n");
      this._out.flush();
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    if (this._handler != null) {
      this._handler.endDocument();
    }
  }
  
  public void endElement(String name)
    throws SAXException
  {
    try
    {
      this._out.write("</");
      this._out.write(name);
      this._out.write(">\n");
      this._out.flush();
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    if (this._handler != null) {
      this._handler.endElement(name);
    }
  }
  
  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException
  {
    if (this._handler != null) {
      this._handler.ignorableWhitespace(ch, start, length);
    }
  }
  
  public void processingInstruction(String target, String data)
    throws SAXException
  {
    try
    {
      this._out.write("--#processingInstruction\n");
      this._out.write("target: ");
      this._out.write(target);
      this._out.write(" data: ");
      this._out.write(data);
      this._out.write(10);
      this._out.flush();
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    if (this._handler != null) {
      this._handler.processingInstruction(target, data);
    }
  }
  
  public void setDocumentLocator(Locator locator)
  {
    if (this._handler != null) {
      this._handler.setDocumentLocator(locator);
    }
  }
  
  public void startDocument()
    throws SAXException
  {
    try
    {
      this._out.write("#startDocument\n");
      this._out.flush();
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    if (this._handler != null) {
      this._handler.startDocument();
    }
  }
  
  public void startElement(String name, AttributeList atts)
    throws SAXException
  {
    try
    {
      this._out.write(60);
      this._out.write(name);
      if ((atts != null) && (atts.getLength() > 0)) {
        for (int i = 0; i < atts.getLength(); i++)
        {
          this._out.write(32);
          this._out.write(atts.getName(i));
          this._out.write("=\"");
          this._out.write(atts.getValue(i));
          this._out.write("\"");
        }
      }
      this._out.write(">\n");
      this._out.flush();
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    if (this._handler != null) {
      this._handler.startElement(name, atts);
    }
  }
}
