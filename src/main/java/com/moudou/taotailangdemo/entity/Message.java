package com.moudou.taotailangdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("message")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;         // 主键ID
    private String title;     // 消息标题
    private String slogan;    // 标语
    private String content;   // 消息内容
    private String imgUrl;    // 图片地址
    private Integer type;     // 类型：0最新消息，1考试资讯
    private LocalDateTime publishTime; // 发布时间
}
