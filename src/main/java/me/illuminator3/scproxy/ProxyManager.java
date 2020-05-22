package me.illuminator3.scproxy;

public interface ProxyManager
    extends ProxyHandler
{
    Proxy currentProxy();
    Proxy nextProxy();

    @Override
    default ProxyManager toProxyManager()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("Class is already a ProxyManager");
    }
}