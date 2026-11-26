package org.twopiradrian.crypto;

public class SymmetricCipher implements CipherStrategy {

    private final byte[] key;

    public SymmetricCipher(byte[] key) {
        if (key == null || key.length == 0) {
            throw new IllegalArgumentException("Symmetric key cannot be empty.");
        }
        this.key = key.clone();
    }

    // Cifra un mensaje en bytes usando la clave simétrica compartida.
    @Override
    public byte[] encrypt(byte[] plaintext) {
        return applyXor(plaintext);
    }

    // Descifra un mensaje en bytes usando la clave simétrica compartida.
    @Override
    public byte[] decrypt(byte[] ciphertext) {
        return applyXor(ciphertext);
    }

    // Estrategia de cifrado y descifrado.
    private byte[] applyXor(byte[] input) {
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
