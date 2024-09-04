package com.leavis.lemon3.controller;

import com.leavis.lemon3.dto.GenericRspDTO;
import com.leavis.lemon3.dto.Nobody;
import com.leavis.lemon3.service.PushMsgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: paynejlli
 * @Description: 消息推送控制器
 * @Date: 2024/9/4 09:54
 */
@Tag(name = "消息推送相关接口")
@RestController
@RequestMapping("/netty")
public class PushMsgController {

    @Autowired
    private PushMsgService pushMsgService;

    @Operation(summary = "给指定用户发送消息", description = "入参示例/push/{uid} uid为用户 id msg:消息内容")
    @ApiResponse(responseCode = "200", description = "给指定用户发送消息成功")
    @GetMapping("/push/{uid}")
    public GenericRspDTO<Nobody> pushMsgToOne(@PathVariable String uid, String msg) {
        pushMsgService.pushMsgToOne(uid, msg);
        return GenericRspDTO.successNobody();
    }

    @Operation(summary = "给全部用户发送消息", description = "msg:消息内容")
    @ApiResponse(responseCode = "200", description = "给全部用户发送消息成功")
    @GetMapping("/push/all")
    public GenericRspDTO<Nobody> pushMsgToAll(String msg) {
        pushMsgService.pushMsgToAll(msg);
        return GenericRspDTO.successNobody();
    }
}
