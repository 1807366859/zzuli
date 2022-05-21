package com.troublemaker.clockin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;



import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

/**
 * @author Troublemaker
 * @date 2022- 04 28 21:02
 */
@Data
public class User {
    @TableId(type = ASSIGN_ID)
    private String id;
    private String username;
    private String password;
    private String email;
    private String build;
    private String dorm;
    private String mobile;
    @TableField(value = "jt_mobile")
    @JsonAlias("jt_mobile")
    private String jtMobile;
}

