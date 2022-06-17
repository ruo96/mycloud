package org.wrh.cloud.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wuruohong
 * @date 2022-06-17 12:48
 */
@Slf4j
@RestController
@RequestMapping("user/")
public class UserSleepController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("sleep")
    public ReturnResult listUsers() throws InterruptedException {
        log.info("[sleep user]>>> {}", LocalDateTime.now().toString());
        TimeUnit.SECONDS.sleep(30);
        return ReturnResult.success("sleep well");
    }



}
