package com.leavis.lemon3.advice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leavis.lemon3.dto.GenericRspDTO;
import com.leavis.lemon3.mdc.TraceIdUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author: paynejlli
 * @Description: 为返回的对象添加 traceId
 * @Date: 2024/9/4 11:41
 */
@Slf4j
@ControllerAdvice
public class TraceIdAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            log.warn("Response body is null");
            return body;
        }
        if (body instanceof GenericRspDTO<?>) {
            // 如果返回的是 GenericRspDTO，可以在返回前添加 traceId
            ((GenericRspDTO<?>) body).setTraceId(TraceIdUtil.getTraceId());
        }
        return body;
    }
}
