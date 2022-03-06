package com.atguigu.statisticsservice.schedule;

import com.atguigu.statisticsservice.service.StatisticsDailyService;
import com.atguigu.statisticsservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务调度
 */
@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    // 每天凌晨一点定时执行
    @Scheduled(cron = "0 0 1 * * ?")
    public void addDataTask() {
        // 通过工具类获取上一天的时间
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        statisticsDailyService.countRegister(day);
    }
}
