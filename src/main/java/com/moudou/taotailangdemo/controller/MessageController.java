package com.moudou.taotailangdemo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moudou.taotailangdemo.entity.Message;
import com.moudou.taotailangdemo.service.MessageService;
import com.moudou.taotailangdemo.vo.MessageVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
@RestController
@RequestMapping("/api/message")
@CrossOrigin
public class MessageController {
    @Resource
    private MessageService messageService;
    // 时间格式化器：全局统一，避免重复创建
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    // 分页查询消息（支持按类型检索）
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(defaultValue = "-1") Integer type) {
        Page<Message> pageParam = new Page<>(page, size);
        IPage<Message> iPage = messageService.listByPage(pageParam, type);
        return Result.success(iPage);
    }

    // 消息详情
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        Message message = messageService.getById(id);
        if (message == null) {
            return Result.fail("消息不存在");
        }
        MessageVo messageVo = new MessageVo();
        messageVo.setId(message.getId());
        messageVo.setTitle(message.getTitle());
        messageVo.setSlogan(message.getSlogan());
        messageVo.setContent(message.getContent());
        messageVo.setImgUrl(message.getImgUrl());
        messageVo.setPublishTime(message.getPublishTime().format(DATE_FORMATTER));
        return Result.success(messageVo);
    }

    // 新增消息
    @PostMapping("/add")
    public Result add(@RequestBody Message message) {
        message.setPublishTime(LocalDateTime.now());
        messageService.save(message);
        return Result.success("新增成功");
    }

    // 编辑消息
    @PostMapping("/update")
    public Result update(@RequestBody Message message) {
        messageService.updateById(message);
        return Result.success("编辑成功");
    }

    // 删除消息
    @PostMapping("/delete")
    public Result delete(@RequestParam Long id) {
        messageService.removeById(id);
        return Result.success("删除成功");
    }

    // 图片上传
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        // 图片保存逻辑（与轮播图上传一致）
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        String uploadPath = System.getProperty("user.home") + "/upload/message/";
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(uploadPath + fileName));
        String imgUrl = "/upload/message/" + fileName;
        return Result.success((Object)imgUrl);
    }
}
