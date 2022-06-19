package org.wrh.cloud.user.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.user.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("分群用户数统计-单元测试")
    public void TestCountCrowdV2(){
        // UserServiceImpl userService1 = new UserServiceImpl();
        ReturnResult userInfo = userService.getUserInfo(123);
        System.out.println("userInfo = " + userInfo);
        Assert.assertNotNull(userInfo);
    }

}