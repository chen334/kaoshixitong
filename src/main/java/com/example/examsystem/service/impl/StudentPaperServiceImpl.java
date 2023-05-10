package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.entity.StudentPaper;
import com.example.examsystem.mapper.StudentPaperMapper;
import com.example.examsystem.service.StudentPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentPaperServiceImpl extends ServiceImpl<StudentPaperMapper, StudentPaper> implements StudentPaperService {
    @Autowired
    private StudentPaperMapper studentPaperMapper;

    @Override
    public List<Exam> SelectExamName() {
        return studentPaperMapper.SlectExamName();
    }
}
