package org.wrh.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author wuruohong
 * @date 2022-06-17 0:27
 */
@EnableZuulProxy
@RibbonClient("zuul")
@SpringBootApplication(scanBasePackages = "org.wrh.cloud")
public class ZuulGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class,args);
    }
}
