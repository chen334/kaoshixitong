package com.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.entity.StudentPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentPaperMapper extends BaseMapper<StudentPaper> {
    @Select("SELECT id,exam_name FROM exam WHERE id in (SELECT DISTINCT eid from student_paper)")
    List<Exam> SlectExamName();
}
