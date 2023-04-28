package com.example.examsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.examsystem.entity.StudentPaper;

import java.util.Map;

public interface StudentPaperService extends IService<StudentPaper> {
    public default IPage<Map<String,Object>> getFilteredPageByStableAndClass(int stable, int uid, Page<Map<String, Object>> page, Integer filter){
        LambdaQueryWrapper<StudentPaper> queryWrapper = new LambdaQueryWrapper<>();
        if (stable ==1){
            queryWrapper.eq(StudentPaper::getExam_stable,1);
        }else {
            queryWrapper.eq(StudentPaper::getExam_stable,0);
        }
        if (filter!=null){
            queryWrapper.like(StudentPaper::getEid,filter);
        }
        return this.pageMaps(page,queryWrapper);
    }
}
