package org.wrh.cloud.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.user.service.UserService;

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
}
