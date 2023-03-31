package com.example.examsystem.dto;

import com.example.examsystem.entity.Exam;
import lombok.Data;

import java.util.List;

@Data
public class HandleExamDto extends Exam {
    private Integer eid;
    private List<Integer> handleQuestionIds;
}
