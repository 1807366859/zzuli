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

    Map<String, String> loginMap(Booker booker, String lt);

    /**
     * @description: get  http://kys.zzuli.edu.cn/cas/login
     * 获取登录所需的隐藏域lt的值
     * @author: troublemaker
     * @date: 9:49
     * @param: [client, url]
     * @return: java.lang.String
     **/
    String getLt(HttpClient client, String url);

    /**
     * @description: post  http://kys.zzuli.edu.cn/cas/login
     * 登录认证
     * @author: troublemaker
     * @date: 9:51
     * @param: [client, url, map]
     * @return: void
     **/
    void login(HttpClient client, String url, Map<String, String> map);

    /**
     * @description: get  http://cgyy.zzuli.edu.cn/User/UserChoose?LoginType=1
     * 获取cookie值
     * @author: troublemaker
     * @date: 9:52
     * @param: [client, url]
     * @return: void
     **/
    void getHomePage(HttpClient client, String url);

    List<FieldInfo> getSelfSetFieldInfos();

    /**
     * @description: get "http://cgyy.zzuli.edu.cn/Field/GetVenueState?dateadd=0&TimePeriod=" +
     * timePeriod.getTimePeriodNO() +
     * "&VenueNo=001&FieldTypeNo=" +
     * fieldTypeNo.getFieldTypeNo() +
     * "&_=" + new Date().getTime();
     * 获取可选的场所信息，支持羽毛球，乒乓球等，及早，中，晚的不同组合。
     * @author: troublemaker
     * @date: 9:52
     * @param: [client, fieldTypeNo, timePeriod]
     * @return: java.util.List<com.troublemaker.order.entity.FieldInfo>
     **/
    List<FieldInfo> getOptionalFieldInfo(HttpClient client, FieldType fieldTypeNo, TimePeriod timePeriod);

    /**
     * @description: 将选择的场所java对象转化为服务器接受的json字符串
     * @author: troublemaker
     * @date: 9:55
     * @param: [fieldInfo]
     * @return: java.lang.String
     **/
    String objToJsonString(FieldInfo fieldInfo);

    /**
     * @description: 当然，你也可以直接定义java对象，不需要通过服务器获取可选场地的信息，这样可以更快。
     * 格式如下: new FieldInfo("19:30", "20:30", "YMQ017", "羽毛球05-1", "03", "0.00", 1, 0)
     * @author: troublemaker
     * @date: 9:58
     * @param: [fieldInfo]
     * @return: java.lang.String
     **/
    String orderInvariableField(FieldInfo fieldInfo);

    /**
     * @description: 根据获取的可选场地的集合, 自定义获取对象，这里默认取集合中元素的最后一个。
     * @author: troublemaker
     * @date: 9:37
     * @param: [client, list]
     * @return: java.lang.String
     **/
    String orderChangeableField(List<FieldInfo> list);

    /**
     * @description: get "http://cgyy.zzuli.edu.cn/Field/OrderField?dateadd=0&VenueNo=001&checkdata=" + checkData;
     * @author: troublemaker
     * @date: 9:57
     * @param: [client, checkData]
     * @return: java.lang.String
     **/
    String order(HttpClient client, String checkData) throws MyException;

    /**
     * @description: get "http://cgyy.zzuli.edu.cn/Field/CardPay?" +
     * "PayNo=02" +
     * "&Money=0" +
     * "&CardMoney=1" +
     * "&Count=0.00" +
     * "&MemberNo=" +
     * "&CardNo=" + cardNo +
     * "&BillType=100" +
     * "&Password=" +
     * "&IsCheckPassword=0" +
     * "&OID=" + OID +
     * "&VenueNo=001" +
     * "&PayDiscount=100" +
     * "&IsUseMemberType=1" +
     * "&EWMNum=1" +
     * "&_=" + new Date().getTime();
     * @author: troublemaker
     * @date: 10:05
     * @param: [client, cardNo, OID]
     * @return: java.lang.String
     **/
    String subMit(HttpClient client, String cardNo, String oId);

    /**
     * @description: get "http://cgyy.zzuli.edu.cn/Field/GetFieldOrder?PageNum=1&PageSize=6&Condition=" +
     * "&_=" + new Date().getTime();
     * 可以获取已提交的所有订单，默认获取第一个。
     * @author: troublemaker
     * @date: 10:02
     * @param: [client]
     * @return: java.lang.String
     **/
    String getOrdered(HttpClient client);
}
