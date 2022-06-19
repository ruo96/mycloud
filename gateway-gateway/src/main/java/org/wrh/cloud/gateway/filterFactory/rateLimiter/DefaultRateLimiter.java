package org.wrh.cloud.gateway.filterFactory.rateLimiter;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.constraints.DecimalMin;
import java.util.HashMap;
import java.util.Objects;

// @Component
public class DefaultRateLimiter extends AbstractRateLimiter<DefaultRateLimiter.Config> {

    Logger logger = LoggerFactory.getLogger(DefaultRateLimiter.class.getName());

    /**
     * 默认一秒一个请求，多了不收了~~
     */
    private final RateLimiter limiter = RateLimiter.create(1);


    public DefaultRateLimiter() {
        super(Config.class, "default-rate-limit", (ConfigurationService) null);
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        Config config = getConfig().get(routeId);
        limiter.setRate(Objects.isNull(config.getPermitsPerSecond()) ? 1 : config.getPermitsPerSecond());

        logger.info("[gateway ratelimiter]>>> Rate: {}", limiter.getRate());
        boolean isAllow = limiter.tryAcquire();

        logger.info("[gateway ratelimiter]>>> isAllow: {} , time:  {}",isAllow,  System.currentTimeMillis());
        return Mono.just(new Response(isAllow, new HashMap<>()));
    }

    @Validated
    public static class Config {

        @DecimalMin("0.1")
        private Double permitsPerSecond;


        public Double getPermitsPerSecond() {
            return permitsPerSecond;
        }

        public Config setPermitsPerSecond(Double permitsPerSecond) {
            this.permitsPerSecond = permitsPerSecond;
            return this;
        }
    }
}

// 作者：架构文摘
// 链接：https://juejin.cn/post/6844903965352525838
// 来源：稀土掘金
// 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。