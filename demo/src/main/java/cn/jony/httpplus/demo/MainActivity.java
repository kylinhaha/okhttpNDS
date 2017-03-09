package cn.jony.httpplus.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;

import cn.jony.okhttpplus.lib.httpdns.DNSCache;
import cn.jony.okhttpplus.lib.httpdns.DNSCacheConfig;
import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveStrategy;
import cn.jony.okhttpplus.lib.okhttp.DnsVisitNetInterceptor;
import cn.jony.okhttpplus.lib.okhttp.HttpDNS;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DNSCache.Instance.init(this.getApplicationContext(), new DNSCacheConfig.Builder().build(),
                HostResolveStrategy.SYNC);
        final OkHttpClient client = new OkHttpClient.Builder().dns(new HttpDNS())
                .addNetworkInterceptor
                        (new DnsVisitNetInterceptor()).build();

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call call = client.newCall(new Request.Builder().url("http://www.baidu.com").build
                        ());

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("request fail", Log.getStackTraceString(e));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("response", response.body().string());
                    }
                });
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call call = client.newCall(new Request.Builder().url("http://www.sina.com.cn").build
                        ());

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("request fail", Log.getStackTraceString(e));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("response", response.body().string());
                    }
                });
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call call = client.newCall(new Request.Builder().url("https://www.baidu.com").build
                        ());

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("request fail", Log.getStackTraceString(e));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("response", response.body().string());
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
