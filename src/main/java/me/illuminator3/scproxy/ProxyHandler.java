package me.illuminator3.scproxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ProxyHandler
{
    Collection<? extends Proxy> getProxies();
    Collection<? extends Proxy> setProxies(Collection<? extends Proxy> proxies);
    Collection<? extends Proxy> updateProxies();

    default ProxyManager toProxyManager()
    {
        return new ProxyManager()
        {
            volatile int currentPosition = 0;

            private synchronized int currentPosition()
            {
                return currentPosition;
            }

            private synchronized int nextPosition()
            {
                int r = currentPosition++;

                if (r >= getProxies().size())
                {
                    currentPosition = 0;

                    return 0;
                }

                return r;
            }

            private List<Proxy> proxiesList()
            {
                return new ArrayList<>(getProxies());
            }

            @Override public Proxy currentProxy() { return proxiesList().get(currentPosition()); }
            @Override public Proxy nextProxy() { return proxiesList().get(nextPosition()); }
            @Override public Collection<? extends Proxy> getProxies() { return ProxyHandler.this.getProxies(); }
            @Override public Collection<? extends Proxy> setProxies(Collection<? extends Proxy> proxies) { return ProxyHandler.this.setProxies(proxies); }
            @Override public Collection<? extends Proxy> updateProxies() { return ProxyHandler.this.updateProxies(); }
        };
    }
}