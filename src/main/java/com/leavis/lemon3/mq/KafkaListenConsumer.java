package com.leavis.lemon3.mq;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @Author: paynejlli
 * @Description: 消费者listener
 * @Date: 2024/8/22 17:19
 */
@Slf4j
@Component
public class KafkaListenConsumer {

    @KafkaListener(topics = "${lemon.kafka.consumer.topic}")
    public void listen(List<ConsumerRecord> records, Acknowledgment ack) {
        log.info("=====消费者接收信息====");
        try {
            for (ConsumerRecord record : records) {
                log.info("---解析消息内容:{}", record.toString());
                // 具体service里取做逻辑
            }
            // 手动提交偏移量
            ack.acknowledge();
        } catch (Exception e) {
            log.error("----数据消费者解析数据异常:{}", e.getMessage(), e);
        }
    }
}
