package com.leavis.lemon3.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leavis.lemon3.bo.UserBO;
import com.leavis.lemon3.dto.PageParamDTO;
import com.leavis.lemon3.dto.UserInfoDTO;
import com.leavis.lemon3.dto.GenericRspDTO;
import com.leavis.lemon3.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: paynejlli
 * @Description: 用户 controller
 * @Date: 2024/8/13 12:05
 */
@Tag(name = "用户相关接口")
@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping(value = "/page")
    @Operation(summary = "获取用户列表", description = "获取用户列表,输入分页参数，返回总记录数和详细记录")
    @ApiResponse(responseCode = "200", description = "返回用户列表和总记录数")
    public GenericRspDTO<List<UserInfoDTO>> getUserPage(PageParamDTO pageParamDTO) {
        Page<UserBO> userBOPage = userService.getUserPage(pageParamDTO.getPage(),
                pageParamDTO.getPageSize());
        if (CollectionUtils.isEmpty(userBOPage.getRecords())) {
            return GenericRspDTO.successPage(new ArrayList<>(), userBOPage.getTotal());
        }
        List<UserInfoDTO> userInfoDTOList = userBOPage.getRecords().stream().map(e -> {
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            BeanUtils.copyProperties(e, userInfoDTO);
            return userInfoDTO;
        }).collect(Collectors.toList());
        return GenericRspDTO.successPage(userInfoDTOList, userBOPage.getTotal());
    }
}
