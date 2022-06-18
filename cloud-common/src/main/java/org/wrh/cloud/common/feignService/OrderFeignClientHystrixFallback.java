package org.wrh.cloud.common.feignService;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.common.enums.ResultEnum;

import java.time.LocalDateTime;

/**
 * @Author: wuruohong
 * @Date: 2021/12/31 10:46
 * @Version: 1.0
 * @Description: 调用人群服务熔断器
 */
@Slf4j
@Component
public class OrderFeignClientHystrixFallback implements OrderFeignClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderFeignClientHystrixFallback.class);


    @Override
    public ReturnResult getOrderList(String userName) {
        LOGGER.error("[fallback]>>> getOrderList error, user: [{}] time: {}", userName, LocalDateTime.now().toString());
        return ReturnResult.fail(ResultEnum.FAIL);
    }
}
