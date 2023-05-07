package com.example.examsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.common.R;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.entity.StudentPaper;
import com.example.examsystem.service.ExamService;
import com.example.examsystem.service.StudentPaperService;
import com.example.examsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/studentpaper")
@Slf4j
public class StudentPaperController {
    @Autowired
    private StudentPaperService studentPaperService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExamService examService;

    @PostMapping("/save")
    private R save(@RequestBody StudentPaper studentPaper,HttpSession session) {
//        int id = 0;
////        Object user = session.getAttribute("user");
//        log.info(String.valueOf("studentpaper111111111111111111111111111111111"+studentPaper));
//        Integer uid = studentPaper.getUid();
//        Integer eid = studentPaper.getEid();
//        int count = studentPaperService.count(new QueryWrapper<StudentPaper>().eq("uid", uid).eq("eid",eid));
//        System.out.println(studentPaper);
//        if (count > 0) {
//            // 学生ID已存在，返回错误信息
//            return R.error("学生已提交试卷");
//        } else {
            // 学生ID不存在，插入数据
            studentPaper.setExam_stable(1);
            studentPaperService.saveOrUpdate(studentPaper);
            return R.success("成功");
//        }
    }

    @PostMapping("/stusave")
    private R stusave(@RequestBody StudentPaper studentPaper,HttpSession session) {
        int id = 0;
        Integer uid = studentPaper.getUid();
        Integer eid = studentPaper.getEid();
        int count = studentPaperService.count(new QueryWrapper<StudentPaper>().eq("uid", uid).eq("eid",eid));
        if (count > 0) {
            // 学生ID已存在，返回错误信息
            return R.error("学生已提交试卷");
        } else {
            // 学生ID不存在，插入数据
            studentPaper.setExam_stable(0);
            studentPaperService.saveOrUpdate(studentPaper);
            return R.success("成功");
        }
    }

    @GetMapping("/page")
    public R<Page> page(@RequestParam int pagenum, @RequestParam int pagesize){
        Page<StudentPaper> studentPaperPage = new Page<>(pagenum,pagesize);
        studentPaperService.page(studentPaperPage);
        log.info("page");
        log.info(String.valueOf(studentPaperPage));
        return R.success(studentPaperPage);
    }

    @PostMapping("/listByStableAndClass")
    public R listByStableAndClass(@RequestParam int uid,@RequestParam int page,@RequestParam int size,@RequestParam int stable,@RequestParam String classId){
    log.info(String.valueOf(classId));
    log.info("111111111111111111111");
                Page<Map<String,Object>> pageParam = new Page<>(page,size);
                IPage<Map<String,Object>> resultPage;
                if (!classId.equals("all")){
                    log.info("进1");
                    resultPage = studentPaperService.getFilteredPageByStableAndClass(uid,pageParam,stable,classId);
                }else {
                    log.info("进2");
                    resultPage = studentPaperService.getFilteredPageByStableAndClass(uid,pageParam,stable);
                }
        for (Map<String,Object> record : resultPage.getRecords()){
            Integer userId = (Integer) record.get("uid");
            if (userId!=null){
                String username=userService.getUsernameByUid(userId);
                if (username!=null){
                    record.put("username",username);
                }
            }
            Integer eid = (Integer) record.get("eid");
            if (eid!=null){
                String examname = examService.getEaxmnameByEid(eid);
                if (examname!=null){
                    record.put("examname",examname);
                }
            }
        }
        return R.success(resultPage);
    }

    @GetMapping("/listClass")
    public R listClass(){
        LambdaQueryWrapper<StudentPaper> queryWrapper  = new LambdaQueryWrapper<>();
        queryWrapper.select(StudentPaper::getClassId).groupBy(StudentPaper::getClassId);
        List<StudentPaper> studentPapers = studentPaperService.list(queryWrapper);
        List<Integer> distinctClassIds = studentPapers.stream()
                .map(StudentPaper::getClassId)
                .collect(Collectors.toList());
        return R.success(distinctClassIds);
    }

    @PostMapping("/list")
    public R list(@RequestParam int sp){
        StudentPaper byId = studentPaperService.getById(sp);
        return R.success(byId);
    }
    @GetMapping("/listAll")
    public R list(){
        return R.success(studentPaperService.list());
    }
}
