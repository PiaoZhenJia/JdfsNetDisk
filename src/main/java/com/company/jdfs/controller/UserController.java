package com.company.jdfs.controller;

import com.company.app.common.base.R;
import com.company.jdfs.JdfsConstant;
import com.company.jdfs.database.DBUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Api(tags = "用户控制器")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private DBUtil dbUtil;

    @ApiOperation("用户登录")
    @GetMapping("/login")
    public R login(HttpServletRequest request, String userName, String passWord) {
        dbUtil.userDbShow();
        if (dbUtil.checkUser(userName, passWord)) {
            request.getSession().setAttribute(JdfsConstant.SESSION_LOGIN_FLAG, userName);
            return new R("登录成功");
        } else {
            return new R(401, "用户名或密码无效");
        }
    }

    @ApiOperation("用户登出")
    @GetMapping("/logout")
    public R logout(HttpServletRequest request) {
        request.getSession().removeAttribute(JdfsConstant.SESSION_LOGIN_FLAG);
        return new R("您已退出登录");
    }

    @ApiOperation("用户登录状态")
    @GetMapping("/status")
    public R status(HttpServletRequest request) {
        String userName = (String) request.getSession().getAttribute(JdfsConstant.SESSION_LOGIN_FLAG);
        if (StringUtils.isNotBlank(userName)) {
            return new R(userName);
        } else {
            return new R(204);
        }
    }

    @ApiOperation("用户修改名称")
    @GetMapping("/change/userName")
    public R changeUserName(HttpServletRequest request, @RequestParam String userName, @RequestParam String password) {
        String oldName = (String) request.getSession().getAttribute(JdfsConstant.SESSION_LOGIN_FLAG);
        if (StringUtils.isBlank(oldName)) {
            return new R(204, "请重新登录");
        }
        if (StringUtils.isBlank(userName)) {
            return new R(400, "请输入正确的新用户名");
        }
        if (!dbUtil.checkUser(oldName, password)){
            return new R(204, "密码校验失败");
        }
        boolean success = dbUtil.changeUserName(oldName, userName);
        if (success) {
            request.getSession().setAttribute(JdfsConstant.SESSION_LOGIN_FLAG, userName);
            return new R("修改成功");
        } else {
            return new R(204, "用户名已存在");
        }
    }

    @ApiOperation("用户修改密码")
    @PostMapping("/change/passWord")
    public R changePwd(HttpServletRequest request, @RequestBody String passWord) {
        String userName = (String) request.getSession().getAttribute(JdfsConstant.SESSION_LOGIN_FLAG);
        if (StringUtils.isBlank(userName)) {
            return new R(204, "请重新登录");
        }
        dbUtil.changeUserPassWord(userName, passWord);
        return new R("修改成功");
    }
}
