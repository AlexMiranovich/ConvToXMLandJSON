package org.exolab.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.xml.sax.DocumentHandler;

public interface Serializer {
  void setOutputCharStream(Writer paramWriter);
  DocumentHandler asDocumentHandler() throws IOException;
  void setOutputFormat(OutputFormat paramOutputFormat);
  void setOutputByteStream(OutputStream paramOutputStream);
}
