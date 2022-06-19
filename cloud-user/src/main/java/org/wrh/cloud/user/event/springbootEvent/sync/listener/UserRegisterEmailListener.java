package org.wrh.cloud.user.event.springbootEvent.sync.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.wrh.cloud.user.event.springbootEvent.event.UserDTO;

/**
 *  * 指定监听器的顺序 *
 *  * 监听器的发布顺序是按照 bean 自然装载的顺序执行的，Spring 支持两种方式来实现有序 *
 *  * 一、实现SmartApplicationListener接口指定顺序。 *
 *  * 把上面三个Listener都改成实现SmartApplicationListener接口，并指定getOrder的返回值，返回值越小，优先级越高。
 *
 *  二、 使用注解
 *  @Order(-2)
 *     @EventListener
 */
@Slf4j
@Component
public class UserRegisterEmailListener implements ApplicationListener<UserDTO> {
    @Override
    public void onApplicationEvent(UserDTO userDTO){
        log.info("[spring-event-listener == email]>>> 监听到用户注册，准备发送--邮件，user:"+userDTO.toString());
    }
}