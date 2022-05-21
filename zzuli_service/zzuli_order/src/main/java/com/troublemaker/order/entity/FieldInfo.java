package com.troublemaker.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Troublemaker
 * @date 2022- 04 29 10:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldInfo {
    @JSONField(name = "BeginTime")
    private String beginTime;
    @JSONField(name = "EndTime")
    private String endTime;
    @JSONField(name = "FieldNo")
    private String fieldNo;
    @JSONField(name = "FieldName")
    private String fieldName;
    @JSONField(name = "FieldTypeNo")
    private String fieldTypeNo;
    @JSONField(name = "Price")
    private String price;
    @JSONField(name = "TimeStatus", serialize = false)
    private int timeStatus;
    @JSONField(name = "TimeStatus", serialize = false)
    private int fieldState;

    // 由于序列化和反序列化过程中，字段均不一致
    // 手动set实现反序列化
    // @JSONField(name = "Price")实现序列化
    public void setFinalPrice(String price) {
        this.price = price;
    }

}
