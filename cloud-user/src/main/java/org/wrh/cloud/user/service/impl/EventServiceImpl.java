package org.wrh.cloud.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.user.event.springbootEvent.event.UserDTO;
import org.wrh.cloud.user.service.EventService;

/**
 * @author wuruohong
 * @date 2022-06-19 10:31
 */
@Slf4j
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public ReturnResult publishEvent(String name) {
        log.info("[public event]>>> publish event name: {}",name);

        UserDTO userDTO = new UserDTO(this);
        userDTO.setAge(18);
        userDTO.setName(name);
        userDTO.setUserId(1001);
        log.info("[publishEvent]>>> ready publish event: {}", userDTO);
        eventPublisher.publishEvent(userDTO);

        return ReturnResult.success(userDTO);
    }

}
