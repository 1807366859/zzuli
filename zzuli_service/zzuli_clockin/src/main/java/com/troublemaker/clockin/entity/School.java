package com.troublemaker.clockin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

/**
 * @BelongsProject: zzuli
 * @BelongsPackage: com.troublemaker.clockin.entity
 * @Author: troublemaker
 * @CreateTime: 2022-05-26  00:10
 */
@Data
public class School {
    @TableId(type = ASSIGN_ID)
    private String sid;
    private String region;
    private String area;
    private String build;
    private String dorm;
    private String mobile;
    private String jtMobile;
    private String lat;
    private String lon;
    private String jzAddress;
    private String jzProvince;
    private String jzCity;
    private String jzDistrict;
    private String sUid;
}

