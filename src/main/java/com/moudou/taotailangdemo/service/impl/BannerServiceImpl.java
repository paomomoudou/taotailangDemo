package com.moudou.taotailangdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moudou.taotailangdemo.entity.Banner;
import com.moudou.taotailangdemo.mapper.BannerMapper;
import com.moudou.taotailangdemo.service.BannerService;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
}
