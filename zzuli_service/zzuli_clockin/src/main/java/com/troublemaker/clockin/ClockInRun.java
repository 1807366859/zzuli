package com.troublemaker.clockin;

import com.troublemaker.clockin.main.DoClockIn;
import com.troublemaker.clockin.main.DoClockInTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Troublemaker
 * @date 2022- 04 29 10:08
 */
@Component
public class ClockInRun {
    @Autowired
    private DoClockInTask doClockInTask;

    @Scheduled(cron = "0 0 0 * * ?")
    public void test0() {
        doClockInTask.start();
    }
}
