package com.troublemaker.order;

import com.troublemaker.order.excute.DoOrderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Troublemaker
 * @date 2022- 04 29 10:08
 */
@Component
public class OrderRun {
    private DoOrderTask doOrderTask;
    public static long startTime;

    @Autowired
    public void setDoOrderTask(DoOrderTask doOrderTask) {
        this.doOrderTask = doOrderTask;
    }

    @Scheduled(cron = "0 0 7 * * ?")
    public void test1() {
        startTime = System.currentTimeMillis();
        doOrderTask.start();
    }
}
