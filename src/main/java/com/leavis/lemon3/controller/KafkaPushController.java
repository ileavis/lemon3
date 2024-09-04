package com.leavis.lemon3.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.leavis.lemon3.dto.KafkaMessageReqDTO;
import com.leavis.lemon3.dto.Nobody;
import com.leavis.lemon3.exception.BizException;
import com.leavis.lemon3.dto.GenericRspDTO;
import com.leavis.lemon3.enums.ErrorCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: paynejlli
 * @Description: kafka相关接口
 * @Date: 2024/8/22 17:27
 */
@Tag(name = "kafka相关接口")
@RequestMapping("/kafka")
@RestController
@Slf4j
public class KafkaPushController {

    @Resource
    private KafkaTemplate kafkaTemplate;

    @Value("${lemon.kafka.consumer.topic}")
    private String topic;

    @PostMapping(value = "/message")
    @Operation(summary = "发送消息到固定的 topic内", description = "字符串，随便填写")
    @ApiResponse(responseCode = "200", description = "发送消息到 topic成功")
    GenericRspDTO<Nobody> sendMsg(@Validated @RequestBody KafkaMessageReqDTO kafkaMessageReqDTO) {
        JSON msgJson = JSONUtil.parseObj(kafkaMessageReqDTO);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, UUID.randomUUID().toString(),
                msgJson);
        AtomicBoolean sendResult = new AtomicBoolean(false);
        // 使用 thenCompose 确保回调完成后再进行下一步
        CompletableFuture resultFuture = future.thenCompose(result -> {
            sendResult.set(true);
            log.info("发送成功: {}", JSONUtil.toJsonStr(result.getProducerRecord().value()));
            return CompletableFuture.completedFuture(null);
        }).exceptionally(e -> {
            sendResult.set(false);
            log.info("发送失败: {}, Exception: {}", JSONUtil.toJsonStr(kafkaMessageReqDTO), e.getMessage());
            throw new RuntimeException("Message sending failed.", e);
        });
        // 等待回调完成
        try {
            resultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            BizException.throwException(ErrorCodeEnum.FAILED, e);
        }
        if (!sendResult.get()) {
            BizException.throwException(ErrorCodeEnum.FAILED);
        }

//        //执行成功回调
//        future.thenAccept(result -> {
//            log.debug("发送成功:{}", JSONUtil.toJsonStr(result.getProducerRecord().value()));
//        });
//        //执行失败回调
//        future.exceptionally(e -> {
//            log.error("发送失败", JSONUtil.toJsonStr(kafkaMessageReqDTO), e);
//            return null;
//        });
        return GenericRspDTO.successNobody();
    }
}
