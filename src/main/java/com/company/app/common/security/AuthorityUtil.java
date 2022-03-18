package com.company.app.common.security;

import com.company.app.common.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Component
public class AuthorityUtil {

    @Autowired
    private LogUtil logUtil;

    @Autowired LocalRolesService localRolesService;

    public boolean checkAuthority(Object handler, HttpSession session) {
        Method method = null;
        //获取方法注解
        try {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            method = handlerMethod.getMethod();
        } catch (Exception e) {
            //throw new RuntimeException("获取接口异常");
        }
        if (method == null) {
            //throw new RuntimeException("接口可能不存在");
        }
        Authority authority = null;
        try{
            authority = method.getAnnotation(Authority.class);
        }catch (Exception ignored){
        }
        //判断是否为免验证路径
        if (null == authority || authority.value() == "none") {
            return true;
        }
        return localRolesService.checkRoles(authority.value(),session);
    }
}
