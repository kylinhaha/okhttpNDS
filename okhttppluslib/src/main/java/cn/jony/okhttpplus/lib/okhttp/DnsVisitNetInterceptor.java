package cn.jony.okhttpplus.lib.okhttp;


import java.io.IOException;

import cn.jony.okhttpplus.lib.httpdns.DNSCache;
import cn.jony.okhttpplus.lib.httpdns.model.HostIP;
import cn.jony.okhttpplus.lib.httpdns.net.networktype.Constants;
import cn.jony.okhttpplus.lib.httpdns.net.networktype.InetAddressUtils;
import cn.jony.okhttpplus.lib.httpdns.net.networktype.NetworkManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class DnsVisitNetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        final String host = request.url().host();
        final boolean isSuc = response.isSuccessful();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetworkManager.Util.getNetworkType() != Constants.NETWORK_TYPE_UNCONNECTED &&
                        NetworkManager.Util.getNetworkType() != Constants.MOBILE_UNKNOWN &&
                        InetAddressUtils.isIPAddress(host)) {
                    HostIP ip = DNSCache.Instance.dbHelper.getIPByID(host);
                    if (isSuc) {
                        ip.sucNum++;
                    } else {
                        ip.failNum++;
                    }
                    ip.visitSinceSaved++;
                    DNSCache.Instance.dbHelper.updateIp(ip);
                }
            }
        }).start();
        return response;
    }
}
