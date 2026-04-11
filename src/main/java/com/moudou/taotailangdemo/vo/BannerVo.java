package com.moudou.taotailangdemo.vo;

import lombok.Data;

@Data
public class BannerVo {
    private Long id;
    private String imgUrl;
    private Integer sort;
    // 1启用 0禁用
    private Integer status;
    private String createTime;
    private String updateTime;
}
