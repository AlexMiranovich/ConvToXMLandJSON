package org.exec.core.util;

import java.io.ByteArrayOutputStream;

public final class Base64Decoder {
  private static final int SEXTET_1_MASK = 262143;
  private static final int SEXTET_2_MASK = 16519167;
  private static final int SEXTET_3_MASK = 16773183;
  private static final int SEXTET_4_MASK = 16777152;
  private static final int SHIFT_1_SEXTET = 6;
  private static final int SHIFT_2_SEXTET = 12;
  private static final int SHIFT_3_SEXTET = 18;
  private static final int SEXTET_2 = 2;
  private static final int SEXTET_3 = 3;
  private static final int SEXTET_4 = 4;
  private static final int OCTET_MASK = 255;
  private static final int SHIFT_1_OCTET = 8;
  private static final int SHIFT_2_OCTET = 16;
  private static final byte SPC = 127;
  private static final byte PAD = 64;
  private static final byte[] MAP = { Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, 62, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, 64, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE };
  private int _buffer = 0;
  private int _sextets = 0;
  private ByteArrayOutputStream _stream = new ByteArrayOutputStream();
  public static byte[] decode(String str) {
    Base64Decoder dec = new Base64Decoder();
    dec.translate(str);
    return dec.getByteArray();
  }
  public void translate(String string) {
    int len = string.length();
    int index = 0;
    int data = MAP[string.charAt(index)];
    while ((index < len) && (data != 64)) {
      if (data != 127) {
        if (this._sextets == 0) {
          this._buffer = (this._buffer & 0x3FFFF | data << 18);
        } else if (this._sextets == 1) {
          this._buffer = (this._buffer & 0xFC0FFF | data << 12);
        } else if (this._sextets == 2) {
          this._buffer = (this._buffer & 0xFFF03F | data << 6);
        } else {
          this._buffer = (this._buffer & 0xFFFFC0 | data);
        }
        
        if (++this._sextets == 4) { decode();
        }
      }
      index++; if (index < len) { data = MAP[string.charAt(index)];
      }
    }
    if (this._sextets > 0) { decodeWithPadding();
    }
  }
  private void decode() {
    this._stream.write((byte)(this._buffer >> 16 & 0xFF));
    this._stream.write((byte)(this._buffer >> 8 & 0xFF));
    this._stream.write((byte)(this._buffer & 0xFF));
    this._buffer = 0;
    this._sextets = 0;
  }
  private void decodeWithPadding() {
    if (this._sextets >= 2) {
      this._stream.write((byte)(this._buffer >> 16 & 0xFF));
    }
    if (this._sextets >= 3) {
      this._stream.write((byte)(this._buffer >> 8 & 0xFF));
    }
    if (this._sextets >= 4) {
      this._stream.write((byte)(this._buffer & 0xFF));
    }
    this._buffer = 0;
    this._sextets = 0;
  }
  public byte[] getByteArray()
  {
    return this._stream.toByteArray();
  }
}
