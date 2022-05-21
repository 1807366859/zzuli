package com.troublemaker.clockin;

import com.troublemaker.clockin.execute.DoClockInTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Troublemaker
 * @date 2022- 04 28 23:05
 */
@SpringBootTest
public class ClockInApplicationTests {

    @Autowired
    private DoClockInTask doClockInTask;

    @Test
    void contextLoads() {
        doClockInTask.start();
    }

}
