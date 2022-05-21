package com.troublemaker.order.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.troublemaker.order.entity.FieldInfo;
import com.troublemaker.order.entity.FieldType;
import com.troublemaker.order.entity.Booker;
import com.troublemaker.order.entity.TimePeriod;
import com.troublemaker.order.exception.MyException;
import com.troublemaker.order.mapper.BookerMapper;
import com.troublemaker.order.service.FieldSelectionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static com.alibaba.fastjson.JSON.*;
import static com.troublemaker.order.OrderRun.startTime;
import static com.troublemaker.utils.encryptionutils.EncryptionUtil.getBase64Password;
import static com.troublemaker.utils.httputils.HttpClientUtils.*;

/**
 * @author Troublemaker
 * @date 2022- 04 25 17:55
 */
@Service
@Slf4j
public class FieldSelectionServiceImpl extends ServiceImpl<BookerMapper, Booker> implements FieldSelectionService {

    @Override
    public Integer addBooker(Booker booker) {
        return baseMapper.insert(booker);
    }

    @Override
    public List<Booker> getBookers() {
        return baseMapper.selectList(null);
    }

    @Override
    public Map<String, String> loginMap(Booker booker, String lt) {
        //Base64加密
        booker.setPassword(getBase64Password(booker.getPassword()));
        //转为map
        HashMap<String, String> loginMap = new HashMap<>(1);
        loginMap.put("username", booker.getUsername());
        loginMap.put("password", booker.getPassword());
        loginMap.put("secret", "");
        loginMap.put("accountLogin", "");
        loginMap.put("lt", lt);
        loginMap.put("execution", "e1s1");
        loginMap.put("_eventId", "submit");
        return loginMap;
    }

    @Override
    public String getLt(HttpClient client, String url) {
        String entityStr = doGetForEntity(client, url);
        return Jsoup.parse(entityStr).select("[name='lt']").attr("value");
    }

    @Override
    public void login(HttpClient client, String url, Map<String, String> map) {
        doApplicationPost(client, url, map);
    }

    @Override
    public void getHomePage(HttpClient client, String url) {
        doGetForEntity(client, url);
    }

    @Override
    public List<FieldInfo> getOptionalFieldInfo(HttpClient client, FieldType fieldTypeNo, TimePeriod timePeriod) throws NullPointerException {
        String selectUrl = "http://cgyy.zzuli.edu.cn/Field/GetVenueState?dateadd=0&TimePeriod=" +
                timePeriod.getTimePeriodNo() +
                "&VenueNo=001&FieldTypeNo=" +
                fieldTypeNo.getFieldTypeNo() +
                "&_=" + System.currentTimeMillis();
        String entityStr = doGetForEntity(client, selectUrl);
        String resultData = parseObject(entityStr).getString(("resultdata"));
        List<FieldInfo> list = parseArray(resultData, FieldInfo.class);
        list.removeIf(fieldInfo -> fieldInfo.getFieldState() != 0 || fieldInfo.getTimeStatus() != 1);
        if (list.size() == 0) {
            throw new NullPointerException("当前无可选场所，请重试。");
        }
        return list;
    }

    @Override
    public String objToJsonString(FieldInfo fieldInfo) {
        String selectStr = null;
        String str = "[" + JSONObject.toJSONString(fieldInfo) + "]";
        try {
            selectStr = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return selectStr;
    }

    @Override
    public String orderInvariableField(FieldInfo fieldInfo) {
        return objToJsonString(fieldInfo);
    }

    @Override
    public String orderChangeableField(List<FieldInfo> list) {
        return objToJsonString(list.get(list.size() - 1));
    }


    @Override
    public String order(HttpClient client, String checkData) throws MyException {
        String url = "http://cgyy.zzuli.edu.cn/Field/OrderField?dateadd=0&VenueNo=001&checkdata=" + checkData;
        long endTime = System.currentTimeMillis();
        long difference = timeDifference(url) - (endTime - startTime);
        try {
            Thread.sleep(difference > 0 ? difference : 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String jsonStr = doGetForEntity(client, url);
        JSONObject jsonObject = parseObject(jsonStr);
        String resultData = "resultdata";
        if (jsonObject.get(resultData) == null) {
            throw new MyException("message: " + jsonObject.get("message"));
        }
        return jsonObject.get(resultData).toString();
    }

    @Override
    public String subMit(HttpClient client, String cardNo, String oId) {
        String url = "http://cgyy.zzuli.edu.cn/Field/CardPay?" +
                "PayNo=02" +
                "&Money=0" +
                "&CardMoney=1" +
                "&Count=0.00" +
                "&MemberNo=" +
                "&CardNo=" + cardNo +
                "&BillType=100" +
                "&Password=" +
                "&IsCheckPassword=0" +
                "&OID=" + oId +
                "&VenueNo=001" +
                "&PayDiscount=100" +
                "&IsUseMemberType=1" +
                "&EWMNum=1" +
                "&_=" + System.currentTimeMillis();
        JSONObject jsonObject = parseObject(doGetForEntity(client, url));
        return jsonObject.get("message").toString();
    }

    @Override
    public String getOrdered(HttpClient client) {
        String url = "http://cgyy.zzuli.edu.cn/Field/GetFieldOrder?PageNum=1&PageSize=6&Condition=" +
                "&_=" + System.currentTimeMillis();
        String entityStr = doGetForEntity(client, url);
        JSONObject jsonObject = parseObject(entityStr);
        JSONArray datatable = (JSONArray) jsonObject.get("datatable");
        JSONObject o = (JSONObject) datatable.get(0);
        return "预约信息: " + o.get("Field").toString() + " 预约时间: " + o.get("PayTime").toString();
    }

}
