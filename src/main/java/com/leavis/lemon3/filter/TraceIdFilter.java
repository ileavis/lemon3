package com.leavis.lemon3.filter;

import com.leavis.lemon3.mdc.TraceIdUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;

/**
 * @Author: paynejlli
 * @Description: TraceIdFilter，为请求添加 traceId
 * @Date: 2024/8/23 15:55
 */
@Order(1)
@Slf4j
@WebFilter(urlPatterns = "/*")
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // 如果有上层调用就用上层的ID
        String traceId = httpServletRequest.getHeader(TraceIdUtil.HTTP_HEADER_TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            TraceIdUtil.setTraceId(TraceIdUtil.generateTraceId());
        } else {
            TraceIdUtil.setTraceId(traceId);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            // 调用结束后删除
            TraceIdUtil.remove();
        }
    }
}
