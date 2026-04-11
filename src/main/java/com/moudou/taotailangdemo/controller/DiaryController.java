package com.moudou.taotailangdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moudou.taotailangdemo.entity.Banner;
import com.moudou.taotailangdemo.entity.Diary;
import com.moudou.taotailangdemo.service.DiaryService;
import com.moudou.taotailangdemo.vo.DiaryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/diary")
@CrossOrigin
public class DiaryController {
    @Autowired
    private DiaryService diaryService;
    // 后端服务基础地址（替换为你的实际IP+端口）
    private static final String BASE_URL = "http://localhost:8080";
    // 时间格式化器：全局统一，避免重复创建
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    // 分页查询日记
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        Page<Diary> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<Diary>()
                .orderByDesc(Diary::getPublishTime);
        IPage<Diary> iPage = diaryService.page(pageParam,wrapper);
        return Result.success(iPage);
    }

    // 日记详情
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id) {
        Diary diary = diaryService.getById(id);
        if (diary == null) {
            return Result.fail("日记不存在");
        }
        DiaryVo  diaryVo = new DiaryVo();
        //BeanUtils.copyProperties(diary, diaryVo);
        diaryVo.setId(diary.getId());
        diaryVo.setTitle(diary.getTitle());
        diaryVo.setMediaUrl(diary.getMediaUrl());
        diaryVo.setMediaType(diary.getMediaType());
        diaryVo.setPublishTime(diary.getPublishTime().format(DATE_FORMATTER));
        return Result.success(diaryVo);
    }

    // 新增日记
    @PostMapping("/add")
    public Result add(@RequestBody Diary diary) {
        diary.setPublishTime(LocalDateTime.now());
        diaryService.save(diary);
        return Result.success("新增成功");
    }

    // 删除日记
    @PostMapping("/delete")
    public Result delete(@RequestBody Diary diary) {
        diaryService.removeById(diary.getId());
        return Result.success("删除成功");
    }

    // 新增：媒体（图片/视频）上传接口
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        // 1. 获取文件后缀
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 2. 生成唯一文件名（避免重名）
        String fileName = UUID.randomUUID() + suffix;
        // 3. 定义保存路径（自动适配Windows/Mac）
        String uploadPath = System.getProperty("user.home") + "/upload/diary/";
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs(); // 目录不存在则创建
        }
        // 4. 保存文件到本地
        File destFile = new File(uploadPath + fileName);
        file.transferTo(destFile);
        // 5. 拼接可访问的网络地址
        String mediaUrl = BASE_URL + "/upload/diary/" + fileName;
        return Result.success((Object)mediaUrl);
    }
}