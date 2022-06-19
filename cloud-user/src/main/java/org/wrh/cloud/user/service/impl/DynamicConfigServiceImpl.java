package org.wrh.cloud.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.user.service.DynamicConfigService;

/**
 * @author wuruohong
 * @date 2022-06-17 23:35
 */
@Slf4j
@Service
@RefreshScope
public class DynamicConfigServiceImpl implements DynamicConfigService {

    @Value("${user.hobby}")
    private String hobby;

    @Value("${hobby}")
    private String hobby1;

    @Override
    public ReturnResult getDynamicConfig() {
        log.info("[getDynamicConfig]>>> hobby: {}", hobby);
        log.info("[getDynamicConfig]>>> hobby1: {}", hobby1);
        return ReturnResult.success(hobby1);
    }
}


