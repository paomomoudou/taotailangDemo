package com.moudou.taotailangdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("banner")
public class Banner {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String imgUrl;
    //private String linkUrl;
    private Integer sort;
    // 1启用 0禁用
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    public Integer getSort(){
        return sort;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
