package com.troublemaker.order.main;


import com.troublemaker.clockin.service.ClockInService;
import com.troublemaker.order.entity.Booker;
import com.troublemaker.order.entity.FieldInfo;
import com.troublemaker.order.entity.FieldType;
import com.troublemaker.order.entity.TimePeriod;
import com.troublemaker.order.exception.MyException;
import com.troublemaker.order.service.FieldSelectionService;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.troublemaker.utils.httputils.HttpClientUtils.getClientNoSSL;


/**
 * @author Troublemaker
 * @date 2022- 04 26 22:54
 */
@Component
public class DoOrder {
    @Autowired
    private FieldSelectionService selectionService;
    @Autowired
    private ClockInService clockInService;
    private static final String loginUrl = "http://kys.zzuli.edu.cn/cas/login";
    private static final String homeUrl = "http://cgyy.zzuli.edu.cn/User/UserChoose?LoginType=1";

    public void start() {

        List<Booker> bookers = selectionService.getBookers();
        for (Booker booker : bookers) {
            //获取httpclient
            HttpClient client = getClientNoSSL();

            //登录
            String lt = clockInService.getLt(client, loginUrl);
            booker.setLt(lt);
            clockInService.login(client, loginUrl, selectionService.bookerToMap(booker));

            //进个人中心拿cookie
            selectionService.getHomePage(client, homeUrl);

            //获取场地信息
            List<FieldInfo> list = selectionService.getOptionalFieldInfo(client, FieldType.Badminton, TimePeriod.NIGHT);

            //对可选场所进行处理
            //得到想要预定的场所
            String orderField = selectionService.orderChangeableField(client, list);

            //提交订单
            String OID;
            try {
                OID = selectionService.order(client, orderField);
                System.out.println(selectionService.subMit(client, booker.getUsername(), OID));
                System.out.println(selectionService.getOrdered(client));
            } catch (MyException e) {
                e.printStackTrace();
            }
        }

    }
}
