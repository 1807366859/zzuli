package com.troublemaker.clockin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.troublemaker.clockin.entity.*;
import com.troublemaker.clockin.mapper.HomeMapper;
import com.troublemaker.clockin.mapper.SchoolMapper;
import com.troublemaker.clockin.mapper.UserMapper;
import com.troublemaker.clockin.service.ClockInService;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ClockInServiceImpl implements ClockInService {

    private final UserMapper userMapper;

    private final SchoolMapper schoolMapper;

    private final HomeMapper homeMapper;

    @Autowired
    public ClockInServiceImpl(UserMapper userMapper, SchoolMapper schoolMapper, HomeMapper homeMapper) {
        this.userMapper = userMapper;
        this.schoolMapper = schoolMapper;
        this.homeMapper = homeMapper;
    }

    @Override
    public Integer addSchoolClockInfo(Map<String, String> map) {
        int r1 = 0, r2;
        User user;
        School school = new School();

        school.setRegion(map.get("region"));
        school.setArea(map.get("area"));
        school.setBuild(map.get("build"));
        school.setDorm(map.get("dorm"));
        school.setMobile(map.get("mobile"));
        school.setJtMobile(map.get("jt_mobile"));
        school.setLat(map.get("lat"));
        school.setLon(map.get("lon"));
        school.setJzAddress(map.get("jz_address"));
        school.setJzProvince(map.get("jz_province"));
        school.setJzCity(map.get("jz_city"));
        school.setJzDistrict(map.get("jz_district"));

        User one = userMapper.selectOne(new QueryWrapper<User>().eq("username", map.get("username")));
        if (one == null) {
            user = new User();
            user.setUsername(map.get("username"));
            user.setPassword(map.get("password"));
            user.setEmail(map.get("email"));
            user.setClockType(Byte.parseByte(map.get("clock_Type")));
            r1 = userMapper.insert(user);
            school.setSUid(user.getUid());
        }else {
            school.setSUid(one.getUid());
        }
        r2 = schoolMapper.insert(school);
        return (r1 == 1 && r2 == 1) ? 1 : 0;
    }

    @Override
    public Integer addHomeClockInfo(Map<String, String> map) {
        int r1 = 0, r2;
        User user;
        Home home = new Home();
        home.setMobile(map.get("mobile"));
        home.setJtMobile(map.get("jt_mobile"));
        home.setLat(map.get("lat"));
        home.setLon(map.get("lon"));
        home.setJzAddress(map.get("jz_address"));
        home.setJzProvince(map.get("jz_province"));
        home.setJzCity(map.get("jz_city"));
        home.setJzDistrict(map.get("jz_district"));

        User one = userMapper.selectOne(new QueryWrapper<User>().eq("username", map.get("username")));
        if (one == null) {
            user = new User();
            user.setUsername(map.get("username"));
            user.setPassword(map.get("password"));
            user.setEmail(map.get("email"));
            user.setClockType(Byte.parseByte(map.get("clock_Type")));
            home.setSUid(user.getUid());
            r1 = userMapper.insert(user);
        }else {
            home.setSUid(one.getUid());
        }
        r2 = homeMapper.insert(home);
        return (r1 == 1 && r2 == 1) ? 1 : 0;
    }

    @Override
    public List<User> getUsers() {
        return userMapper.selectList(null);
    }

    @Override
    public School getSchoolByUserId(String uid) {
        QueryWrapper<School> wrapper = new QueryWrapper<>();
        wrapper.eq("s_uid", uid);
        return schoolMapper.selectOne(wrapper);
    }

    @Override
    public Home getHomeByUserId(String uid) {
        QueryWrapper<Home> wrapper = new QueryWrapper<>();
        wrapper.eq("s_uid", uid);
        return homeMapper.selectOne(wrapper);
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
    public SchoolInputData getSchoolInfoFromServer(CloseableHttpClient client, String url) {
        String entityStr = doGetForEntity(client, url);
        return parseObject(entityStr, SchoolInputData.class);
    }

    @Override
    public HomeInputData getHomeInfoFromServer(CloseableHttpClient client, String url) {
        String entityStr = doGetForEntity(client, url);
        return parseObject(entityStr, HomeInputData.class);
    }

    @Override
    public String SchoolFinalData(SchoolInputData schoolInputData, School school) {
        schoolInputData.setRegion(school.getRegion());
        schoolInputData.setArea(school.getArea());
        schoolInputData.setBuild(school.getBuild());
        schoolInputData.setDorm(school.getDorm());
        schoolInputData.setMobile(school.getMobile());
        schoolInputData.setJtMobile(school.getJtMobile());
        schoolInputData.setLat(school.getLat());
        schoolInputData.setLon(school.getLon());
        schoolInputData.setGcjLat(school.getLat());
        schoolInputData.setGcjLon(school.getLon());
        schoolInputData.setJzAddress(school.getJzAddress());
        schoolInputData.setJzProvince(school.getJzProvince());
        schoolInputData.setJzCity(school.getJzCity());
        schoolInputData.setJzDistrict(school.getJzDistrict());
        return toJSONString(schoolInputData);
    }

    @Override
    public String HomeFinalData(HomeInputData homeInputData, Home home) {
        homeInputData.setMobile(home.getMobile());
        homeInputData.setJtMobile(home.getJtMobile());
        homeInputData.setLat(home.getLat());
        homeInputData.setLon(home.getLon());
        homeInputData.setGcjLat(home.getLat());
        homeInputData.setGcjLon(home.getLon());
        homeInputData.setJzAddress(home.getJzAddress());
        homeInputData.setJzProvince(home.getJzProvince());
        homeInputData.setJzCity(home.getJzCity());
        homeInputData.setJzDistrict(home.getJzDistrict());
        return toJSONString(homeInputData);
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
