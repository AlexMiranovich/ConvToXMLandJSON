package org.exolab.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class Version {
  public static final String VERSION = "1.3.1";
  public static final String VERSION_DATE = "2010103";
  public static final String BUILD_VERSION = getBuildVersion();
  private static final String JAR_PROTOCOL = "jar:";
  private static final String FILE_PROTOCOL = "file:";
  private static final String DATE_FORMAT = "yyyyMMdd.HHmmss";

  public Version() {
  }

  public static String getBuildVersion() {
    StringBuffer buffer = new StringBuffer("1.3.1");
    String classname = Version.class.getName();
    String resource = "/" + classname.replace('.', '/') + ".class";
    URL url = Version.class.getResource(resource);
    if (url != null) {
      buffer.append("  [");
      String href = url.toString();
      Date date = null;
      if (href.startsWith("jar:")) {
        href = href.substring("jar:".length());
        if (href.startsWith("file:")) {
          href = href.substring("file:".length());
        }

        int idx = href.indexOf(33);
        String entryName = href.substring(idx + 2);
        href = href.substring(0, idx);

        try {
          ZipFile file = new ZipFile(href);
          ZipEntry entry = file.getEntry(entryName);
          if (entry != null) {
            long t = entry.getTime();
            if (t > 0L) {
              date = new Date(entry.getTime());
            }
          }
        } catch (IOException var12) {
          ;
        }
      } else if (href.startsWith("file:")) {
        File file = new File(href.substring("file:".length()));
        date = new Date(file.lastModified());
      }

      if (date != null) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd.HHmmss");
        buffer.append(format.format(date));
      } else {
        buffer.append("0");
      }

      buffer.append(']');
    }

    return buffer.toString();
  }

  public static void main(String[] args) {
    System.out.println(BUILD_VERSION);
  }
}
