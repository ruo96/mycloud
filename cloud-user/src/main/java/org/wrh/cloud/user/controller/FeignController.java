package org.wrh.cloud.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.common.feignService.OrderFeignClient;

import javax.annotation.Resource;
import java.time.LocalDateTime;


/**
 * @author wuruohong
 * @date 2022-06-18 17:49
 */
@RestController
@RequestMapping("user/")
public class FeignController {
    private static final Logger log = LoggerFactory.getLogger(FeignController.class);

    @Resource
    OrderFeignClient orderFeignClient;

    @GetMapping("feign/info")
    public ReturnResult getInfo(@RequestParam("name") String name) {
        log.info("[feign-user]>>> name: {}  time:{}", name, LocalDateTime.now().toString());
        ReturnResult result = orderFeignClient.getOrderList(name);
        log.info("[feign-user]>>> return: {} time:{}", result, LocalDateTime.now().toString());
        return result;

    }

}
