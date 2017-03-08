package cn.jony.okhttpplus.lib.httpdns.model;


import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveStrategy;

public class HostIP {
    public static final long PERMIT_MAX_RTT = 500;
    private static final int MAX_TTL = 255;

    public String sourceIP;
    public String targetIP;
    public String operator;
    public String host;

    public long saveMillis;
    // 默认rtt为允许最大rtt，默认ttl为允许最大ttl
    public long rtt = PERMIT_MAX_RTT;
    public int ttl = MAX_TTL;

    public int sucNum;
    public int failNum;
    public int visitSinceSaved;

    public HostIP(){}

    private HostIP(Builder builder) {
        sourceIP = builder.sourceIP;
        targetIP = builder.targetIP;
        operator = builder.operator;
        host = builder.host;
        saveMillis = builder.saveMillis;
        rtt = builder.rtt;
        ttl = builder.ttl;
        sucNum = builder.sucNum;
        failNum = builder.failNum;
        visitSinceSaved = builder.visitSinceSaved;
    }

    public boolean isReliable(HostResolveStrategy hostResolveStrategy) {
        return hostResolveStrategy.isReliable(this);
    }

    public long getWorkMillis() {
        return System.currentTimeMillis() - saveMillis;
    }

    public double getFailPercent() {
        return sucNum > 0 ? failNum / (double) sucNum : 1.0;
    }


    public static final class Builder {
        private String sourceIP;
        private String targetIP;
        private String operator;
        private String host;
        private long saveMillis;
        private long rtt = PERMIT_MAX_RTT;
        private int ttl = MAX_TTL;
        private int sucNum;
        private int failNum;
        private int visitSinceSaved;

        public Builder() {
        }

        public Builder sourceIP(String val) {
            sourceIP = val;
            return this;
        }

        public Builder targetIP(String val) {
            targetIP = val;
            return this;
        }

        public Builder operator(String val) {
            operator = val;
            return this;
        }

        public Builder host(String val) {
            host = val;
            return this;
        }

        public Builder saveMillis(long val) {
            saveMillis = val;
            return this;
        }

        public Builder rtt(long val) {
            rtt = val;
            return this;
        }

        public Builder ttl(int val) {
            ttl = val;
            return this;
        }

        public Builder sucNum(int val) {
            sucNum = val;
            return this;
        }

        public Builder failNum(int val) {
            failNum = val;
            return this;
        }

        public Builder visitSinceSaved(int val) {
            visitSinceSaved = val;
            return this;
        }

        public HostIP build() {
            return new HostIP(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HostIP ip = (HostIP) o;

        return targetIP != null ? targetIP.equals(ip.targetIP) : ip.targetIP == null;

    }

    @Override
    public int hashCode() {
        return targetIP != null ? targetIP.hashCode() : 0;
    }
}
