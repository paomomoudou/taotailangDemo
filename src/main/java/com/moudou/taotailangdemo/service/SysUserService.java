package com.moudou.taotailangdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moudou.taotailangdemo.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    // 登录验证：返回用户信息，失败返回null
    SysUser login(String phone, String password);
    // 注册功能：返回是否成功
    boolean register(String phone, String password, String nickname);
}
