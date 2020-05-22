package me.illuminator3.scproxy;

import java.util.Collection;

public interface ProxyScanner
{
    Collection<? extends Proxy> newProxies();
    ProxyAPI getAPI();
}