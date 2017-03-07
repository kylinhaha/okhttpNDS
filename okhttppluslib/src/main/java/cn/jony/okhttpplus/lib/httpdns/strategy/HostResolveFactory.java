package cn.jony.okhttpplus.lib.httpdns.strategy;


public class HostResolveFactory {
    public static HostResolveStrategy getStrategy(String strategy) {
        switch (strategy) {
            case HostResolveStrategy.EMPTY:
                return HostResolveStrategy.EMPTY_RESOLVE_STRATEGY;
            case HostResolveStrategy.DEFAULT:
            default:
                return new DefaultHostResolveStrategy();
        }
    }
}
