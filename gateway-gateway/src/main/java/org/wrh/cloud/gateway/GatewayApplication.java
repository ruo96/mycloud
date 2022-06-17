package org.wrh.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wuruohong
 * @date 2022-06-17 13:57
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.wrh.cloud.gateway.service")
@SpringBootApplication(scanBasePackages = "org.wrh.cloud.gateway")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
