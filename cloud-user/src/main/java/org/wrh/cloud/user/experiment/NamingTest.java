package org.wrh.cloud.user.experiment;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;

import java.io.IOException;
import java.util.Properties;

/**
 * @author wuruohong
 * @date 2022-06-18 16:03
 *
 * 可以看到是在长轮询的任务中，当服务端配置信息发生变更时，客户端将最新的数据获取下来之后，保存在了 CacheData 中，同时更新了该 CacheData 的 md5 值，
 * 所以当下次执行 checkListenerMd5 方法时，就会发现当前 listener 所持有的 md5 值已经和 CacheData 的 md5 值不一样了，也就意味着服务端的配置信息发生改变了，这时就需要将最新的数据通知给 Listener 的持有者。
 *
 * 至此配置中心的完整流程已经分析完毕了，可以发现，Nacos 并不是通过推的方式将服务端最新的配置信息发送给客户端的，而是客户端维护了一个长轮询的任务，
 * 定时去拉取发生变更的配置信息，然后将最新的数据推送给 Listener 的持有者。
 *
 * 拉的优势 *
 * 客户端拉取服务端的数据与服务端推送数据给客户端相比，优势在哪呢，为什么 Nacos 不设计成主动推送数据，而是要客户端去拉取呢？如果用推的方式，
 * 服务端需要维持与客户端的长连接，这样的话需要耗费大量的资源，并且还需要考虑连接的有效性，例如需要通过心跳来维持两者之间的连接。而用拉的方式，
 * 客户端只需要通过一个无状态的 http 请求即可获取到服务端的数据。
 *
 * ==============================================================================================================================
 * 总结
 *
 * Nacos 服务端创建了相关的配置项后，客户端就可以进行监听了。 *
 * 客户端是通过一个定时任务来检查自己监听的配置项的数据的，一旦服务端的数据发生变化时，客户端将会获取到最新的数据，
 * 并将最新的数据保存在一个 CacheData 对象中，然后会重新计算 CacheData 的 md5 属性的值，此时就会对该 CacheData 所绑定的 Listener 触发 receiveConfigInfo 回调。
 *
 * 考虑到服务端故障的问题，客户端将最新数据获取后会保存在本地的 snapshot 文件中，以后会优先从文件中获取配置信息的值
 *
 * 作者：逅弈
 * 链接：https://www.jianshu.com/p/38b5452c9fec
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class NamingTest {
    public static void main(String[] args) throws NacosException {

        Properties properties = new Properties();
        properties.setProperty("serverAddr", "127.0.0.1:8848");
        properties.setProperty("namespace", "public");

        NamingService naming = NamingFactory.createNamingService(properties);

        naming.registerInstance("cloud-user", "11.11.11.11", 8888, "TEST1");

        naming.registerInstance("cloud-user", "22.22.22.22", 9999, "TEST2");

        System.out.println("1  " + naming.getAllInstances("cloud-user"));

        naming.deregisterInstance("cloud-user", "11.11.11.11", 8888, "TEST1");

        System.out.println("2  " + naming.getAllInstances("cloud-user"));

        naming.subscribe("cloud-user", new EventListener() {
            @Override
            public void onEvent(Event event) {
                System.out.println("3  " + ((NamingEvent)event).getServiceName());
                System.out.println("4  " + ((NamingEvent)event).getInstances());
            }
        });

        try {
            final int read = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
