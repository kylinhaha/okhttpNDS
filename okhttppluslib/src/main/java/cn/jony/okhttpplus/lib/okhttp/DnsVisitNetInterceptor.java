package cn.jony.okhttpplus.lib.okhttp;


import java.io.IOException;

import cn.jony.okhttpplus.lib.httpdns.DNSCache;
import cn.jony.okhttpplus.lib.httpdns.model.HostIP;
import cn.jony.okhttpplus.lib.httpdns.net.networktype.InetAddressUtils;
import cn.jony.okhttpplus.lib.httpdns.net.networktype.NetworkManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class DnsVisitNetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(request);

        final String host = chain.connection().socket().getInetAddress().getHostAddress();
        final boolean isSuc = response.isSuccessful();
        final long rtt = System.currentTimeMillis() - startTime;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetworkManager.getInstance().isNetOK() &&
                        InetAddressUtils.isIPAddress(host)) {
                    HostIP ip = DNSCache.Instance.getIP(NetworkManager.getInstance().ipAddress, host);
                    if (ip != null) {
                        if (isSuc) {
                            ip.sucNum++;
                            ip.rtt = ip.sucNum == 1 ? rtt : (ip.rtt + rtt) / ip.sucNum;
                        } else {
                            ip.failNum++;
                        }
                        ip.visitSinceSaved++;
                        DNSCache.Instance.updateIP(ip);
                    }
                }
            }
        }).start();
        return response;
    }
}
