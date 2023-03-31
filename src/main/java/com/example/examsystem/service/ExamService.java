package com.example.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.examsystem.entity.Exam;

import java.util.List;

public interface ExamService extends IService<Exam> {
    public List<Exam> listByStable(int stable);
}
