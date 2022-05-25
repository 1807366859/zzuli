package com.troublemaker.clockin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.troublemaker.clockin.entity.InputData;
import com.troublemaker.clockin.entity.User;
import com.troublemaker.clockin.mapper.UserMapper;
import com.troublemaker.clockin.service.ClockInService;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONString;
import static com.troublemaker.clockin.ClockInRun.startTime;
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
    public Map<String, String> loginMap(User user, String lt) {
        //Base64加密
        user.setPassword(getBase64Password(user.getPassword()));
        //转为map
        HashMap<String, String> loginMap = new HashMap<>(1);
        loginMap.put("username", user.getUsername());
        loginMap.put("password", user.getPassword());
        loginMap.put("secret", "");
        loginMap.put("accountLogin", "");
        loginMap.put("lt", lt);
        loginMap.put("execution", "e1s1");
        loginMap.put("_eventId", "submit");
        return loginMap;
    }

    @Override
    public String getLt(CloseableHttpClient client, String url) {
        String entityStr = doGetForEntity(client, url);
        return Jsoup.parse(entityStr).select("[name='lt']").attr("value");
    }

    @Override
    public void login(CloseableHttpClient client, String url, Map<String, String> map) {
        doApplicationPost(client, url, map);
//        return Jsoup.parse(entityStr).select("title").html();
    }

    @Override
    public String getCodeLink(CloseableHttpClient client, String url) {
        String entityStr = doGetForEntity(client, url);
        return Jsoup.parse(entityStr).select("a[data-href]").attr("data-href");
    }

    @Override
    public String getToken(CloseableHttpClient client, String url) {
        String headers = doGetForHeaders(client, url);
        String subHeader = headers.substring(0, headers.indexOf(";"));
        String token = subHeader.substring(subHeader.indexOf("="));
        return token.substring(1);
    }

    @Override
    public InputData getInfoFromServer(CloseableHttpClient client, String url) {
        String entityStr = doGetForEntity(client, url);
        return parseObject(entityStr, InputData.class);
    }

    @Override
    public String finalData(InputData inputData, User user) {
        inputData.setBuild(user.getBuild());
        inputData.setDorm(user.getDorm());
        inputData.setMobile(user.getMobile());
        inputData.setJtMobile(user.getJtMobile());
        return toJSONString(inputData);
    }

    @Override
    public String submitData(CloseableHttpClient client, String url, String params, Header header) {
        //线程抵达时间
        long endTime = System.currentTimeMillis();
        //从程序启动到线程抵达所用时间与服务器的时时间的时差
        long difference = timeDifference(url) - (endTime - startTime);
        try {
            //比服务器慢多少就睡眠多少
            //否则直接执行
            Thread.sleep(difference > 0 ? difference : 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return doJsonPostWithHeader(client, url, params, header);
    }
}
