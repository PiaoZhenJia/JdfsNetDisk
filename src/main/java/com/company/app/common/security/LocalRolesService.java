package com.company.app.common.security;

import javax.servlet.http.HttpSession;

public interface LocalRolesService {
    boolean checkRoles(String authorityValue, HttpSession session);
}
