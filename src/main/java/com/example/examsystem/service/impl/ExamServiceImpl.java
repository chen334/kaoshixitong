package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.entity.User;
import com.example.examsystem.mapper.ExamMapper;
import com.example.examsystem.service.ExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {
    @Autowired
    private ExamService examService;

    @Override
    public List<Exam> listByStable(int stable) {
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Exam::getStable,stable);
        log.info(String.valueOf(queryWrapper));
        List<Exam> list = examService.list(queryWrapper);
        return list;
    }
    @Override
    public String getEaxmnameByEid(Integer userId) {
        Exam exam= examService.getById(userId);
        return exam !=null?exam.getExam_name():null;
    }
}
