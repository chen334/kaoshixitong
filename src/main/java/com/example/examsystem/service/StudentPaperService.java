package com.example.examsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.examsystem.entity.StudentPaper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


public interface StudentPaperService extends IService<StudentPaper> {
    public default IPage<Map<String,Object>> getFilteredPageByStableAndClass(int uid, Page<Map<String, Object>> page, int stable,String classId){
        LambdaQueryWrapper<StudentPaper> queryWrapper = new LambdaQueryWrapper<>();
        if (stable ==1){
            queryWrapper.eq(StudentPaper::getExam_stable,1);
        }else {
            queryWrapper.eq(StudentPaper::getExam_stable,0);
        }
//        queryWrapper.eq(StudentPaper::getExam_stable)
//        if (filter!=null){
//            queryWrapper.like(StudentPaper::getEid);
//        }

        if (classId!="all"){
            System.out.println("111111111111111111111111111111111111111111111");
            System.out.println(classId);
            queryWrapper.eq(StudentPaper::getClassId,classId);
        }
        return this.pageMaps(page,queryWrapper);
    }
    public default IPage<Map<String,Object>> getFilteredPageByStableAndClass(int uid, Page<Map<String, Object>> page, int stable){
        LambdaQueryWrapper<StudentPaper> queryWrapper = new LambdaQueryWrapper<>();
        if (stable ==1){
            queryWrapper.eq(StudentPaper::getExam_stable,1);
        }else {
            queryWrapper.eq(StudentPaper::getExam_stable,0);
        }
        return this.pageMaps(page,queryWrapper);
    }
}
