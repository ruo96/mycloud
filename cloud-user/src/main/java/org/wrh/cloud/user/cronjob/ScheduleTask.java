package org.wrh.cloud.user.cronjob;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.wrh.cloud.user.config.DynamicConfig;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;


// @RefreshScope
// @Component
public class ScheduleTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTask.class);

    @Value("${schedule.enabled}")
    private Boolean enabled;

    @Autowired
    private DynamicConfig dynamicConfig;

    /**
     * 自动通知
     */
    // @Scheduled(cron = "${schedule.notify}")
    // @Scheduled(cron = dynamicConfig.get)
    public void notifyJob() {
        if (!dynamicConfig.getEnabled()) {
            LOGGER.info("[cronjob]>>> enbale: {}   time: {}", enabled, LocalDateTime.now().toString());
            return;
        }
        LOGGER.info("[crontab]>>> notifyJob start   time: {}", LocalDateTime.now().toString());

        // Flux<String> just = Flux.just("123");

    }


}
