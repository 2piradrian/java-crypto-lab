package org.twopiradrian;

import org.twopiradrian.channel.TransmissionChannel;
import org.twopiradrian.crypto.AsymmetricCipher;
import org.twopiradrian.crypto.AsymmetricCipher.KeyPair;
import org.twopiradrian.crypto.CipherStrategy;
import org.twopiradrian.crypto.SymmetricCipher;
import org.twopiradrian.system.Node;
import org.twopiradrian.util.ByteFormat;

import java.nio.charset.StandardCharsets;

public class Main {

    static void main(String[] args) {
        TransmissionChannel channel = new TransmissionChannel(true);

        demonstrateSingleByteXor('H', 'M');
        runSymmetricCommunication(channel);
        runAsymmetricCommunication(channel);
    }

    // Demostración mínima de cómo XOR cifra y descifra 1 byte bit a bit.
    private static void demonstrateSingleByteXor(char messageChar, char keyChar) {
        CipherStrategy xor = new SymmetricCipher(new byte[]{(byte) keyChar});
        byte msgByte = (byte) messageChar;
        byte keyByte = (byte) keyChar;
        byte cipherByte = xor.encrypt(new byte[]{msgByte})[0];
        byte recoveredByte = xor.decrypt(new byte[]{cipherByte})[0];

        System.out.println("[Micro-example: 1 byte with XOR (^)]");
        System.out.println("  Original char '" + messageChar + "': " + ByteFormat.toBinary(msgByte) + " (0x" + ByteFormat.toHex(new byte[]{msgByte}) + ")");
        System.out.println("  Key byte      '" + keyChar + "': " + ByteFormat.toBinary(keyByte) + " (0x" + ByteFormat.toHex(new byte[]{keyByte}) + ")");
        System.out.println("  Encrypted (M ^ K): " + ByteFormat.toBinary(cipherByte) + " (0x" + ByteFormat.toHex(new byte[]{cipherByte}) + ")");
        System.out.println("  Decrypted (C ^ K): " + ByteFormat.toBinary(recoveredByte) + " -> '" + (char) recoveredByte + "'");
        System.out.println();
    }

    // Comunicación bidireccional entre dos nodos usando la misma clave secreta.
    private static void runSymmetricCommunication(TransmissionChannel channel) {
        System.out.println("=== SYMMETRIC ===\n");

        byte[] sharedKey = "MY_SECRET_KEY".getBytes(StandardCharsets.UTF_8);

        Node system1 = new Node("System1", new SymmetricCipher(sharedKey));
        Node system2 = new Node("System2", new SymmetricCipher(sharedKey));

        system1.sendMessage("Hello System2! Transfer 500 USD", system2, channel);
        system2.sendMessage("Received System1, operation confirmed.", system1, channel);
    }

    // Comunicación bidireccional usando pares de claves pública/privada independientes.
    private static void runAsymmetricCommunication(TransmissionChannel channel) {
        System.out.println("=== ASYMMETRIC ===\n");

        // Cada nodo genera su propio par (clave pública + clave privada).
        KeyPair system1Keys = AsymmetricCipher.generateKeyPair(61, 53);
        KeyPair system2Keys = AsymmetricCipher.generateKeyPair(67, 71);

        System.out.println("[Key Setup]");
        System.out.println("  System1 PublicKey:  (e=" + system1Keys.publicKey().e() + ", n=" + system1Keys.publicKey().n() + ")");
        System.out.println("  System1 PrivateKey: (d=" + system1Keys.privateKey().d() + ", n=" + system1Keys.privateKey().n() + ") [SECRET]");
        System.out.println("  System2 PublicKey:  (e=" + system2Keys.publicKey().e() + ", n=" + system2Keys.publicKey().n() + ")");
        System.out.println("  System2 PrivateKey: (d=" + system2Keys.privateKey().d() + ", n=" + system2Keys.privateKey().n() + ") [SECRET]");
        System.out.println();

        // System1 cifra con la clave pública de System2 y descifra con su propia clave privada (y viceversa).
        Node system1 = new Node("System1", new AsymmetricCipher(system1Keys.privateKey(), system2Keys.publicKey()));
        Node system2 = new Node("System2", new AsymmetricCipher(system2Keys.privateKey(), system1Keys.publicKey()));

        system1.sendMessage("Hello System2! Transfer 500 USD", system2, channel);
        system2.sendMessage("Received System1, operation confirmed.", system1, channel);
    }
}
