package com.troublemaker.clockin.execute;

import com.troublemaker.clockin.entity.User;
import com.troublemaker.clockin.service.ClockInService;
import com.troublemaker.clockin.thread.ClockInTask;
import com.troublemaker.utils.mail.SendMail;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author Troublemaker
 * @date 2022- 04 30 22:16
 */
@Component
@Slf4j
public class DoClockInTask {

    private final ClockInService service;
    private final SendMail sendMail;
    private final ThreadPoolExecutor poolExecutor;
    private final HttpClientBuilder clientBuilder;

    @Autowired
    public DoClockInTask(ClockInService service, SendMail sendMail, ThreadPoolExecutor poolExecutor, HttpClientBuilder clientBuilder) {
        this.service = service;
        this.sendMail = sendMail;
        this.poolExecutor = poolExecutor;
        this.clientBuilder = clientBuilder;
    }

    public void start() {
        List<User> users = service.getUsers();
        // 当打卡人数较少时，使用Java自带的线程池
//        ExecutorService executor = Executors.newFixedThreadPool(users.size());
        // 当打卡人数较多时，使用自定义全局线程池
        for (User user : users) {
            if (user.getClockType() == 2) {
                log.info(user.getUsername() + " " + "跳过打卡");
                continue;
            }
            // CallerRunsPolicy()策略, 即调用者(main)执行该任务
            poolExecutor.execute(new ClockInTask(user, sendMail, service, clientBuilder));
        }
    }

}
