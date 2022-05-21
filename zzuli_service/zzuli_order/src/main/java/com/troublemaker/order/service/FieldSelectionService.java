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

    String getLt(HttpClient client, String url);

    void login(HttpClient client, String url, Map<String, String> map);

    void getHomePage(HttpClient client, String url);

    List<FieldInfo> getOptionalFieldInfo(HttpClient client, FieldType fieldTypeNo, TimePeriod timePeriod);

    String objToJsonString(FieldInfo fieldInfo);

    String orderInvariableField(FieldInfo fieldInfo);

    String orderChangeableField(List<FieldInfo> list);

    String order(HttpClient client, String checkData) throws MyException;

    String subMit(HttpClient client, String cardNo, String OID);

    String getOrdered(HttpClient client);
}
