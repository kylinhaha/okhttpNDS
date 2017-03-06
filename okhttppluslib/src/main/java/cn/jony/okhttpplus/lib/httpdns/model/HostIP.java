package cn.jony.okhttpplus.lib.httpdns.model;


import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveStrategy;

public class HostIP {
    String sourceIP;
    String targetIP;

    long saveMillis;
    long rtt;
    int ttl;

    int sucNum;
    int failNum;

    public boolean isReliable(HostResolveStrategy hostResolveStrategy) {
        return hostResolveStrategy.isReliable(this);
    }
}
