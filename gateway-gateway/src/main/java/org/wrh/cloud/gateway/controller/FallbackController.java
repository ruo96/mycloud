package org.wrh.cloud.gateway.controller;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.common.enums.ResultEnum;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wuruohong
 * @date 2022-06-17 20:37
 */
@Slf4j
@RestController
public class FallbackController {
    /*@GetMapping("/fallback")
    public ReturnResult fallback() {
        log.info("[FallbackController]>>> fallback: {}", LocalDateTime.now().toString());
        return ReturnResult.fail(ResultEnum.GATEWAY_FALL_BACK_ERROR);
    }*/

    @RequestMapping("/fallback")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Map<String, Object>> fallback(ServerWebExchange exchange){
        log.info("[gateway-fallback]>>> {}", LocalDateTime.now().toString());
        Map result = new HashMap<>(4);
        result.put("code", -500);
        result.put("data", null);
        result.put("message","服务异常");

        //获取异常对象
        Exception exception = exchange.getAttribute(ServerWebExchangeUtils.HYSTRIX_EXECUTION_EXCEPTION_ATTR);
        URI uri = exchange.getRequest().getURI();
        if(exchange instanceof ServerWebExchangeDecorator) {
            //ServerWebExchange delegate
            ServerWebExchange delegate = ((ServerWebExchangeDecorator) exchange);
            uri = delegate.getRequest().getURI();
        }
        //log
        log.error("接口调用失败,URL={}",uri , exception);
        //判断异常类型
        if(exception!=null && exception instanceof HystrixTimeoutException){
            result.put("msg", "接口调用超时");
        }else if(exception!=null && exception.getMessage()!=null){
            result.put("msg", "接口调用失败："+ exception.getMessage());
        }else{
            result.put("msg", "接口调用失败");
        }

        return Mono.just(result);
    }

    @RequestMapping("/error")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Map<String, Object>> errorHandler(ServerWebExchange exchange){
        Map result = new HashMap<>(4);
        result.put("code", 404);
        result.put("data", null);
        result.put("message","URL匹配失败");

        return Mono.just(result);
    }
}
