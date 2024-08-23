package com.leavis.lemon3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author: paynejlli
 * @Description: kafka消息内容
 * @Date: 2024/8/22 17:38
 */
@Schema(description = "kafka消息内容")
@Data
public class KafkaMessageReqDTO {

    @NotNull(message = "id不能为空")
    @Schema(description = "id")
    private Long id;

    @NotBlank(message = "消息内容不能为空")
    @Schema(description = "消息内容")
    private String message;
}
