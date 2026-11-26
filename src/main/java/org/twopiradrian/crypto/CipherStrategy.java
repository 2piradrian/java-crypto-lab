package org.twopiradrian.crypto;

public interface CipherStrategy {

    byte[] encrypt(byte[] plaintext);

    byte[] decrypt(byte[] ciphertext);
}
