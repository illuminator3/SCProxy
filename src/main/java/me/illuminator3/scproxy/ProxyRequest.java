package me.illuminator3.scproxy;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProxyRequest
{
    private final ProxyAPI api;

    public ProxyRequest(ProxyAPI api)
    {
        this.api = api;
    }

    public ProxyAPI getAPI()
    {
        return api;
    }

    public Collection<? extends Proxy> request()
        throws IOException
    {
        URL url = new URL(getAPI().getRequestURL());
        InputStream in = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        List<Proxy> proxies = new ArrayList<>();

        reader.lines().forEach(str -> {
            String[] arr = str.split(":");
            InetSocketAddress address = new InetSocketAddress(arr[0], Integer.parseInt(arr[1]));

            proxies.add(SCProxy.Factory.createProxy(getAPI().getType(), address));
        });

        return proxies;
    }
}