package com.leavis.lemon3.interceptor;

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

    private static final String TRACE_ID = "traceId";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
            ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        String traceId = MDC.get(TRACE_ID);
        if (traceId != null) {
            request.getHeaders().set(TRACE_ID, traceId);
        } else {
            request.getHeaders().set(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
        }
        return clientHttpRequestExecution.execute(request, body);
    }
}
