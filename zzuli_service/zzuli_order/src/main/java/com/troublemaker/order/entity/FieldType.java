package com.troublemaker.order.entity;

/**
 * @author Troublemaker
 * @date 2022- 04 26 11:54
 */
public enum FieldType {
    Body("01"),
    Volleyball("02"),
    Badminton("03"),
    TableTennis("04");

    private final String fieldTypeNo;

    FieldType(String fieldTypeNo) {
        this.fieldTypeNo = fieldTypeNo;
    }

    public String getFieldTypeNo() {
        return fieldTypeNo;
    }
}
