package com.troublemaker.order.entity;

/**
 * @author Troublemaker
 * @date 2022- 04 26 11:54
 */
public enum FieldType {
    //健身
    Body("01"),
    //排球
    Volleyball("02"),
    //羽毛球
    Badminton("03"),
    //乒乓球
    TableTennis("04");

    private final String fieldTypeNo;

    FieldType(String fieldTypeNo) {
        this.fieldTypeNo = fieldTypeNo;
    }

    public String getFieldTypeNo() {
        return fieldTypeNo;
    }
}
