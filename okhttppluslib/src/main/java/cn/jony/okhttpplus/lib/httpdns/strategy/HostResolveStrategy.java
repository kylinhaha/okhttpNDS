package cn.jony.okhttpplus.lib.httpdns.strategy;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import cn.jony.okhttpplus.lib.httpdns.model.HostIP;

public interface HostResolveStrategy {
    String DEFAULT = "default";
    String EMPTY = "empty";

    HostResolveStrategy EMPTY_RESOLVE_STRATEGY = new HostResolveStrategy() {
        @Override
        public List<InetAddress> lookup(String hostname) throws UnknownHostException {
            return null;
        }

        @Override
        public boolean isReliable(HostIP ip) {
            return false;
        }

        @Override
        public void update() {

        }

        @Override
        public void clear() {

        }
    };

    /**
     * lookup dns of hostname; if hostname has been ip, then the hostname of InetAddress is ""
     *
     * @param hostname
     * @return
     * @throws UnknownHostException
     */
    List<InetAddress> lookup(String hostname) throws UnknownHostException;

    boolean isReliable(HostIP ip);

    void update();

    void clear();
}
