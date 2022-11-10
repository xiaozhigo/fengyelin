package com.example.consumer;

import com.alibaba.fastjson.JSONObject;
import com.example.fengcommon.config.kafka.KafkaGroup;
import com.example.fengcommon.config.kafka.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: institutional_otc
 * @Package: com.hy.otc.provider.consumer
 * @ClassName: TestConsumerService
 * @Author: libin06
 * @Description:
 * @Date: 2022/9/13 10:20
 * @Version: 1.0
 */
@Slf4j
@Component
public class ConsumerService {
    @KafkaListener(topics = {KafkaTopic.FENG_TOPIC}, groupId = KafkaGroup.FENG_GROUP)
    public void tradeListener(@Payload ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {
        log.info("收到交易消息:{}",consumerRecord.value());
        try {
            if (consumerRecord.value() != null) {
                log.info("消费内容:{}",consumerRecord.value());
                ack.acknowledge();
                return;
            }
        }catch (Exception e){
            log.error("消费异常:{}",e.getMessage());
        }
        ack.acknowledge();
    }
}

