package com.moudou.taotailangdemo.vo;
import lombok.Data;
@Data
public class MessageVo {
    private Long id;         // 主键ID
    private String title;     // 消息标题
    private String slogan;    // 标语
    private String content;   // 消息内容
    private String imgUrl;    // 图片地址
    private Integer type;     // 类型：0最新消息，1考试资讯
    private String publishTime; // 发布时间
}
