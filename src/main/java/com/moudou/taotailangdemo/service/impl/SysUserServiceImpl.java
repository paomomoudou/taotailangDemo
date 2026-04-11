package com.moudou.taotailangdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moudou.taotailangdemo.entity.SysUser;
import com.moudou.taotailangdemo.mapper.SysUserMapper;
import com.moudou.taotailangdemo.service.SysUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public SysUser login(String phone, String password) {
        // 1. 根据手机号查询用户
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, phone));
        if (user == null) {
            return null; // 手机号不存在
        }
        // 2. 验证密码（BCrypt加密比对）
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null; // 密码错误
        }
        // 3. 验证账号状态
        if (user.getStatus() == 0) {
            return null; // 账号已禁用
        }
        return user; // 登录成功
    }
    @Override
    public boolean register(String phone, String password, String nickname) {
        // 1. 检查手机号是否已被注册
        if (this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, phone)) > 0) {
            return false; // 手机号已存在
        }

        SysUser sysUser = new SysUser();
        sysUser.setPhone(phone);
        sysUser.setPassword(passwordEncoder.encode(password)); // 加密密码
        sysUser.setNickname(nickname);
        sysUser.setStatus(1); // 默认正常

        return this.save(sysUser); // 保存数据库
    }
}
