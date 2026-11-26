package org.twopiradrian.util;

public final class ByteFormat {

    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    private ByteFormat() {
    }

    // Convierte un arreglo de bytes a pares hexadecimales separados por espacio.
    public static String toHex(byte[] data) {
        if (data == null || data.length == 0) {
            return "<empty>";
        }
        StringBuilder sb = new StringBuilder(data.length * 3);
        for (int i = 0; i < data.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            int value = data[i] & 0xFF;
            sb.append(HEX_DIGITS[value >>> 4]);
            sb.append(HEX_DIGITS[value & 0x0F]);
        }
        return sb.toString();
    }

    // Convierte bytes a ASCII visible; los no imprimibles se muestran como '.' para no romper la consola.
    public static String toPrintableAscii(byte[] data) {
        if (data == null || data.length == 0) {
            return "<empty>";
        }
        StringBuilder sb = new StringBuilder(data.length);
        for (byte b : data) {
            int unsigned = b & 0xFF;
            if (unsigned >= 32 && unsigned <= 126) {
                sb.append((char) unsigned);
            } else {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    // Devuelve los 8 bits de un byte en formato binario.
    public static String toBinary(byte b) {
        StringBuilder sb = new StringBuilder(8);
        for (int bit = 7; bit >= 0; bit--) {
            sb.append(((b >> bit) & 1) == 1 ? '1' : '0');
        }
        return sb.toString();
    }
}
