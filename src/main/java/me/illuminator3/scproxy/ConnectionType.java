package me.illuminator3.scproxy;

import java.net.Proxy;

public enum ConnectionType
    implements Converter<Proxy.Type>
{
    DIRECT(Proxy.Type.DIRECT),
    HTTP(Proxy.Type.HTTP),
    SOCKS4(Proxy.Type.SOCKS),
    SOCKS5(Proxy.Type.SOCKS);

    private final Proxy.Type jType;

    ConnectionType(Proxy.Type jType)
    {
        this.jType = jType;
    }

    @Override
    public Proxy.Type convert()
    {
        return this.jType;
    }
}