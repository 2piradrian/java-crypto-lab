package org.twopiradrian.crypto;

// Cifra y descifra con XOR: es reversible porque (M ^ K) ^ K = M.
public class SymmetricCipher {

    // Cifra un mensaje en bytes usando la clave simétrica compartida.
    public static byte[] encrypt(byte[] plaintext, byte[] key) {
        return applyXor(plaintext, key);
    }

    // Descifra un mensaje en bytes usando la clave simétrica compartida.
    public static byte[] decrypt(byte[] ciphertext, byte[] key) {
        return applyXor(ciphertext, key);
    }

    // Estrategia de cifrado y descifrado.
    private static byte[] applyXor(byte[] input, byte[] key) {
        if (key == null || key.length == 0) {
            throw new IllegalArgumentException("Symmetric key cannot be empty.");
        }

        // Array de bytes para almacenar el resultado.
        byte[] result = new byte[input.length];

        // Conversión byte a byte
        for (int i = 0; i < input.length; i++) {
            // Obtiene el byte correspondiente de la clave, repitiéndola de forma cíclica si el mensaje es más largo.
            byte keyByte = key[i % key.length];
            // Aplica XOR entre el byte del mensaje y el byte de la clave.
            result[i] = (byte) (input[i] ^ keyByte);
        }
        return result;
    }
}
