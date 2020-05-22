package me.illuminator3.scproxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public interface Proxy
    extends Converter<java.net.Proxy>
{
    ConnectionType getConnectionType();
    Socket openConnection(InetSocketAddress address) throws IOException;
    Socket readyConnection();
    InetSocketAddress getAddress();
    String getHost();
    int getPort();
}