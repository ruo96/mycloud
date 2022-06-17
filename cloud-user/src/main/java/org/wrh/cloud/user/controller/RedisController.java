package org.wrh.cloud.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.common.service.RedisService;
import org.wrh.cloud.user.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuruohong
 * @date 2022-06-17 12:48
 */
@Slf4j
@RestController
@RequestMapping("redis/")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @GetMapping("find")
    public ReturnResult findResult(HttpServletRequest request) {
        String key = request.getParameter("key");
        log.info("[redis]>>> key: {}   time:{}", key, LocalDateTime.now().toString());
        String value = redisService.getString(key);
        if (StringUtils.isBlank(value)) {
            redisService.setString(key, "1");
        } else {
            redisService.increaseBy(key, 1L);
        }

        return ReturnResult.success(redisService.getString(key));
    }


}
