package com.example.examsystem.dto;

import com.example.examsystem.entity.StudentPaper;
import lombok.Data;

@Data
public class StudentPaperDto extends StudentPaper {
    private int page;
    private int size;
}
