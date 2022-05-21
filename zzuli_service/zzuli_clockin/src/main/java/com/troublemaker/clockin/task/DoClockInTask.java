package com.troublemaker.clockin.task;

import com.troublemaker.clockin.entity.User;
import com.troublemaker.clockin.service.ClockInService;
import com.troublemaker.clockin.thread.ClockInTask;
import com.troublemaker.utils.mail.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Troublemaker
 * @date 2022- 04 30 22:16
 */
@Component
public class DoClockInTask {
    @Autowired
    private ClockInService service;

    @Autowired
    private SendMail sendMail;

    public void start() {
        List<User> users = service.getUsers();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        final CountDownLatch countDownLatch = new CountDownLatch(users.size());
        for (User user : users) {
            executor.execute(new ClockInTask(user, countDownLatch, sendMail, service));
        }
        try {
            countDownLatch.await();
        } catch (Exception ignored) {

        } finally {
            executor.shutdown();
        }
    }

}
