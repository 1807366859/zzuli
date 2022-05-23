package com.troublemaker.clockin.thread;

import com.troublemaker.clockin.entity.InputData;
import com.troublemaker.clockin.entity.User;
import com.troublemaker.clockin.service.ClockInService;
import com.troublemaker.utils.mail.SendMail;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;


import java.util.concurrent.CountDownLatch;

import static com.troublemaker.utils.httputils.HttpClientUtils.*;

/**
 * @author Troublemaker
 * @date 2022- 04 30 22:06
 */
@Data
@Slf4j
public class ClockInTask implements Runnable {
    private User user;
    private CountDownLatch countDownLatch;
    private SendMail sendMail;
    private ClockInService service;
    private static final String LOGIN_URL = "http://kys.zzuli.edu.cn/cas/login";
    private static final String CODE_URL = "https://msg.zzuli.edu.cn/xsc/week?spm=1";
    private static final String ADD_URL = "https://msg.zzuli.edu.cn/xsc/add";
    private static  String userInfoUrl = "https://msg.zzuli.edu.cn/xsc/get_user_info?wj_type=1";
//    private static final String HISTORY_URL = "https://msg.zzuli.edu.cn/xsc/log?type=0&code=";

    public ClockInTask(User user, CountDownLatch countDownLatch, SendMail sendMail, ClockInService service) {
        this.user = user;
        this.countDownLatch = countDownLatch;
        this.sendMail = sendMail;
        this.service = service;
    }

    @Override
    public void run() {
        try {
            HttpClient client = getClientNoSSL();

            //登录
            String lt = service.getLt(client, LOGIN_URL);
            service.login(client, LOGIN_URL, service.loginMap(user, lt));

            //获得打卡链接
            String link = service.getCodeLink(client, CODE_URL);

            //获得TOKEN
            String token = service.getToken(client, link);
            Header header = getHeader("X-XSRF-TOKEN", token);

            //拼接url
            userInfoUrl += link.substring(link.lastIndexOf("&"));

            //从服务器获得打卡数据
            InputData inputData = service.getInfoFromServer(client, userInfoUrl);

            //填充其他字段数据
            String finalData = service.finalData(inputData, user);

            //提交到服务器
            int count = 0;
            while (true) {
                count++;
                String clockInfo = service.submitData(client, ADD_URL, finalData, header);
                if ("{\"code\":0,\"message\":\"ok\"}".equals(clockInfo)) {
                    log.info(user.getUsername() + " " + clockInfo);
                    sendMail.sendSimpleMail(user.getEmail(), "🦄🦄🦄旋转木马提醒你,打卡成功💕💕💕");
                    break;
                }
                if (count == 3) {
                    sendMail.sendSimpleMail(user.getEmail(), "由于不可抗力影响😤,打卡失败😅,请自行打卡🙌");
                    break;
                }
            }
        } catch (Exception e) {
            log.info("异常: "+ e);
            sendMail.sendSimpleMail(user.getEmail(), "由于不可抗力影响😤,打卡失败😅,请自行打卡🙌");
        } finally {
            countDownLatch.countDown();
        }
    }
}
