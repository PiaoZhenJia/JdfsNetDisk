package com.company.app.common.security;

import javax.servlet.http.HttpSession;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
public interface LocalRolesService {
    boolean checkRoles(String authorityValue, HttpSession session);
}
