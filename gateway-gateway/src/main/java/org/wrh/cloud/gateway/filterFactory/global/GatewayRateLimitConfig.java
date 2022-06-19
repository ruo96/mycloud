package org.wrh.cloud.gateway.filterFactory.global;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author wuruohong
 * @date 2022-06-19 15:06
 */
@Configuration
public class GatewayRateLimitConfig {

    @Bean
    KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }
}
