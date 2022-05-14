package com.troublemaker.order.main;

import com.troublemaker.order.entity.Booker;
import com.troublemaker.order.service.FieldSelectionService;
import com.troublemaker.order.thread.OrderTask;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.troublemaker.order.thread.OrderTask.setNumber;


/**
 * @author Troublemaker
 * @date 2022- 04 30 22:57
 */
@Component
public class DoOrderTask {
    @Autowired
    private FieldSelectionService selectionService;

    public void start() {
        List<Booker> bookers = selectionService.getBookers();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        final CountDownLatch countDownLatch = new CountDownLatch(bookers.size());
        for (Booker booker : bookers) {
            //spring创建的对象是无参构造
            //手动new对象,spring无法自动注入属性，须在类中自己手动new好所自动注入的属性
            executor.execute(new OrderTask(booker, countDownLatch));
        }
        try {
            countDownLatch.await();
        } catch (Exception ignored) {

        } finally {
            setNumber(bookers.size());
            executor.shutdown();
        }
    }
}
