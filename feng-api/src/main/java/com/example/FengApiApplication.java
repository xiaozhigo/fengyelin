package com.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableDubbo
@SpringBootApplication
@EnableKafka
public class FengApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FengApiApplication.class, args);
    }

}
