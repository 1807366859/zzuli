package com.troublemaker.clockin;

import com.troublemaker.clockin.execute.DoClockInTask;
import com.troublemaker.clockin.service.ClockInService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Troublemaker
 * @date 2022- 04 28 23:05
 */
@SpringBootTest
public class ClockInApplicationTests {

    @Autowired
    private DoClockInTask doClockInTask;

    @Autowired
    private ClockInService service;

    @Test
    void contextLoads() {
        doClockInTask.start();
    }

    @Test
    void test0(){
        Map<String, String> map = new HashMap<>();
        map.put("username","332103030216");
        map.put("password","014552");
        map.put("email","727881985@qq.com");
        map.put("clock_Type","1");

        map.put("region","科学校区");
        map.put("area","宿舍区");
        map.put("build","11号楼");
        map.put("dorm","1312");
        map.put("mobile","15903750964");
        map.put("jt_mobile","13949471106");
        map.put("lat","34.817355");
        map.put("lon","113.513509");
        map.put("jz_address","河南省郑州市中原区(郑州轻工业大学(科学校区))");
        map.put("jz_province","河南省");
        map.put("jz_city","郑州市");
        map.put("jz_district","中原区");
        service.addSchoolClockInfo(map);
    }

    @Test
    void test1(){
        Map<String, String> map = new HashMap<>();
        map.put("username","332103060280");
        map.put("password","715766");
        map.put("email","1876828495@qq.com");
        map.put("clock_Type","0");

        map.put("mobile","13903838843");
        map.put("jt_mobile","13015150370");
        map.put("lat","33.71302");
        map.put("lon","115.175072");
        map.put("jz_address","中国河南省周口市郸城县胡集乡");
        map.put("jz_province","河南省");
        map.put("jz_city","周口市");
        map.put("jz_district","郸城县");
        service.addHomeClockInfo(map);
    }
}
