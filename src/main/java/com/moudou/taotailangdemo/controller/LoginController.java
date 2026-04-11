package com.moudou.taotailangdemo.controller;

import com.moudou.taotailangdemo.entity.SysUser;
import com.moudou.taotailangdemo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private SysUserService sysUserService;

    // 登录接口
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        String phone = params.get("phone");
        String password = params.get("password");

        SysUser user = sysUserService.login(phone, password);
        if (user != null) {
            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("data", user); // 实际项目建议只返回脱敏信息
        } else {
            result.put("code", 400);
            result.put("msg", "手机号或密码错误，或账号已禁用");
        }
        return result;
    }

    // 注册接口
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        String phone = params.get("phone");
        String password = params.get("password");
        String nickname = params.get("nickname");

        // 简单参数校验
        if (phone == null || !phone.matches("^1[3-9]\\d{9}$")) {
            result.put("code", 400);
            result.put("msg", "请输入正确的手机号");
            return result;
        }
        if (password == null || password.length() < 6) {
            result.put("code", 400);
            result.put("msg", "密码长度不能少于6位");
            return result;
        }

        boolean success = sysUserService.register(phone, password, nickname);
        if (success) {
            result.put("code", 200);
            result.put("msg", "注册成功，请登录");
        } else {
            result.put("code", 400);
            result.put("msg", "注册失败，手机号已被占用");
        }
        return result;
    }

}
