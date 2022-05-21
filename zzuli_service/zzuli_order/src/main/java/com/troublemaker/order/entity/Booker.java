package com.troublemaker.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

/**
 * @author Troublemaker
 * @date 2022- 04 29 12:13
 */
@Data
public class Booker {
    @TableId(type = ASSIGN_ID)
    private String id;
    private String username;
    private String password;
}
