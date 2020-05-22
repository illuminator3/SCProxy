package me.illuminator3.scproxy;

public enum ProxyAPI
{
    PROXYSCRAPE_HTTP("https://api.proxyscrape.com/?request=displayproxies&proxytype=http&timeout=5000&anonymity=elite&ssl=yes", ConnectionType.HTTP),
    PROXYSCRAPE_SOCKS4("https://api.proxyscrape.com/?request=displayproxies&proxytype=socks4&timeout=5000&anonymity=elite&ssl=yes", ConnectionType.SOCKS4),
    PROXYSCRAPE_SOCKS5("https://api.proxyscrape.com/?request=displayproxies&proxytype=socks5&timeout=5000&anonymity=elite&ssl=yes", ConnectionType.SOCKS5);

    private final String requestURL;
    private final ConnectionType type;

    ProxyAPI(String requestURL, ConnectionType type)
    {
        this.requestURL = requestURL;
        this.type = type;
    }

    public String getRequestURL()
    {
        return requestURL;
    }

    public ConnectionType getType()
    {
        return type;
    }
}