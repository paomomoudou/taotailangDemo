package com.moudou.taotailangdemo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moudou.taotailangdemo.entity.Message;
import org.springframework.stereotype.Service;

@Service
public interface MessageService extends IService<Message> {
    IPage<Message> listByPage(Page<Message> page, Integer type);
}
