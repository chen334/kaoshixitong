package com.example.examsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.common.R;
import com.example.examsystem.entity.Question;
import com.example.examsystem.entity.StudentPaper;
import com.example.examsystem.service.StudentPaperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/studentpaper")
@Slf4j
public class StudentPaperController {
    @Autowired
    private StudentPaperService studentPaperService;

    @PostMapping("/save")
    private R save(@RequestBody StudentPaper studentPaper){
        log.info(String.valueOf(studentPaper)+"1111111111111111111111111111111111111");
        if (studentPaper.getId()==null){
            List<StudentPaper> list = studentPaperService.list(new QueryWrapper<StudentPaper>().eq("eid",studentPaper.getEid()));
        }
        studentPaperService.saveOrUpdate(studentPaper);
        return R.success("成功");
    }

    @GetMapping("/page")
    public R<Page> page(@RequestParam int pagenum, @RequestParam int pagesize){
        Page<StudentPaper> studentPaperPage = new Page<>(pagenum,pagesize);
        studentPaperService.page(studentPaperPage);
        log.info(String.valueOf(studentPaperPage));
        return R.success(studentPaperPage);
    }

    @PostMapping("/list")
    public R list(@RequestParam int sp){
        StudentPaper byId = studentPaperService.getById(sp);
        return R.success(byId);
    }
}
