package org.exec.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class HexDecoder {
  public static final String DATA_TYPE = "hexBinary";
  private static final int DECODING_TABLE_SIZE = 128;
  protected static final byte[] ENCODING_TABLE = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
  protected static final byte[] DECODING_TABLE = new byte[''];
  protected static void initialiseDecodingTable() {
    for (int i = 0; i < ENCODING_TABLE.length; i++) {
      DECODING_TABLE[ENCODING_TABLE[i]] = ((byte)i);
    }
    DECODING_TABLE[97] = DECODING_TABLE[65];
    DECODING_TABLE[98] = DECODING_TABLE[66];
    DECODING_TABLE[99] = DECODING_TABLE[67];
    DECODING_TABLE[100] = DECODING_TABLE[68];
    DECODING_TABLE[101] = DECODING_TABLE[69];
    DECODING_TABLE[102] = DECODING_TABLE[70];
  }
  static {
    initialiseDecodingTable();
  }
  public static int encode(byte[] data, int off, int length, OutputStream out)
    throws IOException {
    for (int i = off; i < off + length; i++) {
      int v = data[i] & 0xFF;
      out.write(ENCODING_TABLE[(v >>> 4)]);
      out.write(ENCODING_TABLE[(v & 0xF)]);
    }
    return length * 2;
  }
  private static boolean ignore(char c)
  {
    return (c == '\n') || (c == '\r') || (c == '\t') || (c == ' ');
  }
  public static int decode(byte[] data, int off, int length, OutputStream out)
    throws IOException {
    int outLen = 0;
    int end = off + length;
    while ((end > off) && 
      (ignore((char)data[(end - 1)]))) {
      end--;
    }
    int i = off;
    while (i < end) {
      while ((i < end) && (ignore((char)data[i]))) {
        i++;
      }
      byte b1 = DECODING_TABLE[data[(i++)]];
      while ((i < end) && (ignore((char)data[i]))) {
        i++;
      }
      byte b2 = DECODING_TABLE[data[(i++)]];
      out.write(b1 << 4 | b2);
      outLen++;
    }
    return outLen;
  }
  public static int decode(String data, OutputStream out)
    throws IOException {
    int length = 0;
    int end = data.length();
    while ((end > 0) && 
      (ignore(data.charAt(end - 1)))) {
      end--;
    }
    int i = 0;
    while (i < end) {
      while ((i < end) && (ignore(data.charAt(i)))) {
        i++;
      }
      byte b1 = DECODING_TABLE[data.charAt(i++)];
      while ((i < end) && (ignore(data.charAt(i)))) {
        i++;
      }
      byte b2 = DECODING_TABLE[data.charAt(i++)];
      out.write(b1 << 4 | b2);
      length++;
    }
    return length;
  }
  public static String encode(byte[] data) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      encode(data, 0, data.length, out);
      out.close();
      return new String(out.toByteArray());
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage(), e);
    }
  }
  public static byte[] decode(String data) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      decode(data, out);
      out.close();
      return out.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
