package org.wrh.cloud.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.user.service.EventService;

import java.time.LocalDateTime;

/**
 * @author wuruohong
 * @date 2022-06-19 10:30
 */
@RequestMapping("event/")
@RestController
public class EventPublishController {

    private static final Logger log = LoggerFactory.getLogger(EventPublishController.class);

    @Autowired
    private EventService eventService;

    @GetMapping("publish/{name}")
    public ReturnResult publishEvent(@PathVariable("name") String name) {
        log.info("[eventPublishController]>>> name: {}  {}", name, LocalDateTime.now().toString());
        return eventService.publishEvent(name);
    }
}
