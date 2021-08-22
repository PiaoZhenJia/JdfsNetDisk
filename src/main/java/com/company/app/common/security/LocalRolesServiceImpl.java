package com.company.app.common.security;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class LocalRolesServiceImpl implements LocalRolesService {
    @Override
    public boolean checkRoles(String authorityValue, HttpSession session) {
        try{
            return authorityValue.equals("admin") && session.getAttribute("isLogin").equals("yes");
        }catch (Exception e){
            return false;
        }
    }
}
