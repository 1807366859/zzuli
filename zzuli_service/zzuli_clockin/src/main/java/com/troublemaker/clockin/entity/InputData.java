package com.troublemaker.clockin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Troublemaker
 * @date 2022- 04 28 21:11
 */
@Data
public class InputData {

    public InputData() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        this.date = sdf.format(d);
        this.region = "科学校区";
        this.area = "宿舍区";
        this.lat = "34.817355";
        this.lon = "113.513509";
        this.gcj_lat = "34.817355";
        this.gcj_jon = "113.513509";
        this.jz_address = "河南省郑州市中原区(郑州轻工业大学(科学校区))";
        this.jz_province = "河南省";
        this.jz_city = "郑州市";
        this.jz_district = "中原区";
        this.jz_sfyz = "是";
        this.temp = "正常";
        this.jrzz = "无";
        this.stzk = "无";
        this.yjs = 0;
        this.other = "无";
        this.jjymqk = "已完成接种";
        this.hsjcqk = "更多次";
        this.last_time = "2022-05-17";
        this.jkmzt = "绿色";
        this.wj_type = 1;
        this.yqgl = "否";
        this.qz_yqbl = "否";
    }

    private String user_code;
    private String user_name;
    private String id_card;
    private String date;
    private String sex;
    private Integer age;
    private String region;
    private String area;
    private String build;
    private String dorm;
    private String spec;
    @JSONField(name = "class")
    private String classX;
    private String mobile;
    private String jt_mobile;
    private String org;
    private Integer year;
    private String province;
    private String city;
    private String district;
    private String address;
    private String lat;
    private String lon;
    private String gcj_lat;
    private String gcj_jon;
    private String jz_address;
    private String jz_province;
    private String jz_city;
    private String jz_district;
    private String jz_sfyz;
    private String temp;
    private String jrzz;
    private String stzk;
    private final Integer yjs;
    private String other;
    private String jjymqk;
    private String hsjcqk;
    private String last_time;
    private String jkmzt;
    private Integer wj_type;
    private String yqgl;
    private String qz_yqbl;
}
