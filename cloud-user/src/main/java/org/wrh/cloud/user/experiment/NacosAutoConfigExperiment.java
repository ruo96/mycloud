package org.wrh.cloud.user.experiment;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author wuruohong
 * @date 2022-06-18 10:57
 */
public class NacosAutoConfigExperiment {
    public static void main(String[] args) throws NacosException, IOException {
        String serverAddr = "localhost";
        String dataId = "appA";
        String group = "DEFAULT_GROUP";
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);
        String content = configService.getConfig(dataId, group, 5000);
        System.out.println("content = " + content);

        configService.addListener(dataId, group, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                System.out.println("currentTime: " + LocalDateTime.now() + ",  receive: " + configInfo);
            }
        });

        int i = System.in.read();
    }

    public static void main1(String[] args) {
        Hashtable<String, String> table = new Hashtable<>();
        System.out.println("table.put(\"w1\",\"r1\") = " + table.put("w1", "r1"));
        System.out.println("table.put(\"w1\",\"r2\") = " + table.put("w1", "r2"));
        System.out.println("table.get(\"w1\") = " + table.get("w1"));
    }

    @Test
    public void Test(){
        Integer i = 5;
        Integer j = 9;
        System.out.println("i.compareTo(j) = " + i.compareTo(j));

        DelayQueue queue = new DelayQueue();
        queue.offer(new Delayed() {
            @Override
            public long getDelay(TimeUnit unit) {
                return 0;
            }

            @Override
            public int compareTo(Delayed o) {
                return 0;
            }
        });

    }
}
