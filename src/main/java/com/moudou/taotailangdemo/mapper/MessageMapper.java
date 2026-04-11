package com.moudou.taotailangdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moudou.taotailangdemo.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
