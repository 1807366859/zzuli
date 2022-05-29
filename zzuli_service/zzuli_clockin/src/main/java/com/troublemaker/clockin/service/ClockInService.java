package com.troublemaker.clockin.service;

import com.troublemaker.clockin.entity.*;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;


import java.util.List;
import java.util.Map;

/**
 * @author Troublemaker
 * @date 2022- 04 28 21:08
 */
public interface ClockInService {

    Integer addSchoolClockInfo(Map<String,String> map);

    Integer addHomeClockInfo(Map<String,String> map);

    List<User> getUsers();

    School getSchoolByUserId(String uid);

    Home getHomeByUserId(String uid);

    Map<String, String> loginMap(User user, String lt);

    String getLt(CloseableHttpClient client, String url);

    void login(CloseableHttpClient client, String url, Map<String, String> map);

    String getCodeLink(CloseableHttpClient client, String url);

    String getToken(CloseableHttpClient client, String url);

    SchoolInputData getSchoolInfoFromServer(CloseableHttpClient client, String url);

    HomeInputData getHomeInfoFromServer(CloseableHttpClient client, String url);

    String SchoolFinalData(SchoolInputData data, School school);

    String HomeFinalData(HomeInputData data, Home home);

    String submitData(CloseableHttpClient client, String url, String params, Header header);
}
