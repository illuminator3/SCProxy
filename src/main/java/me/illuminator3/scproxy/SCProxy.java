package me.illuminator3.scproxy;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

public final class SCProxy
{
    private static final ProxyScanner proxyScanner;
    private static final ProxyManager proxyManager;

    static {
        proxyScanner = Factory.createProxyScanner();
        proxyManager = Factory.createProxyManager();
    }

    public static ProxyManager getProxyManager()
    {
        return proxyManager;
    }

    protected static Collection<? extends Proxy> proxies()
    {
        return proxyScanner.newProxies();
    }

    protected static class Factory
    {
        protected static ProxyManager createProxyManager()
        {
            return createProxyHandler().toProxyManager();
        }

        protected static ProxyHandler createProxyHandler()
        {
            final AtomicReference<Collection<? extends Proxy>> proxiesReference = new AtomicReference<>(SCProxy.proxies());

            return new ProxyHandler()
            {
                @Override
                public Collection<? extends Proxy> getProxies()
                {
                    return proxiesReference.get();
                }

                @Override
                public Collection<? extends Proxy> setProxies(Collection<? extends Proxy> proxies)
                {
                    proxiesReference.set(proxies);

                    return getProxies();
                }

                @Override
                public Collection<? extends Proxy> updateProxies()
                {
                    return setProxies(SCProxy.proxies());
                }
            };
        }

        protected static ProxyScanner createProxyScanner()
        {
            return createProxyScanner(ProxyAPI.PROXYSCRAPE_SOCKS4);
        }

        protected static ProxyScanner createProxyScanner(ProxyAPI api)
        {
            return new ProxyScanner()
            {
                @Override
                public Collection<? extends Proxy> newProxies()
                {
                    try
                    {
                        return new ProxyRequest(getAPI()).request();
                    } catch (final Exception ex)
                    {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public ProxyAPI getAPI()
                {
                    return api;
                }
            };
        }

        protected static Proxy createProxy(ConnectionType type, InetSocketAddress address)
        {
            return new Proxy()
            {
                @Override
                public java.net.Proxy convert()
                {
                    return new java.net.Proxy(getConnectionType().convert(), getAddress());
                }

                @Override
                public ConnectionType getConnectionType()
                {
                    return type;
                }

                @Override
                public Socket openConnection(InetSocketAddress addr)
                    throws IOException
                {
                    Socket socket = readyConnection();

                    socket.connect(addr, 5000);

                    return socket;
                }

                @Override
                public Socket readyConnection()
                {
                    Socket socket = new Socket(convert());

                    if (getConnectionType() == ConnectionType.SOCKS4)
                    {
                        try
                        {
                            Class<? extends Socket> clazzSocks = socket.getClass();
                            Field sockImplField = clazzSocks.getDeclaredField("impl");

                            sockImplField.setAccessible(true);

                            SocketImpl socksimpl = (SocketImpl) sockImplField.get(socket);
                            Class<? extends SocketImpl> clazzSocksImpl = socksimpl.getClass();
                            Method setSockVersion = clazzSocksImpl.getDeclaredMethod("setV4");

                            setSockVersion.setAccessible(true);
                            setSockVersion.invoke(socksimpl);
                            sockImplField.set(socket, socksimpl);
                        } catch (final Exception ex)
                        {
                            throw new RuntimeException(ex);
                        }
                    }

                    return socket;
                }

                @Override
                public InetSocketAddress getAddress()
                {
                    return address;
                }

                @Override
                public String getHost()
                {
                    return getAddress().getAddress().getHostAddress();
                }

                @Override
                public int getPort()
                {
                    return getAddress().getPort();
                }
            };
        }
    }
}