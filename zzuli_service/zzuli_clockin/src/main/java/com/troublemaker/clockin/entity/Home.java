package com.troublemaker.clockin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

/**
 * @BelongsProject: zzuli
 * @BelongsPackage: com.troublemaker.clockin.entity
 * @Author: troublemaker
 * @CreateTime: 2022-05-26  14:28
 */
@Data
public class Home {
    @TableId(type = ASSIGN_ID)
    private String hid;
    private String mobile;
    @TableField(value = "jt_mobile")
    private String jtMobile;
    private String lat;
    private String lon;
    @TableField(value = "jz_address")
    private String jzAddress;
    @TableField(value = "jz_province")
    private String jzProvince;
    @TableField(value = "jz_city")
    private String jzCity;
    @TableField(value = "jz_district")
    private String jzDistrict;
    @TableField(value = "s_uid")
    private String sUid;
}

