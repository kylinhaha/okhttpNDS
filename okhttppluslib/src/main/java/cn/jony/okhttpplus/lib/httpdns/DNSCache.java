package cn.jony.okhttpplus.lib.httpdns;


import android.content.Context;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jony.okhttpplus.lib.httpdns.db.DNSCacheDatabaseHelper;
import cn.jony.okhttpplus.lib.httpdns.net.networktype.Constants;
import cn.jony.okhttpplus.lib.httpdns.net.networktype.NetworkManager;
import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveFactory;
import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveStrategy;
import okhttp3.OkHttpClient;

public enum DNSCache {
    Instance;

    public static final int UPDATE_CACHE_MSG = 1;
    public static final int CLEAR_CACHE_MSG = 2;

    public DNSCacheConfig config;
    public DNSCacheDatabaseHelper dbHelper;
    public OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(1, TimeUnit
            .SECONDS).readTimeout(1, TimeUnit.SECONDS).build();

    private String strategyName = HostResolveStrategy.DEFAULT;
    private HostResolveStrategy strategy;

    public void init(Context context, DNSCacheConfig config) {
        this.dbHelper = new DNSCacheDatabaseHelper(context);
        this.config = config;
        startUpdateTask();
    }

    public void init(Context context, DNSCacheConfig config, String strategyName) {
        this.dbHelper = new DNSCacheDatabaseHelper(context);
        this.config = config;
        this.strategyName = strategyName;
        startUpdateTask();
    }

    public void init(Context context, DNSCacheConfig config, HostResolveStrategy strategy) {
        this.dbHelper = new DNSCacheDatabaseHelper(context);
        this.config = config;
        this.strategy = strategy;
        startUpdateTask();
    }

    private UpdateThread updateThread;

    private void startUpdateTask() {
        updateThread = new UpdateThread();
        updateThread.start();
        Handler handler = updateThread.handler;
        handler.sendMessageDelayed(handler.obtainMessage(UPDATE_CACHE_MSG), config.expireMillis);
    }

    public void clear() {
        Handler handler = updateThread.handler;
        handler.sendMessage(handler.obtainMessage(CLEAR_CACHE_MSG));
    }

    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if (hostname == null) throw new UnknownHostException("hostname == null");
        return getHostResolveStrategy().lookup(hostname);
    }

    private HostResolveStrategy getHostResolveStrategy() {
        return strategy == null ? HostResolveFactory.getStrategy(strategyName) : strategy;
    }

    public void onNetworkStatusChanged(NetworkInfo networkInfo) {
        //do nothing
    }

    private class UpdateThread extends Thread {
        public Handler handler;

        @Override
        public void run() {
            Looper.prepare();
            handler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == UPDATE_CACHE_MSG) {
                        if (NetworkManager.Util.getNetworkType() !=
                                Constants.NETWORK_TYPE_UNCONNECTED && NetworkManager.Util
                                .getNetworkType() != Constants.MOBILE_UNKNOWN && strategy != null) {
                            strategy.update();
                        }

                        handler.sendMessageDelayed(handler.obtainMessage(UPDATE_CACHE_MSG), config
                                .expireMillis);

                    } else if (msg.what == CLEAR_CACHE_MSG && strategy != null) {
                        strategy.clear();
                    }
                }
            };
            Looper.loop();
        }
    }
}
