package com.troublemaker.clockin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.assertj.core.annotations.Beta;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


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
    @TableField(exist = false)
    private String secret = "";
    @TableField(exist = false)
    private String accountLogin = "";
    @TableField(exist = false)
    private String lt = "";
    @TableField(exist = false)
    private String execution = "e1s1";
    @TableField(exist = false)
    private String _eventId = "submit";
    private String email;
    private String build;
    private String dorm;
    private String mobile;
    @TableField(value = "jt_mobile")
    private String jt_mobile;
}

