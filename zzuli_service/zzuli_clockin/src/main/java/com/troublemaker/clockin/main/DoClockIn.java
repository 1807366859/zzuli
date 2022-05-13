package com.troublemaker.clockin.main;

import com.troublemaker.clockin.entity.InputData;
import com.troublemaker.clockin.entity.User;
import com.troublemaker.clockin.service.ClockInService;
import com.troublemaker.utils.mail.SendMail;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.troublemaker.utils.encryptionutils.EncryptionUtil.getAesToUrl;
import static com.troublemaker.utils.httputils.HttpClientUtils.getClientNoSSL;
import static com.troublemaker.utils.httputils.HttpClientUtils.getHeader;

/**
 * @author Troublemaker
 * @date 2022- 04 28 22:38
 */
@Component
public class DoClockIn {
    @Autowired
    private ClockInService service;
    @Autowired
    private SendMail sendMail;

    private static final String loginUrl = "http://kys.zzuli.edu.cn/cas/login";
    private static final String codeUrl = "https://msg.zzuli.edu.cn/xsc/week?spm=1";
    private static final String addUrl = "https://msg.zzuli.edu.cn/xsc/add";
    private static final String historyUrl = "https://msg.zzuli.edu.cn/xsc/log?type=0&code=";

    public void start() {
        List<User> users = service.getUsers();
        for (User user : users) {
            try {
                HttpClient client = getClientNoSSL();
                //登录
                user.setLt(service.getLt(client, loginUrl));
                service.login(client, loginUrl, service.userToMap(user));

                //获得打卡链接https://msg.zzuli.edu.cn/morn/view?from=&code=8003df88*****0e7a2d63
                String link = service.getCodeLink(client, codeUrl);

                //获得TOKEN
                String token = service.getToken(client, link);
                Header header = getHeader("X-XSRF-TOKEN", token);

                //从服务器获得打卡数据
                //https://msg.zzuli.edu.cn/xsc/get_user_info?code=8003df88*****0e7a2d63&wj_type=1
                //拼接链接
                String userInfoUrl = "https://msg.zzuli.edu.cn/xsc/get_user_info?wj_type=1";
                userInfoUrl += link.substring(link.lastIndexOf("&"));
                //获取数据
                InputData inputData = service.getInfoFromServer(client, userInfoUrl);

                //从数据库取其他字段数据
                String finalData = service.finalData(inputData, user);
                System.out.println(finalData);
                //提交到服务器
                String clockInfo = service.submitData(client, addUrl, finalData, header);
                System.out.println(clockInfo);

                //查看填报历史
                //https://msg.zzuli.edu.cn/xsc/log?type=0&code=
                String aesToUrl = getAesToUrl(user.getUsername(), historyUrl);

                //邮件文本
                String text = "打卡成功：" + aesToUrl;
                //发送邮件
                if (parseObject(clockInfo).get("message").equals("ok")) {
                    sendMail.sendSimpleMail(user.getEmail(), text);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
