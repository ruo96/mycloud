package org.wrh.cloud.user.event.springbootEvent.sync.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.wrh.cloud.user.event.springbootEvent.event.UserDTO;

@Slf4j
@Component
public class UserRegisterMessageListener implements ApplicationListener<UserDTO> {
    @Override
    public void onApplicationEvent(UserDTO userDTO){
        log.info("[spring-event-listener == firstMessage]>>> 监听到用户注册，准备发送--首条站内短消息，user:"+userDTO.toString());
    }
}