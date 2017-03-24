github仓库：https://github.com/jonyChina162/okhttpNDS  
gradle引用：compile 'cn.jony.okhttpplus.lib:okhttp3plus:1.0.0-beta'  
 
目的
===
1. 防劫持，通常是域名劫持，因为我们可以使用ip+头部设置“host”的方式直接访问；另外虽然https使得我们可以验证服务器，但同样使得我们承担多失败一次的代价，通过httpDns方式直接访问可信Dns（Dns_Pod）来代替经过中间服务商的dns拿到ip然后进行网络访问。
2. 因为集成http2，所以依赖于okhttp可以减少自己对协议的支持；同时okhttp支持直接设置dns的方式来访问网络，避免我们设host的方式造成https证书检验host时不通过的坑。

缺点
===
1. 我们跳过了中间dns，直接访问可信dns，如果可信dns出现了问题的话，那么我们的dns请求失败，这时如何处理网络访问；
2. 返回的ip可能存在性能不好的问题（虽然大多时候返回的ip速度都很快），这时会造成访问性能下降，我们该如何应对。

解决方案
===
针对以上两个缺点，参考了sina的HttpDNS方案，做出了本文的解决方案：
1. 提供三种HttpDns访问策略模式，默认的方式同步缓存、异步网络dns；也就是本次通过http请求访问的dns下次再使用，本次走系统默认的host访问。
2. 使用ip访问后对ip进行一系列的校验，检测参数判断ip是否可靠，作为下次是否访问的依据。


* * *
本包访问的可信服务器为开源的DNS_POS服务器，http://119.29.29.29/d?dn=

策略
===
提供了三种HttpDNS解析的策略：
#####default - DNS解析流程包括
1. 查找
查找索引包括host和sourceIP，依次分3个层次cache，db和httpDNS查找；在任何一层如果查找到数据则返回，并用查找出来的数据刷新上一层次的数据；
如果都没有查找到，则走系统默认的dns；默认的httpDNS查找是异步的，因此第一次可能不能及时的走httpDNS，如果希望第一次查找就走dns，可以使用
{@link cn.jony.okhttpplus.lib.httpdns.DNSCache#preLoadDNS(String...)}来进行预加载

2. 检查ip是否新鲜
对于查找出来的ip验证其ttl是否新鲜，如果不新鲜则在后台启动一次对该host的lookup重新获取其ttl。

3. 更新 
分为定时更新和非定时更新:
a. 定时更新：只更新db中的ip数据，不更新cache中的数据，cache中的数据在超时时，从db中查找或者通过httpDNS请求返回数据更新cache。
定时更新流程：每隔超时时间的一半时间，对db所有的HostIP数据进行一轮测速并统计，并重置缓存时间内访问量超过阈值的HostIP的缓存时间；最后
删除不可靠的HostIP，保留可靠的HostIP。
b. 非定时更新，主要包括两个方面：1. 当cache数据失效时，从db获得有效数据时直接更新cache；2. 通过DnsVisitInterceptor对访问的dns进行
数据更新。

4. 验证HostIp可靠性：
   1. targetIP 存在 
   2. 没有超时 
   3. rtt 没有超过允许的最大rtt，默认300 
   4. 请求失败次数最多为1次，或者成功率大于95%

#####strict - 严格模式
与default模式的差别主要在于default模式会优先使用未使用过的dns（HostIP的rtt默认为0）；而strict模式确保HostIP只有在经过测速后才可以使用，
因此strict模式在HttpDNS请求后会尽快发送一轮测速，而default模式只会在定时更新任务中进行测速。并且在过滤可以使用的ip的时候strict模式是通过
isFresh来验证而不是通过isReliable验证
#####sync - 同步模式
default在发出HttpDNS请求后并不进行等待，直接返回结果。这时结果可能为null，即本次请求下次使用。而sync模式等待请求完成后再返回，
一般依次dns请求的响应时间在20ms以内。

使用方式
===
1. 初始化DNSCache

       DNSCache.Instance.init(this.getApplicationContext(), new DNSCacheConfig.Builder().build(),
        HostResolveStrategy.SYNC);
        
2. 给okhttp设置dns和访问interceptor

       final OkHttpClient client = new OkHttpClient.Builder().dns(new HttpDNS())
        .addNetworkInterceptor
                (new DnsVisitNetInterceptor()).build();
                
3. 调用DNSCache.preLoad预加载app的主host               
                
                
***                
下一步要做单元测试和测速模块，欢迎使用，如果有bug可以随时反馈