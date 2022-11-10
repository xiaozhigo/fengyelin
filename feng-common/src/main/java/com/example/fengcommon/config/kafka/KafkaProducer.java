package com.example.fengcommon.config.kafka;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

/**
 * @ProjectName: institutional_otc
 * @Package: com.hy.otc.common.service
 * @ClassName: XX
 * @Author: libin06
 * @Description:
 * @Date: 2022/9/9 11:14
 * @Version: 1.0
 */
@Component
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 同步发送
     *
     * @param topic
     * @param obj
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void send(String topic, Object obj) {
        String obj2String = JSONObject.toJSONString(obj);
        log.info("准备发送消息为：{}", obj2String);
        try {
            kafkaTemplate.send(topic, obj2String).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步发送
     *
     * @param topic
     * @param obj
     */
    public void asyncSend(String topic, Object obj) {
        String obj2String = JSONObject.toJSONString(obj);
        log.info("准备发送消息为：{}", obj2String);
        kafkaTemplate.send(topic, obj);
        kafkaTemplate.flush();
    }


}
