package com.troublemaker.order;

import com.troublemaker.order.task.DoOrderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Troublemaker
 * @date 2022- 04 29 10:08
 */
@Component
public class OrderRun {
    @Autowired
    private DoOrderTask doOrderTask;
    public static long startTime;

    @Scheduled(cron = "0 0 7 * * ?")
    public void test1() {
        startTime = System.currentTimeMillis();
        doOrderTask.start();
    }
}
