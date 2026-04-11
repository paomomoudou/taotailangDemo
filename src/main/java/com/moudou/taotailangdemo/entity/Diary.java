package com.moudou.taotailangdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("diary")
public class Diary {
    @TableId(type = IdType.AUTO)
    private Long id;         // 主键ID
    private String title;     // 日记标题
    private String mediaUrl;  // 媒体地址（图片/视频）
    private String mediaType; // 媒体类型（image/video）
    private LocalDateTime publishTime; // 发布时间
}
