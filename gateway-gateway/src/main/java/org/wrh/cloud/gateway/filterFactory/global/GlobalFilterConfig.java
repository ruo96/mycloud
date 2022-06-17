package org.wrh.cloud.gateway.filterFactory.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author wuruohong
 * @date 2022-06-17 17:35
 * 这个例子主要是用来看顺序的， 实际中只需要一个全局过滤器即可
 */
@Slf4j
// @Configuration
public class GlobalFilterConfig {
    @Bean
    @Order(-1)
    public GlobalFilter a() throws Exception {
        return ((exchange, chain) -> {
            log.info("[GlobalFilter - a  forward]>>> time: {}", LocalDateTime.now().toString());
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("[GlobalFilter - a  back]>>> time: {}", LocalDateTime.now().toString());
            }));
        });
    }

    @Bean
    @Order(1)
    public GlobalFilter b() {
        return ((exchange, chain) -> {
            log.info("[GlobalFilter - b  forward]>>> time: {}", LocalDateTime.now().toString());
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("[GlobalFilter - b  back]>>> time: {}", LocalDateTime.now().toString());
            }));
        });
    }

    @Bean
    @Order(2)
    public GlobalFilter c() {
        return ((exchange, chain) -> {
            log.info("[GlobalFilter - c  forward]>>> time: {}", LocalDateTime.now().toString());
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("[GlobalFilter - c  back]>>> time: {}", LocalDateTime.now().toString());
            }));
        });
    }
}
