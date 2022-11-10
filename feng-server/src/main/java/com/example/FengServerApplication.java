package com.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@EnableDubbo
@SpringBootApplication
@MapperScan({"com.example.mapper"})
@EnableKafka
public class FengServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FengServerApplication.class, args);
    }

}
