package com.leavis.lemon3.filter;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.WriteListener;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.StreamUtils;

/**
 * @Author: paynejlli
 * @Description: 日志打印
 * @Date: 2024/8/20 15:22
 */
@Order(1)
@Slf4j
@WebFilter(urlPatterns = "/*")
public class WebLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        RequestWrapper requestWrapper = new RequestWrapper(request);
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) servletResponse);

        chain.doFilter(requestWrapper, responseWrapper);

        log.info("HTTP {} {} {request params: {},request body: {},client ip: {},response body: {},cost: {}ms}",
                request.getMethod(), request.getRequestURI(), JakartaServletUtil.getParamMap(request),
                requestWrapper.getBodyAsString(),
                JakartaServletUtil.getClientIP(request), responseWrapper.getContentAsString(),
                System.currentTimeMillis() - start);

        ServletOutputStream outputStream = servletResponse.getOutputStream();
        outputStream.write(responseWrapper.getContent());
        outputStream.flush();
        outputStream.close();
    }

    public static class RequestWrapper extends HttpServletRequestWrapper {

        private final byte[] body;

        public RequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            body = StreamUtils.copyToByteArray(request.getInputStream());
        }

        @Override
        public ServletInputStream getInputStream() {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            return new ServletInputStream() {

                public int read() {
                    return byteArrayInputStream.read();
                }

                public boolean isFinished() {
                    return false;
                }

                public boolean isReady() {
                    return false;
                }

                public void setReadListener(ReadListener listener) {
                }
            };
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        public byte[] getBody() {
            return body;
        }

        public String getBodyAsString() {
            return new String(body, StandardCharsets.UTF_8);
        }
    }

    public static class ResponseWrapper extends HttpServletResponseWrapper {

        private ByteArrayOutputStream byteArrayOutputStream;
        private ServletOutputStream servletOutputStream;

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
            byteArrayOutputStream = new ByteArrayOutputStream();
            servletOutputStream = new MyServletOutputStream(byteArrayOutputStream);
        }

        @Override
        public ServletOutputStream getOutputStream() {
            return servletOutputStream;
        }

        @Override
        public PrintWriter getWriter() {
            return new PrintWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8));
        }

        @Override
        public void flushBuffer() {
            if (servletOutputStream != null) {
                try {
                    servletOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public byte[] getContent() {
            flushBuffer();
            // response中的数据
            return byteArrayOutputStream.toByteArray();
        }

        public String getContentAsString() {
            return new String(getContent(), StandardCharsets.UTF_8);
        }

        class MyServletOutputStream extends ServletOutputStream {

            // 把response输出流中的数据写入字节流中
            private ByteArrayOutputStream byteArrayOutputStream;

            public MyServletOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
                this.byteArrayOutputStream = byteArrayOutputStream;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
            }

            @Override
            public void write(int b) {
                byteArrayOutputStream.write(b);
            }
        }
    }
}
