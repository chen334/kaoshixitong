package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Question implements Serializable {
    private int id;
    private int question_type;
    private String className;
    private String question_name;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String t_points;
    private String anser;
    private String create_time;
}
