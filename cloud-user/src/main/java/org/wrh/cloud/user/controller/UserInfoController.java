package org.wrh.cloud.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.user.domain.User;
import org.wrh.cloud.user.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuruohong
 * @date 2022-06-17 12:48
 */
// @Slf4j
@RestController
@RequestMapping("user/")
public class UserInfoController {
    
    private static final Logger log = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @GetMapping("list")
    public ReturnResult listUsers() {
        log.info("[list user]>>> {}", LocalDateTime.now().toString());
        List<User> list = new ArrayList<>();
        list.add(User.builder().name("w1").age(18).sex("male").build());
        list.add(User.builder().name("r1").age(18).sex("female").build());
        return ReturnResult.success(list);
    }


    @GetMapping("order/{str}")
    public String addOrder(@PathVariable String str) {
        log.info("[list user]>>> {}", LocalDateTime.now().toString());
        return restTemplate.getForObject("http://cloud-order/order/add/" + str, String.class);
    }

    @GetMapping("hold/{str}")
    public String hold(@PathVariable String str) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        HttpServletRequest request = sra.getRequest();

        log.info("[list user]>>> {}", LocalDateTime.now().toString());
        return restTemplate.getForObject("http://cloud-order/order/add/" + str, String.class);
    }

    @GetMapping("test/{str}")
    public ReturnResult testService(@PathVariable String str) {
        log.info("[test user]>>> {}", LocalDateTime.now().toString());
        return userService.getUserInfo(123);
    }


}
