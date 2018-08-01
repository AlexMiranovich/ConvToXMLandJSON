package org.exolab.xml.wls8;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import org.exolab.xml.OutputFormat;
import org.exolab.xml.Serializer;
import org.xml.sax.DocumentHandler;

public class WeblogicXercesSerializer extends WeblogicXercesImplementation implements Serializer {
  private static Class _serializerClass;
  private static Method _asDocumentHandler;
  private static Method _setOutputByteStream;
  private static Method _setOutputCharStream;
  private static Method _setOutputFormat;
  private Object _serializer;
  static {
    Class weblogicOutputFormat = null;
    try {
      _serializerClass = Class.forName("weblogic.apache.xml.serialize.XMLSerializer");
      weblogicOutputFormat = Class.forName("weblogic.apache.xml.serialize.OutputFormat");
    } catch (ClassNotFoundException e) {
      handleStaticInitException(e);
    }
    _asDocumentHandler = getMethod(_serializerClass, "asDocumentHandler", new Class[0]);
    
    Class[] parameterOutputStream = { OutputStream.class };
    _setOutputByteStream = getMethod(_serializerClass, "setOutputByteStream", parameterOutputStream);
    
    Class[] parameterWriter = { Writer.class };
    _setOutputCharStream = getMethod(_serializerClass, "setOutputCharStream", parameterWriter);
    
    Class[] parameterOutputFormat = { weblogicOutputFormat };
    _setOutputFormat = getMethod(_serializerClass, "setOutputFormat", parameterOutputFormat);
  }
  public WeblogicXercesSerializer() {
    try {
      this._serializer = _serializerClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e.toString());
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException(e.toString());
    }
  }
  public DocumentHandler asDocumentHandler() throws IOException {
    return (DocumentHandler)invoke(_asDocumentHandler, new Object[0]);
  }
  public void setOutputByteStream(OutputStream output) {
    Object[] params = { output };
    invoke(_setOutputByteStream, params);
  }
  public void setOutputCharStream(Writer out) {
    Object[] params = { out };
    invoke(_setOutputCharStream, params);
  }
  public void setOutputFormat(OutputFormat format) {
    Object[] params = { format.getFormat() };
    invoke(_setOutputFormat, params);
  }
  private Object invoke(Method method, Object[] params)
  {
    return invoke(this._serializer, method, params);
  }
}
