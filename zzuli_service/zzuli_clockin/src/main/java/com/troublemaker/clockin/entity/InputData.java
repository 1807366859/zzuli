package com.troublemaker.clockin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Troublemaker
 * @date 2022- 04 28 21:11
 */
@Data
public class InputData {


    public InputData() {
        Date day = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.date = simpleDateFormat.format(day);
        this.region = "科学校区";
        this.area = "宿舍区";
        this.lat = "34.817355";
        this.lon = "113.513509";
        this.gcjLat = "34.817355";
        this.gcjJon = "113.513509";
        this.jzAddress = "河南省郑州市中原区(郑州轻工业大学(科学校区))";
        this.jzProvince = "河南省";
        this.jzCity = "郑州市";
        this.jzDistrict = "中原区";
        this.jzSfyz = "是";
        this.temp = "正常";
        this.jrzz = "无";
        this.stzk = "无";
        this.yjs = 0;
        this.other = "无";
        this.jjymqk = "已完成接种";
        this.hsjcqk = "更多次";
        this.lastTime = "2022-05-17";
        this.jkmzt = "绿色";
        this.wjType = 1;
        this.yqgl = "否";
        this.qzYqbl = "否";
    }

    @JSONField(name = "user_code")
    private String userCode;
    @JSONField(name = "user_name")
    private String userName;
    @JSONField(name = "id_card")
    private String idCard;
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
    @JSONField(name = "jt_mobile")
    private String jtMobile;
    private String org;
    private Integer year;
    private String province;
    private String city;
    private String district;
    private String address;
    private String lat;
    private String lon;
    @JSONField(name = "gcj_lat")
    private String gcjLat;
    @JSONField(name = "gcj_jon")
    private String gcjJon;
    @JSONField(name = "jz_address")
    private String jzAddress;
    @JSONField(name = "jz_province")
    private String jzProvince;
    @JSONField(name = "jz_city")
    private String jzCity;
    @JSONField(name = "jz_district")
    private String jzDistrict;
    @JSONField(name = "jz_sfyz")
    private String jzSfyz;
    private String temp;
    private String jrzz;
    private String stzk;
    private final Integer yjs;
    private String other;
    private String jjymqk;
    private String hsjcqk;
    @JSONField(name = "last_time")
    private String lastTime;
    private String jkmzt;
    @JSONField(name = "wj_type")
    private Integer wjType;
    private String yqgl;
    @JSONField(name = "qz_yqbl")
    private String qzYqbl;
}
