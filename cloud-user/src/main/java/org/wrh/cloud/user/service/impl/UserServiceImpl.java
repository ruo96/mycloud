package org.wrh.cloud.user.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wrh.cloud.common.annotation.Limit;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.user.domain.User;
import org.wrh.cloud.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuruohong
 * @date 2022-06-19 10:15
 */
// @Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public ReturnResult getUserInfo(Integer userId) {
        log.info("[getUserInfo]>>> userId:{}",userId);
        return ReturnResult.success("hello , this is userService test method");
    }

    @Limit(key = "getUseList", permitsPerSecond = 2, timeout = 3000)
    @Override
    public ReturnResult getUserList() {
        List<User> list = new ArrayList<>();
        list.add(User.builder().name("w1" + LocalDateTime.now().toString()).age(18).sex("male").build());
        list.add(User.builder().name("r1" + LocalDateTime.now().toString()).age(18).sex("female").build());
        return ReturnResult.success(list);
    }

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(4);
        for (int i = 0; i < 10; i++) {
            if (rateLimiter.tryAcquire()) {
                System.out.println("acquire " + LocalDateTime.now().toString());
            } else {
                System.out.println("not acquire " + LocalDateTime.now().toString());
            }
        }

    }
}
