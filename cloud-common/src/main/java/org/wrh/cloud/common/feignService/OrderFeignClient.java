package org.wrh.cloud.common.feignService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wrh.cloud.common.dto.ReturnResult;

/**
 * @author wuruohong
 * @date 2022-06-18 17:37
 */
// @FeignClient(name = "bi-crowd",
//         configuration = FeignConfiguration.class,
//         fallbackFactory = CrowdAnalyseFeignClientHystrixFallbackFactory.class)
@FeignClient(name = "cloud-order")
public interface OrderFeignClient {

    @GetMapping(value = "/feign/info")
    public ReturnResult getOrderList(@RequestParam("userName") String userName);
}
