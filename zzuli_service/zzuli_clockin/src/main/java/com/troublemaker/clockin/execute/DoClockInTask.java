package com.troublemaker.clockin.execute;

import com.troublemaker.clockin.entity.User;
import com.troublemaker.clockin.service.ClockInService;
import com.troublemaker.clockin.thread.ClockInTask;
import com.troublemaker.utils.mail.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author Troublemaker
 * @date 2022- 04 30 22:16
 */
@Component
public class DoClockInTask {
    private ClockInService service;
    private SendMail sendMail;

    @Autowired
    public void setService(ClockInService service) {
        this.service = service;
    }

    @Autowired
    public void setSendMail(SendMail sendMail) {
        this.sendMail = sendMail;
    }

    public void start() {
        List<User> users = service.getUsers();
        final CountDownLatch countDownLatch = new CountDownLatch(users.size());
        // 当打卡人数较少时，使用Java自带的线程池
//        ExecutorService executor = Executors.newFixedThreadPool(users.size());

        // 当打卡人数较多时，使用自定义线程池
        // corePoolSize     核心线程数
        // maximumPoolSize  最大线程数
        // keepAliveTime    空闲线程存活时间
        // unit             时间单位
        // workQueue        任务队列
        // threadFactory    线程工厂
        // handler          线程拒绝策略
        int capacity = 100;
        int corePoolSize = 20;
        int maxMumPoolSize = 50;
        int keepAliveSeconds = 5;
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(capacity);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize, maxMumPoolSize, keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

        for (User user : users) {
            // 由于刚进入的线程的执行需要和服务器时间比较
            // 可能会进行等待 导致 等待队列满 -> 创建线程—> 达到最大线程数 -> 拒绝任务执行
            // 所以对达到核心线程数的后续任务进行拦截, 待前面的任务执行完毕后即可正常执行
            // 或者直接将打卡时间推迟, 无需进行设置, 直接打卡即可
//            if (i == corePoolSize) {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

            // 或CallerRunsPolicy()策略, 即调用者执行该任务

            poolExecutor.execute(new ClockInTask(user, countDownLatch, sendMail, service));

        }
        try {
            countDownLatch.await();
        } catch (Exception ignored) {

        } finally {
            poolExecutor.shutdown();
        }
    }

}
