package com.seehoo.rent.sdk.client;

/**
 * SDK内部辅助：响应节点原文提取、公共键名
 */
final class RentSdkHelper {

    static final String KEY_SIGN = "sign";

    private RentSdkHelper() {
    }

    /**
     * 从网关响应原文中提取指定响应节点的原始字符串（含首尾大括号）
     * <p>网关响应为紧凑JSON，按节点名定位后用花括号计数截取，保证原文不被重序列化破坏</p>
     */
    static String extractRawNode(String body, String nodeName) {
        String marker = "\"" + nodeName + "\":";
        int nodeStart = body.indexOf(marker);
        if (nodeStart < 0) {
            return null;
        }
        int open = body.indexOf('{', nodeStart + marker.length());
        if (open < 0) {
            return null;
        }
        final char ESC = (char) 92;  // 反斜杠字符，避开转义
        final char LBRACE = '{';
        final char RBRACE = '}';
        final char QUOTE = '"';
        int depth = 0;
        boolean inString = false;
        for (int i = open; i < body.length(); i++) {
            char c = body.charAt(i);
            if (inString) {
                if (c == ESC) {
                    i++;
                } else if (c == QUOTE) {
                    inString = false;
                }
                continue;
            }
            if (c == QUOTE) {
                inString = true;
            } else if (c == LBRACE) {
                depth++;
            } else if (c == RBRACE) {
                depth--;
                if (depth == 0) {
                    return body.substring(open, i + 1);
                }
            }
        }
        return null;
    }
}
