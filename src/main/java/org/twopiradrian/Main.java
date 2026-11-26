package org.twopiradrian;

import org.twopiradrian.channel.TransmissionChannel;
import org.twopiradrian.crypto.SymmetricCipher;
import org.twopiradrian.system.Node;
import org.twopiradrian.util.ByteFormat;

import java.nio.charset.StandardCharsets;

public class Main {

    static void main(String[] args) {
        demonstrateSingleByteXor('H', 'M');
        runSymmetricCommunication();
    }

    // Demostración mínima de cómo XOR cifra y descifra 1 byte bit a bit.
    private static void demonstrateSingleByteXor(char messageChar, char keyChar) {
        byte msgByte = (byte) messageChar;
        byte keyByte = (byte) keyChar;
        byte cipherByte = SymmetricCipher.encrypt(new byte[]{msgByte}, new byte[]{keyByte})[0];
        byte recoveredByte = SymmetricCipher.decrypt(new byte[]{cipherByte}, new byte[]{keyByte})[0];

        System.out.println("[Micro-example: 1 byte with XOR (^)]");
        System.out.println("  Original char '" + messageChar + "': " + ByteFormat.toBinary(msgByte) + " (0x" + ByteFormat.toHex(new byte[]{msgByte}) + ")");
        System.out.println("  Key byte      '" + keyChar + "': " + ByteFormat.toBinary(keyByte) + " (0x" + ByteFormat.toHex(new byte[]{keyByte}) + ")");
        System.out.println("  Encrypted (M ^ K): " + ByteFormat.toBinary(cipherByte) + " (0x" + ByteFormat.toHex(new byte[]{cipherByte}) + ")");
        System.out.println("  Decrypted (C ^ K): " + ByteFormat.toBinary(recoveredByte) + " -> '" + (char) recoveredByte + "'");
        System.out.println();
    }

    // Comunicación bidireccional entre dos nodos usando la misma clave secreta.
    private static void runSymmetricCommunication() {
        byte[] sharedKey = "MY_SECRET_KEY".getBytes(StandardCharsets.UTF_8);

        Node system1 = new Node("System1", sharedKey);
        Node system2 = new Node("System2", sharedKey);
        TransmissionChannel channel = new TransmissionChannel(true);

        system1.sendSymmetricMessage("Hello System2! Transfer 500 USD", system2, channel);
        system2.sendSymmetricMessage("Received System1, operation confirmed.", system1, channel);
    }
}
