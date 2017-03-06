package cn.jony.okhttpplus.lib.httpdns.strategy;


public class HostResolveFactory {
    public static HostResolveStrategy getStrategy(String strategy) {
        switch (strategy) {
            case HostResolveStrategy.EMPTY:
            case HostResolveStrategy.DEFAULT:
            default:
                return HostResolveStrategy.EMPTY_RESOLVE_STRATEGY;
        }
    }
}
