package com.troublemaker.order.service;

import com.troublemaker.order.entity.FieldInfo;
import com.troublemaker.order.entity.FieldType;
import com.troublemaker.order.entity.Booker;
import com.troublemaker.order.entity.TimePeriod;
import com.troublemaker.order.exception.MyException;
import org.apache.http.client.HttpClient;

import java.util.List;
import java.util.Map;

/**
 * @author Troublemaker
 * @date 2022- 04 29 10:26
 */

public interface FieldSelectionService {
    Integer addBooker(Booker booker);

    List<Booker> getBookers();

    Map<String, String> bookerToMap(Booker booker);

    //登录认证
    String getLt(HttpClient client, String url);

    void login(HttpClient client, String url, Map<String, String> map);

    String getHomePage(HttpClient client, String url);

    //    private String selectUrl ="http://cgyy.zzuli.edu.cn/Field/GetVenueState?dateadd=0&TimePeriod=0&VenueNo=001&FieldTypeNo=03&_=";
    List<FieldInfo> getOptionalFieldInfo(HttpClient client, FieldType fieldTypeNo, TimePeriod timePeriod);

    String orderInvariableField(HttpClient client, FieldInfo fieldInfo);

    String orderChangeableField(HttpClient client, List<FieldInfo> list);

    String order(HttpClient client, String checkData) throws MyException;

    String subMit(HttpClient client, String cardNo, String OID);

    String getOrdered(HttpClient client);
}
