package com.troublemaker.clockin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Troublemaker
 * @date 2022- 04 28 21:27
 */
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "com.troublemaker")
public class ClockInApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClockInApplication.class, args);
    }
}
