package org.wrh.cloud.gateway.RoutePredicateFactory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author wuruohong
 * @date 2022-06-17 16:07
 * 这种写法可以生效
 */
@Slf4j
@Component
public class AgeAuthRoutePredicateFactory extends AbstractRoutePredicateFactory<AgeAuthRoutePredicateFactory.Config> {

    // 构造函数， 声明断言对象
    public AgeAuthRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(new String[]{"minAge","maxAge"});
    }

    @Override
    public Predicate<ServerWebExchange> apply(Consumer<Config> consumer) {
        return super.apply(consumer);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        log.info("[AgeAuthRoutePredicateFactory]>>> enter , time:{}", LocalDateTime.now().toString());
        // 创建网关断言对象
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                MultiValueMap<String, String> queryParams = serverWebExchange.getRequest().getQueryParams();
                String age = queryParams.getFirst("age");
                log.info("[AgeAuthRoutePredicateFactory]>>> age: {}", age);
                if (StringUtils.isNotBlank(age) && age.matches("[0-9]+")) {
                    log.info("[AgeAuthRoutePredicateFactory]>>> age match: {}", age);
                    int iAge = Integer.parseInt(age);
                    if (iAge >= config.minAge && iAge < config.maxAge) {
                        /** 手动制造异常检测*/
                        // int i = 1 / 0;
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static class Config{
        private Integer minAge;
        private Integer maxAge;

        public Integer getMinAge() {
            return minAge;
        }

        public void setMinAge(Integer minAge) {
            this.minAge = minAge;
        }

        public Integer getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(Integer maxAge) {
            this.maxAge = maxAge;
        }
    }

}
