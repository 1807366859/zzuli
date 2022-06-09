package com.troublemaker.clockin.thread;

import com.troublemaker.clockin.entity.*;
import com.troublemaker.clockin.service.ClockInService;
import com.troublemaker.utils.mail.SendMail;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


import java.io.IOException;

import static com.troublemaker.utils.httputils.HttpClientUtils.*;

/**
 * @author Troublemaker
 * @date 2022- 04 30 22:06
 */
@Data
@Slf4j
public class ClockInTask implements Runnable {
    private User user;
    private SendMail sendMail;
    private ClockInService service;
    private HttpClientBuilder clientBuilder;
    private CloseableHttpClient client = null;
    private String loginUrl = "http://kys.zzuli.edu.cn/cas/login";
    private String codeUrl = "https://msg.zzuli.edu.cn/xsc/week?spm=";
    private String addUrl = "https://msg.zzuli.edu.cn/xsc/add";
    private String userInfoUrl = "https://msg.zzuli.edu.cn/xsc/get_user_info?wj_type=";
//    private static final String HISTORY_URL = "https://msg.zzuli.edu.cn/xsc/log?type=0&code=";

    public ClockInTask(User user, SendMail sendMail, ClockInService service, HttpClientBuilder clientBuilder) {
        this.user = user;
        this.sendMail = sendMail;
        this.service = service;
        this.clientBuilder = clientBuilder;
        codeUrl += user.getClockType();
        userInfoUrl += user.getClockType();
    }

    @Override
    public void run() {
        try {
            client = clientBuilder.build();
            // 登录
            String lt = service.getLt(client, loginUrl);
            service.login(client, loginUrl, service.loginMap(user, lt));

            // 获得含有code的链接，code=8055141d21s21sd411dd63
            String link = service.getCodeLink(client, codeUrl);

            // 获得TOKEN
            String token = service.getToken(client, link);
            Header header = getHeader("X-XSRF-TOKEN", token);

            // 将code拼接到url上
            userInfoUrl += link.substring(link.lastIndexOf("&"));

            String inputData;
            if (1 == user.getClockType()) {
                // 从服务器获得打卡数据
                SchoolInputData schoolInputData = service.getSchoolInfoFromServer(client, userInfoUrl);
                // 填充其他字段数据
                School school = service.getSchoolByUserId(user.getUid());
                inputData = service.SchoolFinalData(schoolInputData, school);
            } else {
                HomeInputData homeInputData = service.getHomeInfoFromServer(client, userInfoUrl);
                Home home = service.getHomeByUserId(user.getUid());
                inputData = service.HomeFinalData(homeInputData, home);
            }
            // 提交到服务器
            int count = 0;
            while (true) {
                count++;
                String clockInfo = service.submitData(client, addUrl, inputData, header);
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
            log.error("异常: " + e);
            sendMail.sendSimpleMail(user.getEmail(), "由于不可抗力影响😤,打卡失败😅,请自行打卡🙌");
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error("异常: " + e);
                }
            }
        }
    }
}

