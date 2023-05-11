package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.examsystem.entity.StudentExamProgress;
import com.example.examsystem.mapper.StudentExamProgressMapper;
import com.example.examsystem.service.StudentExamProgressService;
import com.example.examsystem.service.StudentPaperService;
import org.springframework.stereotype.Service;

@Service
public class StudentExamProgressServiceImpl extends ServiceImpl<StudentExamProgressMapper, StudentExamProgress> implements StudentExamProgressService {
}
