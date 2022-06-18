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
