package com.moudou.taotailangdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moudou.taotailangdemo.entity.Message;
import com.moudou.taotailangdemo.mapper.MessageMapper;
import com.moudou.taotailangdemo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Override
    public IPage<Message> listByPage(Page<Message> page, Integer type) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<Message>()
                .orderByDesc(Message::getPublishTime); // 按发布时间倒序
        if (type != -1) {
            wrapper.eq(Message::getType, type); // 按类型筛选
        }
        return messageMapper.selectPage(page, wrapper);
    }
}
