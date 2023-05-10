package com.example.examsystem.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Exam implements Serializable {
    private int id;
    private String classId;
    private String exam_name;
    private String exam_room;
    private Date exam_time;
    private Date exam_endtime;
    private Integer exam_duration;
    private String teacher;
    private String stable;
    private Date create_time;
}
