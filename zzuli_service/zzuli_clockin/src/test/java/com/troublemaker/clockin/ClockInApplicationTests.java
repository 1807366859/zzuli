package com.troublemaker.clockin;

import com.troublemaker.clockin.main.DoClockIn;
import com.troublemaker.clockin.main.DoClockInTask;
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
    private DoClockIn clockIn;
    @Autowired
    private DoClockInTask doClockInTask;
    long start = 0, end = 0;

    @Test
    void contextLoads() {
        start = System.currentTimeMillis();
        clockIn.start();
        end = System.currentTimeMillis();
        System.out.println("普通方法，耗时：" + (end - start) + "ms");
    }

    @Test
    void test1() {
        start = System.currentTimeMillis();
        doClockInTask.start();
        end = System.currentTimeMillis();
        System.out.println("多线程方法，耗时：" + (end - start) + "ms");
    }
}
