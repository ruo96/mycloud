package org.wrh.cloud.user.event.springbootEvent.async.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.wrh.cloud.user.event.springbootEvent.event.UserDTO;

/**
 * 定义事件监听器，可以通过注解或者实现接口来实现。
 */
@Slf4j
@Component
public class AsyncUserRegisterSmsListener {

	// 通过注解实现监听器
    @Order(-2)
    @Async("asyncThreadPool")
    @EventListener
    public void handleUserEvent(UserDTO userDTO){

        log.info("[async-spring-event-listener == sms]>>> 异步监听到用户注册，准备发送--短信，user: {}", userDTO.toString());
    }
}