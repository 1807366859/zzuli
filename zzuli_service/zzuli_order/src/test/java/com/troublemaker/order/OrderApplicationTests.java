package com.troublemaker.order;

import com.troublemaker.order.main.DoOrder;
import com.troublemaker.order.main.DoOrderTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author Troublemaker
 * @date 2022- 04 29 10:46
 */
@SpringBootTest
public class OrderApplicationTests {
    @Autowired
    private DoOrder doOrder;
    @Autowired
    private DoOrderTask doOrdertask;
    long start = 0, end = 0;

    @Test
    void contextLoads() {
        start = System.currentTimeMillis();
        doOrder.start();
        end = System.currentTimeMillis();
        System.out.println("普通方法，耗时：" + (end - start) + "ms");
    }

    @Test
    public void test1() {
        start = System.currentTimeMillis();
        doOrdertask.start();
        end = System.currentTimeMillis();
        System.out.println("多线程方法，耗时：" + (end - start) + "ms");
    }
}
