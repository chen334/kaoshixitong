package com.example.examsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.examsystem.dto.StudentPaperDto;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.entity.StudentPaper;
import com.example.examsystem.mapper.StudentPaperMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


public interface StudentPaperService extends IService<StudentPaper> {

    public default IPage<Map<String,Object>> getFilteredPageByStableAndClass(Page<Map<String, Object>> page, StudentPaperDto studentPaperDto){
        LambdaQueryWrapper<StudentPaper> queryWrapper = new LambdaQueryWrapper<>();
        if (studentPaperDto.getExam_stable()==1){
            queryWrapper.eq(StudentPaper::getExam_stable,1);
        }else {
            queryWrapper.eq(StudentPaper::getExam_stable,0);
        }
        System.out.println("000000000000000000000000000000000000");
        System.out.println(studentPaperDto.getClassId());
        if (!studentPaperDto.getClassId().equals("all")){
            System.out.println("111111111111111111111111111111111111111111111");
            System.out.println(studentPaperDto.getClassId());
            queryWrapper.eq(StudentPaper::getClassId,studentPaperDto.getClassId());
        }
        if (!studentPaperDto.getEid().equals("all")){
            System.out.println("Ename!!!!!!!");
            queryWrapper.eq(StudentPaper::getEid,studentPaperDto.getEid());
        }
        System.out.println("querywrapper!!!!!");
        System.out.println(queryWrapper);
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

//    public default IPage<Map<String,Object>> getFilteredPageByStableAndClass(int uid, Page<Map<String, Object>> page, int stable,String Ename){
//        LambdaQueryWrapper<StudentPaper> queryWrapper = new LambdaQueryWrapper<>();
//        if (stable ==1){
//            queryWrapper.eq(StudentPaper::getExam_stable,1);
//        }else {
//            queryWrapper.eq(StudentPaper::getExam_stable,0);
//        }
//        if (Ename!=null){
//            queryWrapper.eq(StudentPaper::getEid,Ename);
//        }
//        return this.pageMaps(page,queryWrapper);
//    }


    List<Exam> SelectExamName();
}
