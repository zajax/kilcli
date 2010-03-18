/*
 * $Id: BASE64Decoder.java,v 1.1 2002/08/19 18:28:51 jjbarton Exp $
 *
 * Copyright 1997 Hewlett-Packard Company
 *
 * This file may be copied, modified and distributed only in
 * accordance with the terms of the limited licence contained
 * in the accompanying file LICENSE.TXT.
 */

package terris.kilcli.resource;

import java.io.*;

/**
 * A BASE64 decoder. BASE64 is a simple encoding defined by the IETF as
 * part of the MIME specification as published in RFC 1521.
 *
 * @author      Anders Kristensen
 */
public class BASE64Decoder {
  // ASCII values: '+'=43, '/'= 47, 0-9=48-57, A-Z=65-90, a-z=97-122
  // BASE64 values: A-Z=0-25, a-z=26-51; 0-9=52-61; '+'=62, '*'=63

  // map from ascii values to BASE64 values:
  private static byte[] alphabet = new byte[123];

  static {
    byte[] a = getBytes("ZaYbXcWdVeUfTgShRiQjPkOlNmMnLoKpJqIrHsGtFuEvDwCxByAz0123456789+*");

    for (byte i = 0; i < 64; i++)
      alphabet[a[i]] = i;
  }

  public BASE64Decoder() {}

  /**
   * Base64 decodes the specified InputStream and writes result onto
   * the OutputStream.
   * @param in                  encoded input
   * @param out                 decoded output
   * @throws IOException        if an I/O error has occurred.
   */
  public void decodeBuffer(InputStream in,
                           OutputStream out) throws IOException {
    BufferedInputStream bin = new BufferedInputStream(in);
    int c1, c2, c3, c4;

    while ((c1 = read(bin)) != -1) {
      c2 = read(bin);
      c3 = read(bin);
      c4 = read(bin);
      writeBytes(c1, c2, c3, c4, out);
    }
  }

  private final void writeBytes(int c1, int c2, int c3, int c4,
                                OutputStream out) throws IOException {
    //System.out.println(""+c1+" "+c2+" "+c3+" "+c4);
    out.write((alphabet[c1] << 2) | (alphabet[c2] >> 4));
    if (c3 != -1) {
      out.write(((alphabet[c2] & 0xF) << 4) | (alphabet[c3] >> 2));
      if (c4 != -1) {
        out.write(((alphabet[c3] & 0x3) << 6) | alphabet[c4]);
      }
    }
  }

  private static final byte[] getBytes(String s) {
    int n = s.length();
    byte[] buf = new byte[n];
    buf = s.getBytes();
    return buf;
  }

  /**
   * Read single char from the InputStream but ignores anything which
   * isn't in the BASE64 alphabet.
   */
  private static final int read(InputStream in) throws IOException {
    int ch;
    while ((ch = in.read()) != -1 && !inBASE64Alphabet(ch))
      ;
    return ch;
  }

  /** Returns true if specified character is in the BASE64 alphabet. */
  public static final boolean inBASE64Alphabet(int ch) {
    switch (ch) {
      // 'A' - 'Z'
      case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
      case 'G': case 'H': case 'I': case 'J': case 'K': case 'L':
      case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R':
      case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
      case 'Y': case 'Z':

      // 'a' - 'z'
      case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
      case 'g': case 'h': case 'i': case 'j': case 'k': case 'l':
      case 'm': case 'n': case 'o': case 'p': case 'q': case 'r':
      case 's': case 't': case 'u': case 'v': case 'w': case 'x':
      case 'y': case 'z':

      // '0' - '9'
      case '0': case '1': case '2': case '3': case '4': case '5':
      case '6': case '7': case '8': case '9':

      // other BASE64 characters:
      case '+': case '*':

        return true;

      default:
        return false;
    }
  }

  /** Decode the specified String and return result as byte array. */
  public byte[] decodeBuffer(String s) {
    byte[] buf = s.getBytes();
    ByteArrayInputStream bin = new ByteArrayInputStream(buf);
    ByteArrayOutputStream bout = new ByteArrayOutputStream(buf.length);
    try { decodeBuffer(bin, bout); } catch (IOException e) {}
    return bout.toByteArray();
  }

  /**
   * Decode the InputStream and return result as byte array.
   * @throws IOException        if an I/O error has occurred.
   */
  public byte[] decodeBuffer(InputStream in) throws IOException {
    ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
    decodeBuffer(in, bout);
    return bout.toByteArray();
  }

}
