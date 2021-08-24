package com.company.app.common.security;

import com.company.jdfs.JdfsConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Service
public class LocalRolesServiceImpl implements LocalRolesService {
    @Override
    public boolean checkRoles(String authorityValue, HttpSession session) {
        try {
            return authorityValue.equals("user") && StringUtils.isNotBlank((String) session.getAttribute(JdfsConstant.SESSION_LOGIN_FLAG));
        } catch (Exception e) {
            return false;
        }
    }
}
