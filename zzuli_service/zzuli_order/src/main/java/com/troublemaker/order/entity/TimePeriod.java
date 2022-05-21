package com.troublemaker.order.entity;

/**
 * @author Troublemaker
 * @date 2022- 04 29 10:28
 */
public enum TimePeriod {
    //早
    MORNING("0"),
    //中
    AFTERNOON("1"),
    //晚
    NIGHT("2");

    private final String timePeriodNo;

    TimePeriod(String timePeriodNo) {
        this.timePeriodNo = timePeriodNo;
    }

    public String getTimePeriodNo() {
        return timePeriodNo;
    }
}

