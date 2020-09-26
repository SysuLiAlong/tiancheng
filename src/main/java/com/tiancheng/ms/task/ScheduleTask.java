package com.tiancheng.ms.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@EnableAsync
public class ScheduleTask {
    //3.添加定时任务
    @Scheduled(cron = "0 0 0/1 * * ?")
    private void produceRuleTask() {

    }


}
