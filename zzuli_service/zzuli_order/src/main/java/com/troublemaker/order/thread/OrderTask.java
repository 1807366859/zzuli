package com.troublemaker.order.thread;

import com.troublemaker.order.entity.Booker;
import com.troublemaker.order.entity.FieldInfo;
import com.troublemaker.order.exception.MyException;
import com.troublemaker.order.service.FieldSelectionService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.troublemaker.utils.httputils.HttpClientUtils.getClientNoSSL;

/**
 * @author Troublemaker
 * @date 2022- 04 30 22:51
 */
@Component
@Data
@NoArgsConstructor
@Slf4j
public class OrderTask implements Runnable {
    private Booker booker;
    private CountDownLatch countDownLatch;
    private FieldSelectionService selectionService;
    private static final String loginUrl = "http://kys.zzuli.edu.cn/cas/login";
    private static final String homeUrl = "http://cgyy.zzuli.edu.cn/User/UserChoose?LoginType=1";
    private static ArrayList<FieldInfo> fieldInfos = new ArrayList<>();
    private static int number;
    static Lock lock = new ReentrantLock();

    static {
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ017", "羽毛球05-1", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ018", "羽毛球05-2", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ019", "羽毛球05-3", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ020", "羽毛球05-4", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ021", "羽毛球06-1", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ022", "羽毛球06-2", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ023", "羽毛球06-3", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ024", "羽毛球06-4", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ025", "羽毛球07-1", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ026", "羽毛球07-2", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ027", "羽毛球07-3", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("19:30", "20:30", "YMQ028", "羽毛球07-4", "03", "0.00", 1, 0));

//        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ017", "羽毛球05-1", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ018", "羽毛球05-2", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ019", "羽毛球05-3", "03", "0.00", 1, 0));
//        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ020", "羽毛球05-4", "03", "0.00", 1, 0));
        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ021", "羽毛球06-1", "03", "0.00", 1, 0));
        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ022", "羽毛球06-2", "03", "0.00", 1, 0));
        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ023", "羽毛球06-3", "03", "0.00", 1, 0));
        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ024", "羽毛球06-4", "03", "0.00", 1, 0));
        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ025", "羽毛球07-1", "03", "0.00", 1, 0));
        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ026", "羽毛球07-2", "03", "0.00", 1, 0));
        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ027", "羽毛球07-3", "03", "0.00", 1, 0));
        fieldInfos.add(new FieldInfo("20:30", "21:30", "YMQ028", "羽毛球07-4", "03", "0.00", 1, 0));
        number = fieldInfos.size();
    }

    public OrderTask(Booker booker, CountDownLatch countDownLatch, FieldSelectionService selectionService) {
        this.booker = booker;
        this.countDownLatch = countDownLatch;
        this.selectionService = selectionService;
    }

    @Override
    public void run() {
        try {
            HttpClient client = getClientNoSSL();

            //登录
            String lt = selectionService.getLt(client, loginUrl);
            booker.setLt(lt);
            selectionService.login(client, loginUrl, selectionService.bookerToMap(booker));

            //进个人中心拿cookie
            selectionService.getHomePage(client, homeUrl);

            //对预定场所进行处理
            lock.lock();
            String orderField;
            FieldInfo info;
            try {
                info = fieldInfos.get(number - 1);
                orderField = selectionService.orderInvariableField(client, info);
                number--;
            } finally {
                lock.unlock();
            }

            //预定并提交订单
            String OID;
            try {
                //预约
                OID = selectionService.order(client, orderField);
                String message = null;
                //提交
                for (int i = 0; i < 3; i++) {
                    message = selectionService.subMit(client, booker.getUsername(), OID);
                    if (message.equals("预订成功！")) {
                        break;
                    } else {
                        Thread.sleep(2000);
                    }
                }
                log.info(booker.getUsername() + " " + info.getFieldName() + " 预约状态: " + message);
                //获取预定信息
//                System.out.println(selectionService.getOrdered(client));
            } catch (MyException e) {
                log.error(booker.getUsername() + " 预约失败 " + info.getFieldName() + " 异常信息: " + e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }
}
