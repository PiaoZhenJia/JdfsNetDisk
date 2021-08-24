package com.company.app.common.utils;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Component
public class LogUtil {


    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //使用了默认的格式创建了一个日期格式化对象。

    public void debug(Object $This, String needLog) {
        LoggerFactory.getLogger($This.getClass()).debug(needLog);
    }
    public void info(Object $This, String needLog) {
        LoggerFactory.getLogger($This.getClass()).info(needLog);
    }
    public void warn(Object $This, String needLog) {
        LoggerFactory.getLogger($This.getClass()).warn(needLog);
    }

    public void error(Object $This, String needLog) {
        LoggerFactory.getLogger($This.getClass()).error(needLog);
    }


}
