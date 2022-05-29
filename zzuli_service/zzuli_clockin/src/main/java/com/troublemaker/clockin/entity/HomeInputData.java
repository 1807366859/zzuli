package com.troublemaker.clockin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @BelongsProject: zzuli
 * @BelongsPackage: com.troublemaker.clockin.entity
 * @Author: troublemaker
 * @CreateTime: 2022-05-26  14:29
 */
@Data
public class HomeInputData {

    public HomeInputData() {
        Date day = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.date = simpleDateFormat.format(day);

        this.jzSfyz = "是";
        this.temp = "正常";
        this.jrzz = "无";
        this.stzk = "无";
        this.jcbl = "否";
        this.yqgl = "否";
        this.qzYqbl = "否";
        this.jjymqk = "已完成接种";
        this.hsjcqk = "更多次";
        this.lastTime = "2022-05-29";
        this.fxdj="低风险";
        this.flgl="正常";
        this.jkmzt = "绿色";
        this.other = "无";
        this.yjs = 0;
        this.wjType = 0;
    }

    // 填写日期
    @JSONField(ordinal = 1)
    private String date;
    // 1、学号
    @JSONField(ordinal = 2, name = "user_code")
    private String userCode;
    // 2、姓名
    @JSONField(ordinal = 3, name = "user_name")
    private String userName;
    // 2-1、身份证号
    @JSONField(ordinal = 4, name = "id_card")
    private String idCard;
    // 3、性别
    @JSONField(ordinal = 5)
    private String sex;
    // 4、年龄
    @JSONField(ordinal = 6)
    private Integer age;
    // 5、所属学院
    @JSONField(ordinal = 7)
    private String org;
    // 6、年级
    @JSONField(ordinal = 8)
    private Integer year;
    // 7、专业
    @JSONField(ordinal = 9)
    private String spec;
    // 8、班级
    @JSONField(ordinal = 10, name = "class")
    private String classX;
    // 9、联系电话（11位手机号）
    @JSONField(ordinal = 11)
    private String mobile;
    // 9-1、家庭（家长）联系电话
    @JSONField(ordinal = 12, name = "jt_mobile")
    private String jtMobile;
    // 10、你目前居住的详细地址
    @JSONField(ordinal = 13)
    private String address;
    // 11、目前您的位置和居住地点
    // 定位的经度纬度
    @JSONField(ordinal = 14)
    private String lat;
    @JSONField(ordinal = 15)
    private String lon;
    @JSONField(ordinal = 16, name = "gcj_lat")
    private String gcjLat;
    @JSONField(ordinal = 17, name = "gcj_lon")
    private String gcjLon;
    // 定位的地址
    @JSONField(ordinal = 18, name = "jz_address")
    private String jzAddress;
    @JSONField(ordinal = 19, name = "jz_province")
    private String jzProvince;
    @JSONField(ordinal = 20, name = "jz_city")
    private String jzCity;
    @JSONField(ordinal = 21, name = "jz_district")
    private String jzDistrict;
    // 11-1、你获取的位置与本人目前所在地是否相符合
    @JSONField(ordinal = 22, name = "jz_sfyz")
    private String jzSfyz;
    // 12、您今日的体温
    @JSONField(ordinal = 23)
    private String temp;
    // 13、您今日有无以下症状
    @JSONField(ordinal = 24)
    private String jrzz;
    // 14、今日同住人员身体状况
    @JSONField(ordinal = 25)
    private String stzk;
    // 15、您假期以来是否接触过疑似或确诊病例
    @JSONField(ordinal = 26)
    private String jcbl;
    // 16、是否被当地要求到指定地点隔离
    @JSONField(ordinal = 27)
    private String yqgl;
    // 17、您是否曾经被诊断为确诊或疑似病例
    @JSONField(ordinal = 28, name = "qz_yqbl")
    private String qzYqbl;
    // 18、完成疫苗接种情况
    @JSONField(ordinal = 29)
    private String jjymqk;
    // 19、10月以来本轮郑州疫情核酸检测次数
    @JSONField(ordinal = 30)
    private String hsjcqk;
    // 19-1、最后一次检测时间
    @JSONField(ordinal = 31, name = "last_time")
    private String lastTime;
    // 20、所在地风险等级
    @JSONField(ordinal = 32)
    private String fxdj;
    // 21、所在区域管理分类
    @JSONField(ordinal = 33)
    private String flgl;
    // 22、您的防疫健康信息码状态（国家政务服务平台健康码状态）
    @JSONField(ordinal = 34)
    private String jkmzt;
    // 23、其他需要说明的情况
    @JSONField(ordinal = 35)
    private String other;
    // 居家打卡
    @JSONField(ordinal = 36, name = "wj_type")
    private Integer wjType;
    @JSONField(ordinal = 37)
    private final Integer yjs;
}

