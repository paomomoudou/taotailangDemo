package com.moudou.taotailangdemo.controller;

import com.moudou.taotailangdemo.entity.SysUser;
import com.moudou.taotailangdemo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /** 修改昵称 */
    @PostMapping("/updateNickname")
    public Map<String,Object> updateNickname(@RequestBody Map<String,String> params){
        Map<String,Object> res=new HashMap<>();
        Long userId=Long.valueOf(params.get("userId"));
        String nick=params.get("nickname");
        if(nick==null||nick.isBlank()){
            res.put("code",400);res.put("msg","昵称不能为空");return res;
        }
        SysUser u=new SysUser();
        u.setId(userId);
        u.setNickname(nick);
        boolean ok=sysUserService.updateById(u);
        res.put("code",ok?200:400);
        res.put("msg",ok?"修改成功":"修改失败");
        return res;
    }

    /** 修改密码 */
    @PostMapping("/updatePwd")
    public Map<String,Object> updatePwd(@RequestBody Map<String,String> params){
        Map<String,Object> res=new HashMap<>();
        Long userId=Long.valueOf(params.get("userId"));
        String oldPwd=params.get("oldPwd");
        String newPwd=params.get("newPwd");

        SysUser user=sysUserService.getById(userId);
        if(!passwordEncoder.matches(oldPwd,user.getPassword())){
            res.put("code",400);res.put("msg","原密码错误");return res;
        }
        if(newPwd.length()<6){
            res.put("code",400);res.put("msg","新密码至少6位");return res;
        }
        SysUser u=new SysUser();
        u.setId(userId);
        u.setPassword(passwordEncoder.encode(newPwd));
        boolean ok=sysUserService.updateById(u);
        res.put("code",ok?200:400);
        res.put("msg",ok?"密码修改成功":"失败");
        return res;
    }

    /** 修改手机号（需校验原身份，这里简易版） */
    @PostMapping("/updatePhone")
    public Map<String,Object> updatePhone(@RequestBody Map<String,String> params){
        Map<String,Object> res=new HashMap<>();
        Long userId=Long.valueOf(params.get("userId"));
        String newPhone=params.get("newPhone");
        if(newPhone == null || !newPhone.matches("^1[3-9]\\d{9}$")){
            res.put("code",400);res.put("msg","手机号格式错误");return res;
        }
        // 判断手机号是否已存在
        long cnt=sysUserService.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone,newPhone));
        if(cnt>0){
            res.put("code",400);res.put("msg","该手机号已被注册");return res;
        }
        SysUser u=new SysUser();
        u.setId(userId);
        u.setPhone(newPhone);
        boolean ok=sysUserService.updateById(u);
        res.put("code",ok?200:400);
        res.put("msg",ok?"手机号修改成功":"失败");
        return res;
    }
}
