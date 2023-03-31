package com.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.examsystem.entity.Question;
import com.example.examsystem.entity.QuestionPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionPaperMapper extends BaseMapper<QuestionPaper> {
    @Select("SELECT * FROM question_paper LEFT JOIN question ab on question_paper.qid=ab.id WHERE question_paper.eid=#{pid}")
    public List<Question> selectQuestions(Integer pid);
}
