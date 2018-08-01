package org.exolab.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.core.util.Messages;
import org.xml.sax.DocumentHandler;

public class XercesSerializer implements Serializer {
  private static final Log LOG = LogFactory.getLog(XercesSerializer.class);
  private Object _serializer;
  public XercesSerializer() {
    try {
      this._serializer = Class.forName("org.apache.xml.serialize.XMLSerializer").newInstance();
    } catch (Exception except) {
      throw new RuntimeException(Messages.format("conf.failedInstantiateSerializer", "org.apache.xml.serialize.XMLSerializer", except));
    }
  }
  public void setOutputCharStream(Writer out) {
    try {
      Method method = this._serializer.getClass().getMethod("setOutputCharStream", new Class[] { Writer.class });
      method.invoke(this._serializer, new Object[] { out });
    } catch (Exception e) {
      String msg = "Problem invoking XMLSerializer.setOutputCharStream()";
      LOG.error(msg, e);
      throw new RuntimeException(msg + e.getMessage());
    }
  }
  public DocumentHandler asDocumentHandler()
    throws IOException {
    try {
      Method method = this._serializer.getClass().getMethod("asDocumentHandler", (Class[])null);
      return (DocumentHandler)method.invoke(this._serializer, (Object[])null);
    } catch (Exception e) {
      String msg = "Problem invoking XMLSerializer.asDocumentHandler()";
      LOG.error(msg, e);
      throw new RuntimeException(msg + e.getMessage());
    }
  }
  public void setOutputFormat(OutputFormat format) {
    try {
      Class outputFormatClass = Class.forName("org.apache.xml.serialize.OutputFormat");
      Method method = this._serializer.getClass().getMethod("setOutputFormat", new Class[] { outputFormatClass });
      method.invoke(this._serializer, new Object[] { format.getFormat() });
    } catch (Exception e) {
      String msg = "Problem invoking XMLSerializer.setOutputFormat()";
      LOG.error(msg, e);
      throw new RuntimeException(msg + e.getMessage());
    }
  }
  public void setOutputByteStream(OutputStream output) {
    try {
      Method method = this._serializer.getClass().getMethod("setOutputByteStream", new Class[] { OutputStream.class });
      method.invoke(this._serializer, new Object[] { output });
    } catch (Exception e) {
      String msg = "Problem invoking XMLSerializer.setOutputByteStream()";
      LOG.error(msg, e);
      throw new RuntimeException(msg + e.getMessage());
    }
  }
}
