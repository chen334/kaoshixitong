package com.example.examsystem.dto;

import com.example.examsystem.entity.Exam;
import lombok.Data;

@Data
public class ExamDto extends Exam {
    private int cid;
    private int eid;
    private int type1;
    private int type2;
    private int type3;
}
