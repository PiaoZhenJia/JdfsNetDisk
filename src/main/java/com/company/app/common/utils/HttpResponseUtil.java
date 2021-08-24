package com.company.app.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
public class HttpResponseUtil {

    /**
     * 设置HTTP状态码
     */
    public static void setStatusCode(HttpServletResponse response, int statusCode) {
        response.setHeader("Content-Type", "application/json");
        response.setStatus(statusCode);
    }

    /**
     * 13      * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 14 *
     * 16      * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 17      * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * 18      *
     * 19      * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 20      * 192.168.1.100
     * 21      *
     * 22      * 用户真实IP为： 192.168.1.110
     * 23      *
     * 24      * @param request
     * 25      * @return
     * 26
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
