package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.examsystem.entity.Class;
import com.example.examsystem.mapper.ClassMapper;
import com.example.examsystem.service.ClassService;
import org.springframework.stereotype.Service;

@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
}
