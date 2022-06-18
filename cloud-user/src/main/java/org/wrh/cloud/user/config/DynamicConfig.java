package org.wrh.cloud.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuruohong
 * @date 2022-06-19 2:16
 */
@RefreshScope
@Configuration
@Data
public class DynamicConfig {

    @Value("${schedule.enabled1}")
    private Boolean enabled;

    @Value("${schedule.notify}")
    private String notify;
}
