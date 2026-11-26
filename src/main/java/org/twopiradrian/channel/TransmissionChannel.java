package org.twopiradrian.channel;

import org.twopiradrian.util.ByteFormat;

public class TransmissionChannel {

    private final boolean mitmLoggingEnabled;

    public TransmissionChannel(boolean mitmLoggingEnabled) {
        this.mitmLoggingEnabled = mitmLoggingEnabled;
    }

    public void send(NetworkNode sender, NetworkNode receiver, byte[] payload) {
        if (mitmLoggingEnabled) {
            System.out.println("[TransmissionChannel / MITM]");
            System.out.println("  Intercepted traffic: " + sender.getName() + " -> " + receiver.getName());
            System.out.println("  Size:                " + payload.length + " bytes");
            System.out.println("  Bytes (Hex):         " + ByteFormat.toHex(payload));
            System.out.println("  As ASCII:            \"" + ByteFormat.toPrintableAscii(payload) + "\"");
            System.out.println();
        }

        receiver.receive(sender.getName(), payload);
    }
}
