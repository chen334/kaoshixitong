package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;

@Data
public class QuestionPaper {
    @TableId(type = IdType.AUTO)
    private int id;
    private int qid;
    private int eid;
}
