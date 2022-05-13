package com.troublemaker.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;

/**
 * @author Troublemaker
 * @date 2022- 04 29 10:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldInfo {
    @JSONField(name = "BeginTime")
    private String BeginTime;
    @JSONField(name = "EndTime")
    private String EndTime;
    @JSONField(name = "FieldNo")
    private String FieldNo;
    @JSONField(name = "FieldName")
    private String FieldName;
    @JSONField(name = "FieldTypeNo")
    private String FieldTypeNo;
    @JSONField(name = "Price")
    private String Price;
    private int TimeStatus;
    private int FieldState;

    public void setFinalPrice(String price) {
        Price = price;
    }

    @Transient
    public int getTimeStatus() {
        return TimeStatus;
    }

    @Transient
    public int getFieldState() {
        return FieldState;
    }
}
