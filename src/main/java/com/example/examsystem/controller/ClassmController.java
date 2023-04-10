package com.example.examsystem.controller;

import com.example.examsystem.common.R;
import com.example.examsystem.entity.Class;
import com.example.examsystem.entity.Classm;
import com.example.examsystem.service.ClassService;
import com.example.examsystem.service.ClassmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/classm")
@Slf4j
public class ClassmController {
    @Autowired
    private ClassmService classmService;

    @GetMapping("/list")
    public R list(){
        List<Classm> list = classmService.list();
        return R.success(list);
    }
}
