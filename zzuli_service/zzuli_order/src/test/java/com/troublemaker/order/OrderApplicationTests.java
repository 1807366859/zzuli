package com.troublemaker.order;

import com.troublemaker.order.excute.DoOrderTask;
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
    private DoOrderTask doOrdertask;

    @Test
    void contextLoads() {
        doOrdertask.start();
    }

}
