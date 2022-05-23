package com.troublemaker.clockin;

import com.troublemaker.clockin.execute.DoClockInTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Troublemaker
 * @date 2022- 04 29 10:08
 */
@Component
@Slf4j
public class ClockInRun {

    private DoClockInTask doClockInTask;

    public static long startTime;

    @Autowired
    public void setDoClockInTask(DoClockInTask doClockInTask) {
        this.doClockInTask = doClockInTask;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void doClockIn() {
        log.info("-----------------打卡启动-------------------");
        startTime = System.currentTimeMillis();
        doClockInTask.start();
        log.info("-----------------打卡完成-------------------");
    }
}
