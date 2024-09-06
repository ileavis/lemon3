package com.leavis.lemon3.utils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: paynejlli
 * @Description: 工具类，用于从URI中提取参数
 * @Date: 2024/9/5 14:10
 */
public class UriUtils {

    /**
     * 从URI中提取path部分
     *
     * @param uri 包含查询参数的URI
     * @return path部分
     */
    public static String getPath(String uri) {
        // 检查uri是否为空或长度为0
        if (uri == null || uri.isEmpty()) {
            return "";
        }

        // 寻找问号的位置
        int queryStart = uri.indexOf('?');

        // 如果存在查询字符串，只返回问号前的部分
        if (queryStart != -1) {
            return uri.substring(0, queryStart);
        }

        // 如果不存在查询字符串，返回整个uri
        return uri;
    }

    /**
     * 从URI中提取参数并返回一个Map
     *
     * @param uri 包含查询参数的URI
     * @return 包含参数键值对的Map
     */
    public static Map<String, String> extractParams(String uri) {
        // 找到问号的位置
        int queryStart = uri.indexOf('?');
        if (queryStart == -1) {
            // 如果没有查询字符串，则返回空Map
            return new HashMap<>();
        }

        // 获取查询字符串
        String query = uri.substring(queryStart + 1);
        // 使用&符号分割参数
        String[] params = query.split("&");
        // 创建一个Map来存放参数
        Map<String, String> paramMap = new HashMap<>();

        for (String param : params) {
            // 使用=符号分割键和值
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2) {
                try {
                    // 解码参数值，防止URL编码的问题
                    String key = keyValue[0];
                    String value = java.net.URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name()).trim();
                    paramMap.put(key, value);
                } catch (Exception e) {
                    // 处理URL解码异常
                    System.err.println("URL解码异常: " + e.getMessage());
                }
            }
        }
        return paramMap;
    }
}