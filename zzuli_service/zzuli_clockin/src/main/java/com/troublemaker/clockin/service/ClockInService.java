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

    List<User> getUsers();

    Map<String, String> loginMap(User user,String lt);

    String getLt(HttpClient client, String url);

    void login(HttpClient client, String url, Map<String, String> map);

    String getCodeLink(HttpClient client, String url);

    String getToken(HttpClient client, String url);

    InputData getInfoFromServer(HttpClient client, String url);

    String finalData(InputData data, User user);

    String submitData(HttpClient client, String url, String params, Header header);
}
