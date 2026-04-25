package com.moudou.taotailangdemo.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moudou.taotailangdemo.entity.Banner;
import com.moudou.taotailangdemo.service.BannerService;
import com.moudou.taotailangdemo.vo.BannerVo;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/banner")
@CrossOrigin // 允许前端跨域访问（测试环境可用，生产环境建议配置具体域名）
public class BannerController {
    @Resource
    private BannerService bannerService;
    // 后端服务基础地址【关键：替换为你的实际IP+端口】
    // 示例：本地运行=http://localhost:8080 | 服务器运行=http://192.168.1.100:8080
    private static final String BASE_URL = "http://localhost:8080";
    // 时间格式化器：全局统一，避免重复创建
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    // 分页查询（按sort升序、createTime降序，列表顺序统一）
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       Integer status) {
        Page<Banner> pageParam = new Page<>(page, size);
        // 排序规则：排序值越小越靠前，相同排序值则最新创建的在前
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<Banner>()
                .orderByAsc(Banner::getSort)
                .orderByDesc(Banner::getCreateTime);
        // 按状态筛选
        if (status != null ) {
            wrapper.eq(Banner::getStatus, status);
        }
        IPage<Banner> iPage = bannerService.page(pageParam, wrapper);
        return Result.success(iPage);
    }

    // 新增轮播图（统一返回格式，包含data字段）
    @PostMapping("/add")
    public Result add(Banner banner) {
        banner.setCreateTime(LocalDateTime.now());
        banner.setUpdateTime(LocalDateTime.now());
        bannerService.save(banner);
        return Result.success(banner); // 返回复新的轮播图对象，保证data字段存在
    }

    // 修改轮播图状态（启用/禁用）
    @PostMapping("/changeStatus")
    public Result changeStatus(@RequestParam Long id, @RequestParam Integer status) {
        Banner banner = bannerService.getById(id);
        if (banner == null) {
            return Result.fail("轮播图不存在");
        }
        banner.setStatus(status);
        banner.setUpdateTime(LocalDateTime.now());
        bannerService.updateById(banner);
        return Result.success(banner); // 返回复新后的对象，前端可实时刷新
    }

    // 图片上传【核心修复：返回完整可访问路径+文件校验】
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        // 1. 文件大小校验：限制5MB（可根据需求调整）
        if (file.getSize() > 5 * 1024 * 1024) {
            return Result.fail("图片大小不能超过5MB");
        }
        // 2. 文件名非空校验
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            return Result.fail("请选择要上传的图片");
        }
        // 3. 图片类型校验：仅支持jpg/png/jpeg/webp
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        if (!List.of(".jpg", ".png", ".jpeg", ".webp").contains(suffix)) {
            return Result.fail("仅支持jpg、png、jpeg、webp格式的图片");
        }
        // 4. 生成唯一文件名（避免重名覆盖）
        String newFileName = UUID.randomUUID() + suffix;
        // 5. 定义本地保存路径（自动适配Windows/Mac）
        String uploadLocalPath = System.getProperty("user.home") + "/upload/";
        File dirFile = new File(uploadLocalPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs(); // 目录不存在则自动创建
        }
        // 6. 保存图片到本地
        File destFile = new File(dirFile, newFileName);
        file.transferTo(destFile);
        // 7. 拼接前端可访问的完整网络路径并返回
        String imgAccessUrl = "/upload/" + newFileName;
        return Result.success((Object) imgAccessUrl);
    }

    // 轮播图详情查询
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id) {
        Banner banner = bannerService.getById(id);
        if (banner == null) {
            return Result.fail("轮播图不存在");
        }

        BannerVo  bannerVo = new BannerVo();
        bannerVo.setId(banner.getId());
        bannerVo.setImgUrl(banner.getImgUrl());
        bannerVo.setSort(banner.getSort());
        bannerVo.setStatus(banner.getStatus());
        bannerVo.setCreateTime(banner.getCreateTime().format(DATE_FORMATTER));
        bannerVo.setUpdateTime(banner.getUpdateTime().format(DATE_FORMATTER));
        return Result.success(bannerVo);
    }

    // 【核心修改】删除轮播图：GET→POST，路径参数→请求参数，适配前端调用
    @PostMapping("/delete")
    public Result delete(@RequestParam Long id) {
        Banner banner = bannerService.getById(id);
        if (banner == null) {
            return Result.fail("轮播图不存在");
        }
        // 可选优化：删除轮播图时，同时删除本地上传的图片文件

        // 只取 /upload/ 后面的文件名，不要带任何域名
        String imgUrl = banner.getImgUrl();
        if (imgUrl != null && imgUrl.startsWith("/upload/")) {
            String imgName = imgUrl.replace("/upload/", "");
            File imgFile = new File(System.getProperty("user.home") + "/upload/" + imgName);
            if (imgFile.exists()) {
                imgFile.delete();
            }
        }


        bannerService.removeById(id);
        return Result.success("删除成功"); // 返回提示信息，前端可解析
    }
}

/**
 * 统一返回结果类（无需修改）
 */
@Data
class Result {
    private int code;
    private String msg;
    private Object data;

    // 成功：返回数据
    public static Result success(Object data) {
        Result r = new Result();
        r.code = 200;
        r.data = data;
        return r;
    }

    // 成功：仅返回提示信息
    public static Result success(String msg) {
        Result r = new Result();
        r.code = 200;
        r.msg = msg;
        return r;
    }

    // 失败：返回提示信息
    public static Result fail(String msg) {
        Result r = new Result();
        r.code = 500;
        r.msg = msg;
        return r;
    }
}