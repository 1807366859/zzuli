package com.troublemaker.order.excute;

import com.troublemaker.order.entity.Booker;
import com.troublemaker.order.entity.FieldInfo;
import com.troublemaker.order.service.FieldSelectionService;
import com.troublemaker.order.thread.OrderTask;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Troublemaker
 * @date 2022- 04 30 22:57
 */
@Component
public class DoOrderTask {
    private FieldSelectionService selectionService;

    @Autowired
    public void setSelectionService(FieldSelectionService selectionService) {
        this.selectionService = selectionService;
    }

    public void start() {
        List<FieldInfo> fieldInfos = selectionService.getSelfSetFieldInfos();
        List<Booker> bookers = selectionService.getBookers();
        // 数量较少，有多少数据创建多少线程
        ExecutorService executor = Executors.newFixedThreadPool(bookers.size());
        final CountDownLatch countDownLatch = new CountDownLatch(bookers.size());
        for (int i = 0; i < bookers.size(); i++) {
            Booker booker = bookers.get(i);
            FieldInfo fieldInfo = fieldInfos.get(i);
            executor.execute(new OrderTask(booker, countDownLatch, selectionService, fieldInfo));
        }
        try {
            countDownLatch.await();
        } catch (Exception ignored) {

        } finally {
            executor.shutdown();
        }
    }
}
