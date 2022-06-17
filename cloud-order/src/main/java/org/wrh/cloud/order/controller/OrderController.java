package org.wrh.cloud.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wrh.cloud.common.dto.ReturnResult;

/**
 * @author wuruohong
 * @date 2022-06-17 15:18
 */
@Slf4j
@RestController
@RequestMapping("order/")
public class OrderController {

    @GetMapping("add/{orderId}")
    public ReturnResult addOrder(@PathVariable("orderId") Integer orderId) {
        log.info("[cloud-order-service]>>> add :{}", orderId);
        return ReturnResult.success(orderId + " already add, please enjoy");
    }
}
