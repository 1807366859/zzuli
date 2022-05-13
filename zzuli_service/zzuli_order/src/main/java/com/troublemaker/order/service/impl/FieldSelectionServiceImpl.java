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
import org.apache.http.client.HttpClient;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static com.alibaba.fastjson.JSON.*;
import static com.troublemaker.utils.encryptionutils.EncryptionUtil.getBase64Password;
import static com.troublemaker.utils.httputils.HttpClientUtils.doGetForEntity;
import static com.troublemaker.utils.httputils.HttpClientUtils.timeDifference;

/**
 * @author Troublemaker
 * @date 2022- 04 25 17:55
 */
@Service
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
    public Map<String, String> bookerToMap(Booker booker) {
        //Base64加密
        booker.setPassword(getBase64Password(booker.getPassword()));
        //转为map
        HashMap<String, String> bookerMap = new HashMap<>();
        bookerMap.put("username", booker.getUsername());
        bookerMap.put("password", booker.getPassword());
        bookerMap.put("lt", booker.getLt());
        bookerMap.put("execution", booker.getExecution());
        bookerMap.put("_eventId", booker.get_eventId());
        bookerMap.put("secret", booker.getSecret());
        bookerMap.put("accountLogin", booker.getAccountLogin());
        return bookerMap;
    }

    //get  http://cgyy.zzuli.edu.cn/User/UserChoose?LoginType=1
    @Override
    public String getHomePage(HttpClient client, String url) {
        return doGetForEntity(client, url);
    }

    @Override
    public List<FieldInfo> getOptionalFieldInfo(HttpClient client, FieldType fieldTypeNo, TimePeriod timePeriod) throws NullPointerException {
        synchronized (this) {
            //"http://cgyy.zzuli.edu.cn/Field/GetVenueState?dateadd=0&TimePeriod=0&VenueNo=001&FieldTypeNo=03&_=";
            String selectUrl = "http://cgyy.zzuli.edu.cn/Field/GetVenueState?dateadd=0&TimePeriod=" + timePeriod.getTimePeriodNO() +
                    "&VenueNo=001&FieldTypeNo=" + fieldTypeNo.getFieldTypeNo() +
                    "&_=" + new Date().getTime();
            String entityStr = doGetForEntity(client, selectUrl);
            //获取所有场所的json信息
            String resultdata = parseObject(entityStr).getString(("resultdata"));
            //将json信息转为javabean对象集合
            List<FieldInfo> list = parseArray(resultdata, FieldInfo.class);
            list.removeIf(fieldInfo -> fieldInfo.getFieldState() != 0 || fieldInfo.getTimeStatus() != 1);
            if (list.size() == 0) {
                throw new NullPointerException("当前可选场所的个数：" + list.size() + " ,请稍后再试。");
            }
            return list;
        }

    }

    @Override
    public String orderInvariableField(HttpClient client, FieldInfo fieldInfo) {
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
    public String orderChangeableField(HttpClient client, List<FieldInfo> list) {
        String selectStr = null;
        String str = "[" + JSONObject.toJSONString(list.get(list.size() - 1)) + "]";
        try {
            selectStr = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("选择信息: " + str);
        return selectStr;
    }

    @Override
    public String order(HttpClient client, String checkData) throws MyException {
        String url = "http://cgyy.zzuli.edu.cn/Field/OrderField?dateadd=0&VenueNo=001&checkdata=" + checkData;
        //时差多久,睡眠多久
        try {
            System.out.println(timeDifference(url));
            Thread.sleep(timeDifference(url));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String jsonStr = doGetForEntity(client, url);
        JSONObject jsonObject = parseObject(jsonStr);
        if (jsonObject.get("resultdata") == null) {
            throw new MyException("当前场地不可用,请重试,详情: " + "ORDER: " + "message: " + jsonObject.get("message"));
        }
        return jsonObject.get("resultdata").toString();
    }

    @Override
    public String subMit(HttpClient client, String cardNo, String OID) {
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
                "&OID=" + OID +
                "&VenueNo=001" +
                "&PayDiscount=100" +
                "&IsUseMemberType=1" +
                "&EWMNum=1" +
                "&_=" + new Date().getTime();
        JSONObject jsonObject = parseObject(doGetForEntity(client, url));
        return "OID: " + OID + ", SUBMIT: " + jsonObject.get("message").toString();
    }

    //http://cgyy.zzuli.edu.cn/Field/GetFieldPayInfo?OID=247c1a8f-0fa3-445b-8276-c5519b56e721&_=1651384536006
    @Override
    public String getOrdered(HttpClient client) {
        String url = "http://cgyy.zzuli.edu.cn/Field/GetFieldOrder?PageNum=1&PageSize=6&Condition=" +
                "&_=" + new Date().getTime();
        String entityStr = doGetForEntity(client, url);
        JSONObject jsonObject = parseObject(entityStr);
        JSONArray datatable = (JSONArray) jsonObject.get("datatable");
        JSONObject o = (JSONObject) datatable.get(0);
        return "预约信息: " + o.get("Field").toString() + " 预约时间: " + o.get("PayTime").toString();
    }

}
