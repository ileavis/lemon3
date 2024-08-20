package com.leavis.lemon3.controller;

import com.leavis.lemon3.rsp.Result;
import com.leavis.lemon3.service.AmapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: paynejlli
 * @Description: 高德
 * @Date: 2024/8/20 16:00
 */
@Tag(name = "高德相关接口")
@RequestMapping("/amap")
@RestController
public class AmapController {

    @Resource
    private AmapService amapService;

    @GetMapping(value = "/weather")
    @Operation(summary = "获取天气信息", description = "入参示例【北京】：110000")
    @ApiResponse(responseCode = "200", description = "天去预报信息")
    Result<String> getWeatherInfo(@Validated @NotBlank String cityCode){
        String weather = amapService.getWeatherInfo(cityCode);
        return Result.success(weather);
    }
}
