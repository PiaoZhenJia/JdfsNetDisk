package com.company.app.common.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Component
public class UUIDUtil {
    /**
     * 生成一个32位无横杠的UUID
     */
    public synchronized String make32BitUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成指定位数无横杠的UUID
     */
    public synchronized String makeAppointBitUUID(int num) {
        if (num > 32) {
            throw new RuntimeException(num + "超过UUID总位数(32)");
        }
        return make32BitUUID().substring(0, num);
    }
}
