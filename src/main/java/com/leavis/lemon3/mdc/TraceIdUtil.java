package com.leavis.lemon3.mdc;

import java.util.UUID;
import org.slf4j.MDC;

/**
 * @Author: paynejlli
 * @Description: traceId工具
 * @Date: 2024/8/20 15:14
 */
public class TraceIdUtil {

    public static final String TRACE_ID = "traceId";
    public static final String HTTP_HEADER_TRACE_ID = "x-lemon-traceId";

    public static String getTraceId() {
        String traceId = MDC.get(TRACE_ID);
        return traceId == null ? "" : traceId;
    }

    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    public static void remove() {
        MDC.remove(TRACE_ID);

    }

    public static void clear() {
        MDC.clear();
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
