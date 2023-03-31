package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.examsystem.entity.StudentPaper;
import com.example.examsystem.mapper.StudentPaperMapper;
import com.example.examsystem.service.StudentPaperService;
import org.springframework.stereotype.Service;

@Service
public class StudentPaperServiceImpl extends ServiceImpl<StudentPaperMapper, StudentPaper> implements StudentPaperService {
}
