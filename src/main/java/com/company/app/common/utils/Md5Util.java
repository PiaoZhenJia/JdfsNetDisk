package com.company.app.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * MD5工具类
 */
@Component
public class Md5Util {
    public String createMd5(String base, String salt) {
        if (StringUtils.isBlank(salt)){
            return  DigestUtils.md5DigestAsHex(base.getBytes());
        } else {
            return DigestUtils.md5DigestAsHex((base + "-" + salt).getBytes());
        }
    }
}


