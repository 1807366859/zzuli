package com.troublemaker.order;

import com.troublemaker.order.excute.DoOrderTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Troublemaker
 * @date 2022- 04 29 10:08
 */
@Slf4j
@Component
public class OrderRun {
    private DoOrderTask doOrderTask;
    public static long startTime;

    @Autowired
    public void setDoOrderTask(DoOrderTask doOrderTask) {
        this.doOrderTask = doOrderTask;
    }

    @Scheduled(cron = "0 0 7 * * ?")
    public void doOrder1() {
        log.info("-----------------预约启动-------------------");
        startTime = System.currentTimeMillis();
        doOrderTask.start();
        log.info("-----------------预约完成-------------------");
    }
}
