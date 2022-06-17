package org.wrh.cloud.gateway.RoutePredicateFactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.wrh.cloud.common.constants.Constant;

import java.util.function.Predicate;

/**
 * @author wuruohong
 * @date 2022-06-17 16:07
 */
@Slf4j
@Component
public class CheckAuthRoutePredicateFactory extends AbstractRoutePredicateFactory<CheckAuthRoutePredicateFactory.Config> {

    // 构造函数， 声明断言对象
    public CheckAuthRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            log.info("[CheckAuthRoutePredicateFactory]>>> enter ,name: {}",config.getName());
            return config.getName().equals("killer");
        };
    }

    public static class Config{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
