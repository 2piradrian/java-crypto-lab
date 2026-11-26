package org.twopiradrian.system;

import org.twopiradrian.channel.NetworkNode;
import org.twopiradrian.channel.TransmissionChannel;
import org.twopiradrian.crypto.SymmetricCipher;
import org.twopiradrian.util.ByteFormat;

import java.nio.charset.StandardCharsets;

public class Node implements NetworkNode {

    private final String name;
    private byte[] sharedSymmetricKey;

    public Node(String name, byte[] sharedSymmetricKey) {
        this.name = name;
        this.sharedSymmetricKey = sharedSymmetricKey.clone();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setSharedSymmetricKey(byte[] sharedSymmetricKey) {
        this.sharedSymmetricKey = sharedSymmetricKey.clone();
    }

    public void sendSymmetricMessage(String message, NetworkNode receiver, TransmissionChannel channel) {

        // Convertimos el texto a binario.
        byte[] plaintextBytes = message.getBytes(StandardCharsets.UTF_8);
        // Ciframos el binario.
        byte[] ciphertextBytes = SymmetricCipher.encrypt(plaintextBytes, sharedSymmetricKey);

        // Logs de interpretación.
        System.out.println("[" + name + " -> SENDING]");
        System.out.println("  Recipient:      " + receiver.getName());
        System.out.println("  Plaintext:      \"" + message + "\"");
        System.out.println("  Plain (Hex):    " + ByteFormat.toHex(plaintextBytes));
        System.out.println("  Cipher (Hex):   " + ByteFormat.toHex(ciphertextBytes));
        System.out.println();

        // Enviamos el mensaje por el canal.
        channel.send(this, receiver, ciphertextBytes);
    }

    @Override
    public void receive(String from, byte[] payload) {

        // Desciframos el binario.
        byte[] decryptedBytes = SymmetricCipher.decrypt(payload, sharedSymmetricKey);
        // Convertimos el binario a texto.
        String recoveredMessage = new String(decryptedBytes, StandardCharsets.UTF_8);

        // Logs de interpretación.
        System.out.println("[" + name + " <- RECEIVED]");
        System.out.println("  Sender:         " + from);
        System.out.println("  Cipher (Hex):   " + ByteFormat.toHex(payload));
        System.out.println("  Decrypted Hex:  " + ByteFormat.toHex(decryptedBytes));
        System.out.println("  Final message:  \"" + recoveredMessage + "\"");
        System.out.println();
    }
}
