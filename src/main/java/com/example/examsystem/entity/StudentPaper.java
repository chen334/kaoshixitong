package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class StudentPaper {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String paper;
    private String examinfo;
    private String exam_name;
    private Integer exam_stable;
    private String classId;
    private String eid;
    private Integer uid;
    private String time;
    private Integer score;
}
