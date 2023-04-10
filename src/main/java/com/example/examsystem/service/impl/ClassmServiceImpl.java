package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.examsystem.entity.Classm;
import com.example.examsystem.mapper.ClassMapper;
import com.example.examsystem.mapper.ClassmMapper;
import com.example.examsystem.service.ClassmService;
import org.springframework.stereotype.Service;

@Service
public class ClassmServiceImpl extends ServiceImpl<ClassmMapper, Classm> implements ClassmService {
}
