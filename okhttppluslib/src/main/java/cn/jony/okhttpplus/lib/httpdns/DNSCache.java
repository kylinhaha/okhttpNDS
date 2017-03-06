package cn.jony.okhttpplus.lib.httpdns;


import android.net.NetworkInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveFactory;
import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveStrategy;

public enum DNSCache {
    Instance;

    private DNSCacheConfig config;
    private String strategyName = HostResolveStrategy.DEFAULT;
    private HostResolveStrategy strategy;

    public void init(DNSCacheConfig config) {
        this.config = config;
    }

    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if (hostname == null) throw new UnknownHostException("hostname == null");
        return getHostResolveStrategy().lookup(hostname);
    }

    private HostResolveStrategy getHostResolveStrategy() {
        return strategy == null ? HostResolveFactory.getStrategy(strategyName) : strategy;
    }

    public void onNetworkStatusChanged(NetworkInfo networkInfo) {

    }
}
