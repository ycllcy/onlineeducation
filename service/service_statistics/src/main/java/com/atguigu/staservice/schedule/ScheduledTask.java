package com.atguigu.staservice.schedule;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author LcY
 * @create 2022-09-17 12:28
 */
@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService service;

    // 表示每隔5秒钟执行一次
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1() {
//        System.out.println("********************task1执行了");
//    }

    // 每天凌晨一点把前一天的数据进行查询添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        service.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }
}
