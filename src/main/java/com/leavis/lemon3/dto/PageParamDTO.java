package com.leavis.lemon3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * @Author: paynejlli
 * @Description: 分页参数
 * @Date: 2024/8/12 10:50
 */
@Schema(description = "分页请求信息")
@Data
public class PageParamDTO {
    @Min(value = 1, message = "pageNum不能小于1")
    @Schema(description = "页码")
    private Integer page = 1;

    @Min(value = 1, message = "pageSize不能小于1")
    //@Max(value = 1000, message = "pageSize不能大于1000")
    @Schema(description = "每页返回条数")
    private Integer pageSize = 15;
}
