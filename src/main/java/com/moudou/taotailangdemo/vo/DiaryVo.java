package com.moudou.taotailangdemo.vo;

import lombok.Data;

@Data
public class DiaryVo {
    private Long id;         // 主键ID
    private String title;     // 日记标题
    private String mediaUrl;  // 媒体地址（图片/视频）
    private String mediaType; // 媒体类型（image/video）
    private String publishTime; // 发布时间
}
