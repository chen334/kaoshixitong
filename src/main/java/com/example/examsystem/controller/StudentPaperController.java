package com.example.examsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.common.R;
import com.example.examsystem.entity.StudentPaper;
import com.example.examsystem.service.StudentPaperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/studentpaper")
@Slf4j
public class StudentPaperController {
    @Autowired
    private StudentPaperService studentPaperService;

    @PostMapping("/save")
    private R save(@RequestBody StudentPaper studentPaper,HttpSession session) {
        int id = 0;
//        Object user = session.getAttribute("user");
        Integer uid = studentPaper.getUid();
        Integer eid = studentPaper.getEid();
        int count = studentPaperService.count(new QueryWrapper<StudentPaper>().eq("uid", uid).eq("eid",eid));
        System.out.println(studentPaper);
        if (count > 0) {
            // 学生ID已存在，返回错误信息
            return R.error("学生已提交试卷");
        } else {
            // 学生ID不存在，插入数据
            studentPaper.setExam_stable(1);
            studentPaperService.saveOrUpdate(studentPaper);
            return R.success("成功");
        }
    }

    @GetMapping("/page")
    public R<Page> page(@RequestParam int pagenum, @RequestParam int pagesize){
        Page<StudentPaper> studentPaperPage = new Page<>(pagenum,pagesize);
        studentPaperService.page(studentPaperPage);
        return R.success(studentPaperPage);
    }

    @PostMapping("/list")
    public R list(@RequestParam int sp){
        StudentPaper byId = studentPaperService.getById(sp);
        return R.success(byId);
    }
}
