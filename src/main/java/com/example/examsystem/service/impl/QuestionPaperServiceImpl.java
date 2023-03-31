package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.examsystem.entity.Question;
import com.example.examsystem.entity.QuestionPaper;
import com.example.examsystem.mapper.QuestionPaperMapper;
import com.example.examsystem.service.QuestionPaperService;
import com.example.examsystem.service.QuestionService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionPaperServiceImpl extends ServiceImpl<QuestionPaperMapper, QuestionPaper> implements QuestionPaperService {


}
