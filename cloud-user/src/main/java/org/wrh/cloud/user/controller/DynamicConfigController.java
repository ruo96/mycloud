package org.wrh.cloud.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.user.service.DynamicConfigService;

import java.time.LocalDateTime;

/**
 * @author wuruohong
 * @date 2022-06-17 23:32
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("dc/")
public class DynamicConfigController {

    @Value("${hobby}")
    private String hobby;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @GetMapping("get")
    public ReturnResult getDynamicConfig() {
        log.info("[]>>> get dc: {}  hobby: {}", LocalDateTime.now().toString(), hobby);
        return dynamicConfigService.getDynamicConfig();
    }
}
