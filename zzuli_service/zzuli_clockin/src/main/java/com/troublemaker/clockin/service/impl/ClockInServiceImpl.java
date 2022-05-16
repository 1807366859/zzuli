package com.troublemaker.clockin.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.troublemaker.clockin.entity.InputData;
import com.troublemaker.clockin.entity.User;
import com.troublemaker.clockin.mapper.UserMapper;
import com.troublemaker.clockin.service.ClockInService;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONString;
import static com.troublemaker.utils.encryptionutils.EncryptionUtil.getBase64Password;
import static com.troublemaker.utils.httputils.HttpClientUtils.*;

/**
 * @author Troublemaker
 * @date 2022- 04 28 21:08
 */
@Service
public class ClockInServiceImpl extends ServiceImpl<UserMapper, User> implements ClockInService {

    @Override
    public Integer addUser(User user) {
        return baseMapper.insert(user);
    }

    @Override
    public List<User> getUsers() {
        return baseMapper.selectList(null);
    }

    @Override
    public Map<String, String> userToMap(User user) {
        //Base64加密
        user.setPassword(getBase64Password(user.getPassword()));
        //转为map
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("password", user.getPassword());
        userMap.put("lt", user.getLt());
        userMap.put("execution", user.getExecution());
        userMap.put("_eventId", user.get_eventId());
        userMap.put("secret", user.getSecret());
        userMap.put("accountLogin", user.getAccountLogin());
        return userMap;
    }

    @Override
    public String getLt(HttpClient client, String url) {
        String entityStr = doGetForEntity(client, url);
        return Jsoup.parse(entityStr).select("[name='lt']").attr("value");
    }

    @Override
    public void login(HttpClient client, String url, Map<String, String> map) {
        String entityStr = doApplicationPost(client, url, map);
//        return Jsoup.parse(entityStr).select("title").html();
    }

    @Override
    public String getCodeLink(HttpClient client, String url) {
        String entityStr = doGetForEntity(client, url);
        return Jsoup.parse(entityStr).select("a[data-href]").attr("data-href");
    }

    @Override
    public String getToken(HttpClient client, String url) {
        String Headers = doGetForHeaders(client, url);
        String subHeader = Headers.substring(0, Headers.indexOf(";"));
        String token = subHeader.substring(subHeader.indexOf("="));
        return token.substring(1);
    }

    @Override
    public InputData getInfoFromServer(HttpClient client, String url) {
        String entityStr = doGetForEntity(client, url);
        return parseObject(entityStr, InputData.class);
    }

    @Override
    public String finalData(InputData inputData, User user) {
        inputData.setBuild(user.getBuild());
        inputData.setDorm(user.getDorm());
        inputData.setMobile(user.getMobile());
        inputData.setJt_mobile(user.getJt_mobile());
        return toJSONString(inputData);
    }

    @Override
    public String submitData(HttpClient client, String url, String params, Header header) {
        //时差多少睡多久
        try {
            Thread.sleep(timeDifference(url));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return doJsonPostWithHeader(client, url, params, header);
    }
}
