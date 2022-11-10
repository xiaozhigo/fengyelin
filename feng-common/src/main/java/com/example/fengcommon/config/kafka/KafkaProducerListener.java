package com.example.fengcommon.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


/**
 * @ProjectName: institutional_otc
 * @Package: com.hy.otc.common.kafka
 * @ClassName: KafkaProducerListener
 * @Author: libin06
 * @Description: kafka 监听消息发送
 * @Date: 2022/9/9 11:28
 * @Version: 1.0
 */
@Slf4j
@Component
public class KafkaProducerListener implements ProducerListener<String, String> {

    /**
     * 成功回调
     */
    @Override
    public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
        log.info("kafka topic: {}, 消息推送成功,推送数据: {}", producerRecord.topic(), producerRecord.value());
    }

    /**
     * 失败回调
     */
    @Override
    public void onError(ProducerRecord producerRecord, @Nullable RecordMetadata recordMetadata, Exception exception) {
        log.error("kafka topic: {}, 消息推送失败,推送数据: {},失败原因：{}", producerRecord.topic(), producerRecord.value(), exception.getMessage());
    }
}