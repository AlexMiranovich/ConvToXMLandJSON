package org.exec.core.util;

public final class Base64Encoder {
  private static final int OCTET_3 = 3;
  private static final int OCTET_1_MASK = 65535;
  private static final int OCTET_2_MASK = 16711935;
  private static final int OCTET_3_MASK = 16776960;
  private static final int OCTET_MASK = 255;
  private static final int SHIFT_1_OCTET = 8;
  private static final int SHIFT_2_OCTET = 16;
  private static final int SEXTET_MASK = 63;
  private static final int SHIFT_1_SEXTET = 6;
  private static final int SHIFT_2_SEXTET = 12;
  private static final int SHIFT_3_SEXTET = 18;
  private static final char[] MAP = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
  private int _buffer = 0;
  private int _octets = 0;
  private StringBuffer _stream = new StringBuffer();
  public static char[] encode(byte[] bytes) {
    Base64Encoder enc = new Base64Encoder();
    enc.translate(bytes);
    return enc.getCharArray();
  }
  public void reset() {
    this._buffer = 0;
    this._octets = 0;
    this._stream = new StringBuffer();
  }
  public void translate(byte[] bytes) {
    for (int i = 0; i < bytes.length; i++) {
      byte b = bytes[i];
      
      if (this._octets == 0) {
        this._buffer = (this._buffer & 0xFFFF | (b & 0xFF) << 16);
      } else if (this._octets == 1) {
        this._buffer = (this._buffer & 0xFF00FF | (b & 0xFF) << 8);
      } else {
        this._buffer = (this._buffer & 0xFFFF00 | b & 0xFF);
      }
      
      if (++this._octets == 3) { encode();
      }
    }
  }
  private void encode() {
    this._stream.append(MAP[(0x3F & this._buffer >> 18)]);
    this._stream.append(MAP[(0x3F & this._buffer >> 12)]);
    this._stream.append(MAP[(0x3F & this._buffer >> 6)]);
    this._stream.append(MAP[(0x3F & this._buffer)]);
    this._buffer = 0;
    this._octets = 0;
  }
  private void encodeWithPadding() {
    this._stream.append(MAP[(0x3F & this._buffer >> 18)]);
    this._stream.append(MAP[(0x3F & this._buffer >> 12)]);
    if (this._octets <= 1) {
      this._stream.append('=');
    } else {
      this._stream.append(MAP[(0x3F & this._buffer >> 6)]);
    }
    if (this._octets <= 2) {
      this._stream.append('=');
    } else {
      this._stream.append(MAP[(0x3F & this._buffer)]);
    }
    this._buffer = 0;
    this._octets = 0;
  }
  public char[] getCharArray() {
    if (this._octets > 0) encodeWithPadding();
    char[] chars = new char[this._stream.length()];
    if (this._stream.length() > 0) this._stream.getChars(0, this._stream.length(), chars, 0);
    return chars;
  }
}
