package com.example.examsystem.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class StudentExamProgress{
    private int id;
    private int student_id;
    private int exam_id;
    private LocalDateTime start_time;
    private int duration;
    private LocalDateTime end_time;
}
