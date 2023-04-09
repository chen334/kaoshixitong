package com.example.examsystem.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.common.R;
import com.example.examsystem.common.StreamUtil;
import com.example.examsystem.dto.ExamDto;
import com.example.examsystem.dto.HandleExamDto;
import com.example.examsystem.entity.*;
import com.example.examsystem.entity.Class;
import com.example.examsystem.mapper.ExamMapper;
import com.example.examsystem.mapper.QuestionPaperMapper;
import com.example.examsystem.mapper.StudentPaperMapper;
import com.example.examsystem.mapper.TestMapper;
import com.example.examsystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exam")
@Slf4j
public class ExamController {
    @Autowired
    private ExamService examService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionPaperService questionPaperService;
    @Autowired
    private QuestionPaperMapper questionPaperMapper;
    @Autowired
    private ClassService classService;
    @Autowired
    private TestService testService;
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private StudentPaperMapper studentPaperMapper;
    @Autowired
    private ExamMapper examMapper;


    @Value("${file.upload-path}")
            private String pictureurl;


    private static final String host="http://localhost";

    @GetMapping("/page")
    public R<Page> page(@RequestParam int pagenum, @RequestParam int pagesize){
        Page<Exam> questionPage = new Page<>(pagenum,pagesize);
        examService.page(questionPage);
        log.info(String.valueOf(questionPage));
        return R.success(questionPage);
    }

    @PostMapping("/del/{id}")
    public R<Boolean> del(@PathVariable int id){
        return R.success(examService.removeById(id));
    }

    @PostMapping("/save")
    public R<Boolean> save(@RequestBody Exam exam){
        exam.setCreate_time(new Date());
        return R.success(examService.saveOrUpdate(exam));
    }

    @PostMapping("/del/batch")
    public R deleteBatch(@RequestBody List<Integer> ids){
        return R.success(examService.removeByIds(ids));
    }

    @PostMapping("/handlepaper")
    public R handlepaper(@RequestBody HandleExamDto examDto){
        UpdateWrapper<QuestionPaper> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("eid",examDto.getEid());
        questionPaperService.remove(updateWrapper);
        List<Integer> handleQuestionIds = examDto.getHandleQuestionIds();
        List<QuestionPaper> list = new ArrayList<>();
        for (Integer handleids:handleQuestionIds){
            QuestionPaper questionPaper = new QuestionPaper();
            questionPaper.setEid(examDto.getEid());
            questionPaper.setQid(handleids);
            list.add(questionPaper);
        }
        questionPaperService.saveBatch(list);
        return R.success("组卷成功");
    }


    @PostMapping("/takePaper")
    public R takePaper(@RequestBody ExamDto user){
        UpdateWrapper<QuestionPaper> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("eid",user.getEid());
        questionPaperService.remove(updateWrapper);
          log.info(String.valueOf(user.getEid()));

        log.info(String.valueOf(user));
        List<Question> questionList = questionService.list();
        List<Question> type1List = questionList.stream().filter(question -> question.getQuestion_type()==1).collect(Collectors.toList());
        List<Question> type2List = questionList.stream().filter(question -> question.getQuestion_type()==2).collect(Collectors.toList());
        List<Question> type3List = questionList.stream().filter(question -> question.getQuestion_type()==3).collect(Collectors.toList());


//        List<QuestionPaper> QP = getPaperQuestion(questionList.size(),user.getType(),questionList,user.getPid());
        List<QuestionPaper> QP = getPaperQuestion(type1List.size(),user.getType1(),type1List,user.getEid());
        QP.addAll(getPaperQuestion(type2List.size(),user.getType2(),type2List,user.getEid()));
        QP.addAll(getPaperQuestion(type3List.size(),user.getType3(),type3List,user.getEid()));
        log.info("111111111111111111111111111111111111111111");
        questionPaperService.saveBatch(QP);
//        log.info(String.valueOf(questionPaperService.saveBatch(QP)));
        return R.success("成功");
    }

    private List<QuestionPaper> getPaperQuestion(int questionSize,int QuestionpaperSize,List<Question> source,Integer eid){

        List<Integer> typeRandomList =getEleList(questionSize,QuestionpaperSize);
        List<QuestionPaper> list = new ArrayList<>();
        for (Integer index:typeRandomList){
            Question question =source.get(index);
            QuestionPaper questionPaper = new QuestionPaper();
            questionPaper.setEid(eid);
            questionPaper.setQid(question.getId());
            list.add(questionPaper);
        }
        return list;
    }

    private List<Integer> getEleList(int sourceSize,int resultSize){
        List<Integer> list = CollUtil.newArrayList();
        for (int i = 0 ;i<sourceSize; i++){
            list.add(i);
        }
        return RandomUtil.randomEleList(list,resultSize);
    }

    @GetMapping("/view/{pid}")
    public R view(@PathVariable Integer pid){
        List<Question> list = questionPaperMapper.selectQuestions(pid);
        log.info(String.valueOf(list));
        return R.success(list);
    }

    @PostMapping("/list")
    public R list(){
        List<Exam> list = examService.list();
        return R.success(list);
    }

    @PostMapping("/listOn")
    public R listOn(@RequestParam int stable){
//        List<Exam> list = examService.list();
        List<Exam> list = examService.listByStable(stable);
        return R.success(list);
    }

    @PostMapping("/list123")
    public R list123(@RequestParam int stable,@RequestParam int uid){
        LambdaQueryWrapper<StudentPaper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentPaper::getUid,uid);
        List<StudentPaper> studentPapers = studentPaperMapper.selectList(queryWrapper);

        List<Map<String, Object>> resultList = studentPapers.stream()
                .filter(StreamUtil.distinctByKey(StudentPaper::getEid))
                .map(studentPaper -> {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("eid", studentPaper.getEid());
                    resultMap.put("score", studentPaper.getScore());
                    return resultMap;
                }).collect(Collectors.toList());

        LambdaQueryWrapper<Exam> examQueryWrapper = new LambdaQueryWrapper<>();
        List<Exam> exams = examMapper.selectList(examQueryWrapper);
        List<Exam>  filteredExams;

        if (stable == 0) {
            filteredExams = exams.stream()
                    .filter(exam -> resultList.stream().noneMatch(result -> Objects.equals(result.get("eid"), exam.getId())))
                    .collect(Collectors.toList());
        } else if (stable == 1) {
            filteredExams = exams.stream()
                    .filter(exam -> resultList.stream().anyMatch(result -> Objects.equals(result.get("eid"), exam.getId()) && result.get("score") == null))
                    .collect(Collectors.toList());
        } else if (stable == 2) {
            filteredExams = exams.stream()
                    .filter(exam -> resultList.stream().anyMatch(result -> Objects.equals(result.get("eid"), exam.getId()) && result.get("score") != null))
                    .collect(Collectors.toList());
        } else {
            return R.error("Invalid stable value.");
        }

        log.info("11111111111111111");
        log.info(String.valueOf(filteredExams));

        return R.success(filteredExams);
    }

    @PostMapping("/infolist")
    public R infolist(@RequestParam Integer eid){
        log.info(String.valueOf(eid)+"12333333333333333333333");
        Exam byId = examService.getById(eid);
        return R.success(byId);
    }

    @GetMapping("/class")
    public R<List<Class>> classd(){
//        List<Class> classes = classMapper.listClassName();
        return R.success(classService.list());
    }

//    @PostMapping("/save")
//    public R<Boolean> save(@RequestBody Question question){
//        return R.success(questionService.saveOrUpdate(question));
//    }

//    @PostMapping("/autopaper")
//    private void autoPaper(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException{
//        //获取安排考试的试卷编号
//        int paperId=Integer.parseInt(request.getParameter("paperId"));
//
//
//    }

    @PostMapping("/setimg/{id}")
    public R setImg(@PathVariable("id") int id, @RequestBody MultipartFile file){
        String fileName = file.getOriginalFilename();
        File saveFile = new File(pictureurl);
        UUID uuid = UUID.randomUUID();
        int index = fileName.indexOf(".");
        String newFileName = "/avator."+fileName.replace(".","")+uuid+fileName.substring(index);
        Test test = new Test();
        test.setUrl(newFileName);
        test.setId(id);
        testMapper.updateById(test);
        try{
            file.transferTo(new File(pictureurl + newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Test test1 = testMapper.selectById(test.getId());
        return R.success(MapUtil.builder().put("backUser",test).map());
    }

//    @PostMapping("/upload")
//    public R update(@RequestBody Test files){
//        return R.success(testService.updateById(files));
//    }
    @GetMapping("/test")
    public void test(HttpServletRequest servletRequest){
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        request.getSession().setAttribute("user1","123");
        Object user1 = servletRequest.getSession().getAttribute("user");
        log.info((String) user1);
    }
}
