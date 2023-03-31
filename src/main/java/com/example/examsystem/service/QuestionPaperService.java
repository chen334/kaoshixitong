package com.example.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.examsystem.entity.Question;
import com.example.examsystem.entity.QuestionPaper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface QuestionPaperService extends IService<QuestionPaper> {
//    @Select("SELECT q.* from paper_question pq left join question q on pq.qid = q.id WHERE pq.pid = #{pid}")
//    @Select("SELECT q.* from exam pq left join question q on pq.qid = q.id WHERE pq.pid = #{pid}")

}
