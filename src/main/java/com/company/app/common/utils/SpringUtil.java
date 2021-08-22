package com.company.app.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author: tutu
 * @Time: 11:28
 * @Date: 2019/6/29
 * @Email: wangde007@outlook.com
 * @CreateBy: idea
 * @Copyright (c) 2007-2018 tu13 Ltd. All Rights Reserved.
 * @Pump: 四季轮回，盛夏来袭。
 **/
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContextParam) throws BeansException {
        applicationContext=applicationContextParam;
    }
    public static Object getObject(String id) {
        Object object = null;
        object = applicationContext.getBean(id);
        return object;
    }
    public static <T> T getObject(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    public static Object getBean(String tClass) {
        return applicationContext.getBean(tClass);
    }

    public <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }
}