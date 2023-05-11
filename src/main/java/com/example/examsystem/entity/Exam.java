package com.example.examsystem.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Exam implements Serializable {
    private int id;
    private String classId;
    private String exam_name;
    private String exam_room;
    private LocalDateTime exam_time;
    private LocalDateTime exam_endtime;
    private Integer exam_duration;
    private String teacher;
    private String stable;
    private LocalDateTime create_time;
}
