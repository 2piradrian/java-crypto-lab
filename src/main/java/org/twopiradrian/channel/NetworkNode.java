package org.twopiradrian.channel;

public interface NetworkNode {

    String getName();

    void receive(String from, byte[] payload);

}
