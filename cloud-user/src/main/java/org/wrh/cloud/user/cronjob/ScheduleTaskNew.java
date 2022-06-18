package org.wrh.cloud.user.cronjob;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.wrh.cloud.user.config.DynamicConfig;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class ScheduleTaskNew implements SchedulingConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTaskNew.class);


    @Autowired
    private DynamicConfig dynamicConfig;

    /**
     * 自动通知
     */
    // @Scheduled(cron = "${schedule.notify}")
    // @Scheduled(cron = dynamicConfig.get)
    public void notifyJob() {
        if (!dynamicConfig.getEnabled()) {
            logger.info("[cronjob]>>> notifyJob FALSE   time: {}", LocalDateTime.now().toString());
            return;
        }
        logger.info("[crontab]>>> notifyJob TRUE   time: {}", LocalDateTime.now().toString());

        // Flux<String> just = Flux.just("123");

    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //执行任务 使用addTriggerTask 方法
        taskRegistrar.addTriggerTask(
                //实现自己的调度任务业务逻辑
                () -> notifyJob(), //业务逻辑
                //实现触发器逻辑
                triggerContext -> {
                    String cron = dynamicConfig.getNotify(); //从nacos中取动态的cron表达式
                    if(StringUtils.isBlank(cron)){
                        cron = "0/5 * * * * ?"; //默认配置:每2min执行一次
                    }
                    logger.info(" CRON :{}", cron);
                    //这边获取动态配置后执行 计算下次促发时间
                    CronTrigger trigger = new CronTrigger(cron);
                    Date nextExec = trigger.nextExecutionTime(triggerContext);
                    return nextExec;
                });
    }

}
