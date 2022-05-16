package com.troublemaker.clockin.service;

import com.troublemaker.clockin.entity.InputData;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import com.troublemaker.clockin.entity.User;


import java.util.List;
import java.util.Map;

/**
 * @author Troublemaker
 * @date 2022- 04 28 21:08
 */
public interface ClockInService {
    Integer addUser(User user);

    //获取User信息
    List<User> getUsers();

    //对User信息进行处理
    //对密码进行Base64加密
    //将User转为Map
    Map<String, String> userToMap(User user);

    //登录认证
    String getLt(HttpClient client, String url);

    void login(HttpClient client, String url, Map<String, String> map);

    //通过打卡界面得到打卡链接
    String getCodeLink(HttpClient client, String url);

    //进入填写界面拿到 TOKEN:XSRF-TOKEN
    String getToken(HttpClient client, String url);

    //进入历史数据进行填充
    InputData getInfoFromServer(HttpClient client, String url);

    //从数据库拿到其他字段
    String finalData(InputData data, User user);

    //添加信息
    String submitData(HttpClient client, String url, String params, Header header);
}
