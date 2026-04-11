package com.moudou.taotailangdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moudou.taotailangdemo.entity.Diary;
import com.moudou.taotailangdemo.mapper.DiaryMapper;
import com.moudou.taotailangdemo.service.DiaryService;
import org.springframework.stereotype.Service;

@Service
public class DiaryServiceImpl extends ServiceImpl<DiaryMapper, Diary> implements DiaryService {
}
