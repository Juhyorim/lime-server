package com.lime.limeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class LimeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimeServerApplication.class, args);
    }

}
