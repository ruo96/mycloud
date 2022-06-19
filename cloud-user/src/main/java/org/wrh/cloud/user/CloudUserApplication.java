package org.wrh.cloud.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wuruohong
 * @date 2022-06-17 12:47
 */
@EnableAsync
@EnableScheduling
@EnableFeignClients(basePackages = "org.wrh.cloud.common.feignService")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "org.wrh.cloud")
public class CloudUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudUserApplication.class, args);

    }
}
