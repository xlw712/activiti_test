package com.levin.activiti;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan(value = {"com.levin", "com.levin.activiti"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
