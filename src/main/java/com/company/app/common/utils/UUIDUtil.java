package com.company.app.common.utils;

import java.util.UUID;

public class UUIDUtil {
    /**
     * 生成一个32位无横杠的UUID
     */
    public synchronized static String make32BitUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
    /**
     * 生成一个16位无横杠的UUID
     */
    public synchronized static String make16BitUUID(){
        return UUID.randomUUID().toString().replace("-","").substring(0,16);
    }
}
