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

    /**
     * @description: get  http://kys.zzuli.edu.cn/cas/login
     *获取登录所需的隐藏域lt的值
     * @author: troublemaker
     * @date:  9:49
     * @param: [client, url]
     * @return: java.lang.String
     **/
    @Override
    public String getLt(HttpClient client, String url) {
        String entityStr = doGetForEntity(client, url);
        return Jsoup.parse(entityStr).select("[name='lt']").attr("value");
    }

    /**
     * @description: post  http://kys.zzuli.edu.cn/cas/login
     * 登录认证
     * @author: troublemaker
     * @date:  9:51
     * @param: [client, url, map]
     * @return: void
     **/
    @Override
    public void login(HttpClient client, String url, Map<String, String> map) {
        doApplicationPost(client, url, map);
//        return Jsoup.parse(entityStr).select("title").html();
    }

    /**
     * @description: get  http://cgyy.zzuli.edu.cn/User/UserChoose?LoginType=1
     * 获取cookie值
     * @author: troublemaker
     * @date:  9:52
     * @param: [client, url]
     * @return: void
     **/
    @Override
    public void getHomePage(HttpClient client, String url) {
         doGetForEntity(client, url);
    }

    /**
     * @description: get "http://cgyy.zzuli.edu.cn/Field/GetVenueState?dateadd=0&TimePeriod=" +
     *                     timePeriod.getTimePeriodNO() +
     *                     "&VenueNo=001&FieldTypeNo=" +
     *                     fieldTypeNo.getFieldTypeNo() +
     *                     "&_=" + new Date().getTime();
     * 获取可选的场所信息，支持羽毛球，乒乓球等，及早，中，晚的不同组合。
     * @author: troublemaker
     * @date:  9:52
     * @param: [client, fieldTypeNo, timePeriod]
     * @return: java.util.List<com.troublemaker.order.entity.FieldInfo>
     **/
    @Override
    public List<FieldInfo> getOptionalFieldInfo(HttpClient client, FieldType fieldTypeNo, TimePeriod timePeriod) throws NullPointerException {
            String selectUrl = "http://cgyy.zzuli.edu.cn/Field/GetVenueState?dateadd=0&TimePeriod=" +
                    timePeriod.getTimePeriodNO() +
                    "&VenueNo=001&FieldTypeNo=" +
                    fieldTypeNo.getFieldTypeNo() +
                    "&_=" + new Date().getTime();
            String entityStr = doGetForEntity(client, selectUrl);
            String resultdata = parseObject(entityStr).getString(("resultdata"));
            List<FieldInfo> list = parseArray(resultdata, FieldInfo.class);
            list.removeIf(fieldInfo -> fieldInfo.getFieldState() != 0 || fieldInfo.getTimeStatus() != 1);
            if (list.size() == 0) {
                throw new NullPointerException("当前无可选场所，请重试。");
            }
            return list;
    }

    /**
     * @description: 将选择的场所java对象转化为服务器接受的json字符串
     * @author: troublemaker
     * @date:  9:55
     * @param: [fieldInfo]
     * @return: java.lang.String
     **/
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

    /**
     * @description: 当然，你也可以直接定义java对象，不需要通过服务器获取可选场地的信息，这样可以更快。
     * 格式如下: new FieldInfo("19:30", "20:30", "YMQ017", "羽毛球05-1", "03", "0.00", 1, 0)
     * @author: troublemaker
     * @date:  9:58
     * @param: [fieldInfo]
     * @return: java.lang.String
     **/
    @Override
    public String orderInvariableField(FieldInfo fieldInfo) {
        return objToJsonString(fieldInfo);
    }

    /**
     * @description: 根据获取的可选场地的集合,自定义获取对象，这里默认取集合中元素的最后一个。
     * @author: troublemaker
     * @date:  9:37
     * @param: [client, list]
     * @return: java.lang.String
     **/
    @Override
    public String orderChangeableField(List<FieldInfo> list) {
        return objToJsonString(list.get(list.size() - 1));
    }

    /**
     * @description: get "http://cgyy.zzuli.edu.cn/Field/OrderField?dateadd=0&VenueNo=001&checkdata=" + checkData;
     * @author: troublemaker
     * @date:  9:57
     * @param: [client, checkData]
     * @return: java.lang.String
     **/
    @Override
    public String order(HttpClient client, String checkData) throws MyException {
        String url = "http://cgyy.zzuli.edu.cn/Field/OrderField?dateadd=0&VenueNo=001&checkdata=" + checkData;
        //时差多久,睡眠多久
        long endTime = System.currentTimeMillis();
        long difference = timeDifference(url) - (endTime - startTime);
        try {
            Thread.sleep(difference > 0 ? difference : 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String jsonStr = doGetForEntity(client, url);
        JSONObject jsonObject = parseObject(jsonStr);
        if (jsonObject.get("resultdata") == null) {
            throw new MyException("message: " + jsonObject.get("message"));
        }
        return jsonObject.get("resultdata").toString();
    }

    /**
     * @description: get "http://cgyy.zzuli.edu.cn/Field/CardPay?" +
     *                 "PayNo=02" +
     *                 "&Money=0" +
     *                 "&CardMoney=1" +
     *                 "&Count=0.00" +
     *                 "&MemberNo=" +
     *                 "&CardNo=" + cardNo +
     *                 "&BillType=100" +
     *                 "&Password=" +
     *                 "&IsCheckPassword=0" +
     *                 "&OID=" + OID +
     *                 "&VenueNo=001" +
     *                 "&PayDiscount=100" +
     *                 "&IsUseMemberType=1" +
     *                 "&EWMNum=1" +
     *                 "&_=" + new Date().getTime();
     * @author: troublemaker
     * @date:  10:05
     * @param: [client, cardNo, OID]
     * @return: java.lang.String
     **/
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
//        return "OID: " + OID + ", SUBMIT: " + jsonObject.get("message").toString();
        return jsonObject.get("message").toString();
    }

    /**
     * @description: get "http://cgyy.zzuli.edu.cn/Field/GetFieldOrder?PageNum=1&PageSize=6&Condition=" +
     *                 "&_=" + new Date().getTime();
     * 可以获取已提交的所有订单，默认获取第一个。
     * @author: troublemaker
     * @date:  10:02
     * @param: [client]
     * @return: java.lang.String
     **/
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
