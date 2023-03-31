package com.example.examsystem.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Exam implements Serializable {
    private int id;
    private int classId;
    private String exam_name;
    private String exam_room;
    private String exam_time;
    private String teacher;
    private String stable;

}
