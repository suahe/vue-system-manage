package com.example.manageSystem.admin.module.login.controller;

import com.example.common.entity.response.Result;
import com.example.common.entity.response.StatusCode;
import com.example.common.utils.MD5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


/**
 * 登陆控制器
 */
@RestController
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/login")
    public Result login(
            @RequestParam(value = "username", required = true) String userName,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "rememberMe", required = true, defaultValue = "false") boolean rememberMe
    ) {
        logger.info("当前登录用户参数：" + userName + password + rememberMe);
        Subject subject = SecurityUtils.getSubject();
        password= MD5Utils.getMD5(password);
        logger.info("MD5加密后密码："+password);
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        token.setRememberMe(rememberMe);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return  Result.error(StatusCode.LOGINERROR,"您的账号或密码输入错误");
        }
        return Result.ok("登陆成功");
    }

   /* @RequestMapping("/")
    public String index() {
        return "no permission";
    }*/
}
