package com.troublemaker.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Troublemaker
 * @date 2022- 04 29 10:48
 */
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "com.troublemaker")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
