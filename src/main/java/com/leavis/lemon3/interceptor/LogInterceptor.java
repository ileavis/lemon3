package com.leavis.lemon3.interceptor;

import com.leavis.lemon3.mdc.TraceIdUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: paynejlli
 * @Description: 日志拦截器
 * @Date: 2024/8/20 15:25
 */
@Order(1)
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //如果有上层调用就用上层的ID
        String traceId = request.getHeader(TraceIdUtil.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            TraceIdUtil.setTraceId(TraceIdUtil.generateTraceId());
        } else {
            TraceIdUtil.setTraceId(traceId);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView)
            throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //调用结束后删除
        TraceIdUtil.remove();
    }
}
