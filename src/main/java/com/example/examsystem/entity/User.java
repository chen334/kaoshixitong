package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer phone;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer password;
    private Integer address;
    private Integer nickname;
    private Integer type;
}
