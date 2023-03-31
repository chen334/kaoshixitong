package com.example.examsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.common.R;
import com.example.examsystem.entity.Class;
import com.example.examsystem.entity.User;
import com.example.examsystem.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/class")
@Slf4j
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping("/page")
    public R<Page> page(@RequestParam int pagenum, @RequestParam int pagesize, @RequestParam String username){
        Page<Class> userPage = new Page<>(pagenum,pagesize);
        LambdaQueryWrapper<Class> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(username!=null,Class::getClassName,username);
        classService.page(userPage,queryWrapper);
        log.info(String.valueOf(userPage));
        return R.success(userPage);
    }

    @PostMapping("/save")
    public R<Boolean> save(@RequestBody Class cla){
        return R.success(classService.saveOrUpdate(cla));
    }

    @PostMapping("/del/{id}")
    public R<Boolean> del(@PathVariable int id){
        return R.success(classService.removeById(id));
    }

    @PostMapping("/del/batch")
    public R deleteBatch(@RequestBody List<Integer> ids){
        return R.success(classService.removeByIds(ids));
    }
}
