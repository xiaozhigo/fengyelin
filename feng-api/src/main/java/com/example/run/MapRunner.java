package com.example.run;

import com.example.fengcommon.map.FunctionMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(-2147483647)
@Slf4j
public class MapRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        FunctionMap.functionMap.put("110","/user/getUserInfoRedis");
    }
}
