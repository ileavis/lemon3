package com.leavis.lemon3.interceptor;

import com.leavis.lemon3.mdc.TraceIdUtil;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * @Author: paynejlli
 * @Description: rest添加拦截器
 * @Date: 2024/8/20 15:17
 */
public class RestTemplateTraceIdInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
            ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        String traceId = MDC.get(TraceIdUtil.TRACE_ID);
        if (traceId != null) {
            request.getHeaders().set(TraceIdUtil.HTTP_HEADER_TRACE_ID, traceId);
        } else {
            request.getHeaders().set(TraceIdUtil.HTTP_HEADER_TRACE_ID, TraceIdUtil.generateTraceId());
        }
        return clientHttpRequestExecution.execute(request, body);
    }
}
