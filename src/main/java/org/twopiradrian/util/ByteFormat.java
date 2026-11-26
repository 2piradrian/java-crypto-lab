package org.twopiradrian.util;

/**
 * Utilidades sin dependencias externas para visualizar bytes en consola
 * (hexadecimal, texto seguro y binario) y entender qué viaja por el canal.
 */
public final class ByteFormat {

    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    private ByteFormat() {
    }

    /**
     * Convierte un arreglo de bytes a pares hexadecimales separados por espacio.
     */
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

    /**
     * Convierte bytes a texto imprimible; los bytes fuera del rango ASCII visible
     * se muestran como '.' para no romper la salida de consola.
     */
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

    /**
     * Muestra la representación binaria de 8 bits de un byte.
     */
    public static String toBinary(byte b) {
        StringBuilder sb = new StringBuilder(8);
        for (int bit = 7; bit >= 0; bit--) {
            sb.append(((b >> bit) & 1) == 1 ? '1' : '0');
        }
        return sb.toString();
    }
}
