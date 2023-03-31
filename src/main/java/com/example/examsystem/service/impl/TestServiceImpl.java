package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.examsystem.entity.Test;
import com.example.examsystem.mapper.TestMapper;
import com.example.examsystem.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {
}
