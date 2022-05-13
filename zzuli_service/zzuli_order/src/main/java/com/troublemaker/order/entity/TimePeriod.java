package com.troublemaker.order.entity;

/**
 * @author Troublemaker
 * @date 2022- 04 29 10:28
 */
public enum TimePeriod {
    MORNING("0"),
    AFTERNOON("1"),
    NIGHT("2");

    private final String timePeriodNO;

    TimePeriod(String timePeriodNO) {
        this.timePeriodNO = timePeriodNO;
    }

    public String getTimePeriodNO() {
        return timePeriodNO;
    }
}

