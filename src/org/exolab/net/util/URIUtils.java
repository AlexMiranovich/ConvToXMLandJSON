package org.exolab.net.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;
import java.util.StringTokenizer;

public class URIUtils {
  private static final String FILE_PROTOCOL_PREFIX = "file:///";
  private static final char HREF_PATH_SEP = '/';
  private static final String URL_PATH_SEP_STR = "/";
  private static final String CURRENT_DIR_OP = ".";
  private static final String PARENT_DIR_OP = "..";
  public static InputStream getInputStream(String href, String documentBase)
    throws FileNotFoundException, IOException {
    URL url = null;
    try {
      url = new URL(href);
      return url.openStream();
    }
    catch (MalformedURLException muex) {
      String xHref = null;
      if ((documentBase != null) && (documentBase.length() > 0))
      {
        int idx = documentBase.lastIndexOf('/');
        if (idx == documentBase.length() - 1) {
          xHref = documentBase + href;
        } else {
          xHref = documentBase + '/' + href;
        }
      }
      else {
        xHref = href;
      }
      try {
        url = new URL(xHref);
        return url.openStream();
      }
      catch (MalformedURLException d) {
        File iFile = new File(href);
        if (iFile.isAbsolute()) {
          return new FileInputStream(iFile);
        }
        iFile = new File(xHref);
        return new FileInputStream(iFile);
      }
    }
  }
  public static Reader getReader(String href, String documentBase)
    throws FileNotFoundException, IOException {
    InputStream is = getInputStream(href, documentBase);
    return new InputStreamReader(is);
  }
  public static String getDocumentBase(String href) {
    String docBase = "";
    if (href == null) {
      return docBase;
    }
    int idx = -1;
    try {
      new URL(href);
      idx = href.lastIndexOf('/');
    }
    catch (MalformedURLException muex) {
      int idx2 = href.lastIndexOf('/');
      idx = href.lastIndexOf(File.separator);
      if (idx2 > idx) {
        idx = idx2;
      }
    }
    if (idx >= 0) {
      docBase = href.substring(0, idx);
    }
    return docBase;
  }
  public static String getRelativeURI(String href) {
    if (href == null) {
      return href;
    }
    int idx = -1;
    try {
      new URL(href);
      idx = href.lastIndexOf('/');
    }
    catch (MalformedURLException muex) {
      int idx2 = href.lastIndexOf('/');
      idx = href.lastIndexOf(File.separator);
      if (idx2 > idx) {
        idx = idx2;
      }
    }
    if (idx >= 0) {
      return href.substring(idx + 1);
    }
    return href;
  }
  public static String normalize(String absoluteURL)
    throws MalformedURLException {
    if (absoluteURL == null) {
      return absoluteURL;
    }
    if (absoluteURL.indexOf('.') < 0) {
      return absoluteURL;
    }
    Stack tokens = new Stack();
    StringTokenizer st = new StringTokenizer(absoluteURL, "/", true);
    String last = null;
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if ("/".equals(token))
      {
        if ("/".equals(last)) {
          tokens.push("");
        }
      }
      else if ("..".equals(token)) {
        if (tokens.empty()) {
          throw new MalformedURLException("invalid absolute URL: " + absoluteURL);
        }
        tokens.pop();
      }
      else if (!".".equals(token)) {
        tokens.push(token);
      }
      last = token;
    }
    StringBuffer buffer = new StringBuffer(absoluteURL.length());
    for (int i = 0; i < tokens.size(); i++) {
      if (i > 0) {
        buffer.append('/');
      }
      buffer.append(tokens.elementAt(i).toString());
    }
    return buffer.toString();
  }
  public static String resolveAsString(String href, String documentBase) {
    try {
      new URL(href);
      return href;
    }
    catch (MalformedURLException muex) {
      String absolute = null;
      if ((documentBase != null) && (documentBase.length() > 0)) {
        int idx = documentBase.lastIndexOf('/');
        if (idx == documentBase.length() - 1) {
          absolute = documentBase + href;
        } else {
          absolute = documentBase + '/' + href;
        }
      }
      else {
        absolute = href;
      }
      try {
        if (absolute.indexOf("./") >= 0) {
          absolute = normalize(absolute);
        }
        new URL(absolute);
        return absolute;
      }
      catch (MalformedURLException d) {
        int idx = absolute.indexOf(':');
        if (idx >= 0)
        {
          String scheme = absolute.substring(0, idx);
          String error = "unknown protocol: " + scheme;
          if (error.equals(muex.getMessage())) {
            return absolute;
          }
        }
        String fileURL = absolute;
        File iFile = new File(href);
        boolean exists = iFile.exists();
        fileURL = createFileURL(iFile.getAbsolutePath());
        if (!iFile.isAbsolute()) {
          iFile = new File(absolute);
          if ((iFile.exists()) || (!exists)) {
            fileURL = createFileURL(iFile.getAbsolutePath());
          }
        }
        try {
          new URL(fileURL);
          return fileURL;
        }
        catch (MalformedURLException s) {}
      }
      return absolute;
    }
  }
  private static String createFileURL(String filename) {
    if (filename == null) {
      return "file:///";
    }
    int size = filename.length() + "file:///".length();
    StringBuffer sb = new StringBuffer(size);
    sb.append("file:///");
    char[] chars = filename.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char ch = chars[i];
      switch (ch) {
      case '\\': 
        sb.append('/');
        break;
      default: 
        sb.append(ch);
      }
    }
    return sb.toString();
  }
}
