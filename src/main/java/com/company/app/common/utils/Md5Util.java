package com.company.app.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;


/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
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


