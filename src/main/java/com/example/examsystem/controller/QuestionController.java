package com.example.examsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.common.R;
import com.example.examsystem.entity.Class;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.entity.Question;
import com.example.examsystem.entity.User;
import com.example.examsystem.mapper.ClassMapper;
import com.example.examsystem.service.ClassService;
import com.example.examsystem.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private ClassService classService;

    @GetMapping("/page")
    public R<Page> page(@RequestParam int pagenum, @RequestParam int pagesize,@RequestParam String type){
        Page<Question> questionPage = new Page<>(pagenum,pagesize);
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(type!=null,Question::getQuestion_type,type);
        questionService.page(questionPage,queryWrapper);
        log.info(String.valueOf(questionPage));
        return R.success(questionPage);
    }

    @GetMapping("/list")
    public R list(){
        List<Question> list = questionService.list();
        return R.success(list);
    }

    @GetMapping("/class")
    public R<List<Class>> classd(){
//        List<Class> classes = classMapper.listClassName();
        return R.success(classService.list());
    }

    @PostMapping("/del/{id}")
    public R<Boolean> del(@PathVariable int id){
        return R.success(questionService.removeById(id));
    }

    @PostMapping("/save")
    public R<Boolean> save(@RequestBody Question question){
        return R.success(questionService.saveOrUpdate(question));
    }

    @PostMapping("/del/batch")
    public R deleteBatch(@RequestBody List<Integer> ids){
        return R.success(questionService.removeByIds(ids));
    }
}
