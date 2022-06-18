package org.wrh.cloud.order.controller;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wrh.cloud.common.dto.ReturnResult;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author wuruohong
 * @date 2022-06-18 18:07
 */
@RestController
@RequestMapping("feign/")
public class OrderFeignController {

    private static final Logger log = LoggerFactory.getLogger(OrderFeignController.class);

    @GetMapping("info")
    public ReturnResult getInfo(@RequestParam("userName") String userName) throws InterruptedException {
        log.info("[order-feign-service]>>> hello :{} this is feigin controller {}", userName, LocalDateTime.now().toString());
        TimeUnit.SECONDS.sleep(20);
        return ReturnResult.success(Lists.newArrayList("hello",userName,"come here!"));
    }
}
