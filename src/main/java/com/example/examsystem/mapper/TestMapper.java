package com.example.examsystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.examsystem.entity.Test;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BaseMapper<Test> {
}
