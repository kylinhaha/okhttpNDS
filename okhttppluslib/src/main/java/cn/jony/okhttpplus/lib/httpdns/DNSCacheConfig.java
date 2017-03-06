package cn.jony.okhttpplus.lib.httpdns;


import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveStrategy;

public class DNSCacheConfig {
    private static final long EXPIRE_MILLIS = 10_000;
    private static final int MAX_TTL = 255;
    private static final int FAST_TTL = 64;

    private String hostResolveStrategyName;
    private HostResolveStrategy hostResolveStrategy;
    private long expireMillis;
    private long updateMillis;
    private int maxTtl;

    static DNSCacheConfig DEFAULT = new DNSCacheConfig(HostResolveStrategy.DEFAULT, null,
            EXPIRE_MILLIS, EXPIRE_MILLIS / 2, MAX_TTL);

    public DNSCacheConfig(String hostResolveStrategyName, HostResolveStrategy
            hostResolveStrategy, long expireMillis, long updateMillis, int maxTtl) {
        this.hostResolveStrategyName = hostResolveStrategyName;
        this.hostResolveStrategy = hostResolveStrategy;
        this.expireMillis = expireMillis;
        this.updateMillis = updateMillis;
        this.maxTtl = maxTtl;
    }

    private DNSCacheConfig(Builder builder) {
        hostResolveStrategyName = builder.hostResolveStrategyName;
        hostResolveStrategy = builder.hostResolveStrategy;
        expireMillis = builder.expireMillis;
        updateMillis = builder.updateMillis;
        maxTtl = builder.maxTtl;
    }

    public class Builder {
        private String hostResolveStrategyName = HostResolveStrategy.DEFAULT;
        private HostResolveStrategy hostResolveStrategy;
        private long expireMillis = EXPIRE_MILLIS;
        private long updateMillis = expireMillis / 2;
        private int maxTtl = MAX_TTL;

        public Builder() {
        }

        public Builder hostResolveStrategyName(String hostResolveStrategyName) {
            this.hostResolveStrategyName = hostResolveStrategyName;
            return this;
        }

        public Builder hostResolveStrategy(HostResolveStrategy val) {
            hostResolveStrategy = val;
            return this;
        }

        public Builder expireMillis(long val) {
            expireMillis = val;
            return this;
        }

        public Builder updateMillis(long val) {
            updateMillis = val;
            return this;
        }

        public Builder maxTtl(int val) {
            maxTtl = val;
            return this;
        }

        public DNSCacheConfig build() {
            return new DNSCacheConfig(this);
        }
    }
}
