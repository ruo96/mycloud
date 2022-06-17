package org.wrh.cloud.gateway.filterFactory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author wuruohong
 * @date 2022-06-17 17:03
 */
@Slf4j
@Component
public class LogGatewayFilterFactory extends AbstractGatewayFilterFactory<LogGatewayFilterFactory.Config> {
    /*@Override
    public GatewayFilter apply(NameValueConfig config) {
        return ((exchange, chain) -> {
            log.info("[CheckAuthGatewayFilterFactory]>>> name:{}   value:{}", config.getName(), config.getValue());
            ServerHttpRequest request = exchange.getRequest().mutate().build();
            return chain.filter(exchange.mutate().request(request).build());
        });
    }*/

    public LogGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(new String[]{"consoleLog","cacheLog"});
    }

    @Override
    public GatewayFilter apply(Config config) {
        /** 这个exchange和chain是啥*/
        return (((exchange, chain) -> {
            if (config.consoleLog) {
                log.info("[CheckAuthGatewayFilterFactory]>>> console 日志已开启");
            }
            if (config.cacheLog) {
                log.info("[CheckAuthGatewayFilterFactory]>>> cache 日志已开启");
            }

            return chain.filter(exchange);

        }));
    }

    @Data
    public static class Config{
        private boolean consoleLog;
        private boolean cacheLog;

    }
}
