package com.troublemaker.order.thread;

import com.troublemaker.order.entity.Booker;
import com.troublemaker.order.entity.FieldInfo;
import com.troublemaker.order.service.FieldSelectionService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

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
    private FieldInfo fieldInfo;
    private static final String LOGIN_URL = "http://kys.zzuli.edu.cn/cas/login";
    private static final String HOME_URL = "http://cgyy.zzuli.edu.cn/User/UserChoose?LoginType=1";

    public OrderTask(Booker booker, CountDownLatch countDownLatch, FieldSelectionService selectionService, FieldInfo fieldInfo) {
        this.booker = booker;
        this.countDownLatch = countDownLatch;
        this.selectionService = selectionService;
        this.fieldInfo = fieldInfo;
    }

    @Override
    public void run() {

        try {
            CloseableHttpClient client = getClientNoSSL();

            // 登录
            String lt = selectionService.getLt(client, LOGIN_URL);
            selectionService.login(client, LOGIN_URL, selectionService.loginMap(booker, lt));

            // 获取cookie
            selectionService.getHomePage(client, HOME_URL);

            // 对预定场所进行处理
            String orderFieldJson = selectionService.orderInvariableField(fieldInfo);

            // 预约
            String oId = selectionService.order(client, orderFieldJson);

            // 提交
            // 重复提交，避免sql死锁导致资源释放
            String message = null;
            int count = 3;
            for (int i = 0; i < count; i++) {
                message = selectionService.subMit(client, booker.getUsername(), oId);
                if ("预订成功！".equals(message)) {
                    break;
                } else {
                    Thread.sleep(2000);
                }
            }
            log.info(booker.getUsername() + " " + fieldInfo.getFieldName() + " 预约状态: " + message);
        } catch (Exception e) {
            log.error(booker.getUsername() + " 预约失败 " + fieldInfo.getFieldName() + " 异常信息: " + e);
        } finally {
            countDownLatch.countDown();
        }
    }
}
