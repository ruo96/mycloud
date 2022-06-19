package org.wrh.cloud.user.event.springbootEvent.sync.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.wrh.cloud.user.event.springbootEvent.event.UserDTO;

/**
 * 定义事件监听器，可以通过注解或者实现接口来实现。
 */
@Slf4j
@Component
public class UserRegisterSmsListener{

	// 通过注解实现监听器
    @EventListener
    public void handleUserEvent(UserDTO userDTO){

        log.info("[spring-event-listener == sms]>>> 监听到用户注册，准备发送--短信，user:"+userDTO.toString());
    }
}