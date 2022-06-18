package org.wrh.cloud.common.feignService;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderFeignClientHystrixFallbackFactory implements FallbackFactory<OrderFeignClient> {

    @Override
    public OrderFeignClient create(Throwable throwable) {
        log.info("[fallback message]>>> exception:{}", throwable.getMessage());

        return new OrderFeignClientHystrixFallback(){

        };
    }
}
