package org.wrh.cloud.gateway.filterFactory.global;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.ResponseDate;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.wrh.cloud.common.constants.Constant;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.common.enums.ResultEnum;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author wuruohong
 * @date 2022-06-17 17:45
 */
// @Component
@Slf4j
public class IpCheckFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("[IpCheckFilter]>>> enter time: {}", LocalDateTime.now().toString());
        HttpHeaders headers = exchange.getRequest().getHeaders();
        if (getIp(headers).equals(Constant.LOCAL_HOST)) {
            log.info("[IpCheckFilter]>>> ip check time: {}", LocalDateTime.now().toString());
            ServerHttpResponse response = exchange.getResponse();
            ReturnResult result = ReturnResult.fail(ResultEnum.FILTER_ERROR);
            byte[] datas = JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(datas);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type",Constant.CONTENT_TYPE_JSON_UTF8);
            return response.writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }

    private String getIp(HttpHeaders headers) {
        // 仅测试使用
        return Constant.LOCAL_HOST;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
