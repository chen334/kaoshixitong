package com.example.examsystem.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.examsystem.common.R;
import com.example.examsystem.entity.Test;
import com.example.examsystem.entity.User;
import com.example.examsystem.mapper.TestMapper;
import com.example.examsystem.service.TestService;
import com.example.examsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonConroller {
    @Autowired
    private TestMapper testMapper;
    @Value("${exam.path}")
    private String basePath;
    @Autowired
    private TestService testService;
    @Autowired
    private UserService userService;

//    @PostMapping("/upload")
//    public R<String> upload(MultipartFile file){
//        String originalFilename = file.getOriginalFilename();
//        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String fileName = UUID.randomUUID().toString()+substring;
//        File dir = new File(basePath);
//        if (!dir.exists()){
//            dir.mkdirs();
//        }
//        try {
//            file.transferTo(new File(basePath+fileName));
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        return R.success(fileName);
//    }

//    @GetMapping  ("/download")
//    public void download(@RequestParam String data, HttpServletResponse response){
//        log.info("123123123123");
//        try {
//            FileInputStream fileInputStream = new FileInputStream(new File(basePath + data));
//            ServletOutputStream outputStream = response.getOutputStream();
//            response.setContentType("image/png");
//            int len=0;
//            byte[] bytes = new byte[1024];
//            while ((len = fileInputStream.read()) != -1){
//                outputStream.write(bytes,0,len);
//                outputStream.flush();
//            }
//            outputStream.close();
//            fileInputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file, HttpSession session) throws IOException {
        log.info("111111");
        String originalFilename =file.getOriginalFilename();
        String type= FileUtil.extName(originalFilename);
        long size=file.getSize();
        //先存储到磁盘
        File uploadFile = new File(basePath);
        if (!uploadFile.exists()){
            uploadFile.mkdir();
        };
        String uuid = IdUtil.fastSimpleUUID();
        String fileUuid = uuid + StrUtil.DOT + type;
        File finaluploadFile = new File(basePath + fileUuid);
        String url;
        //获取文件md5
//        String md5 = SecureUtil.md5(uploadFile);
        String md5 = SecureUtil.md5(file.getInputStream());
//        Test files = getFileByMd5(md5);
//        if (files != null){
//            url = files.getUrl();
//        }else {
            //把获取到的文件存储到磁盘目录
            file.transferTo(finaluploadFile);
            url = "http://localhost:8086/common/" + fileUuid;
//        }

        //存储数据库
//        Test saveFile = new Test();
//        saveFile.setName(originalFilename);
//        saveFile.setUrl(url);
//        log.info(String.valueOf(session.getAttribute("user")));
        User saveAvege = new User();
        saveAvege.setId((Integer) session.getAttribute("user"));
        saveAvege.setUrl(url);
        userService.updateById(saveAvege);
//        testMapper.insert(saveFile);
        return url;
    }

    @GetMapping("/list")
    private R list(@RequestParam int id){
        return R.success(testService.getById(id));
    }
//    private Test getFileByMd5(String md5){
//        //查询文件的md5是否存在
//        QueryWrapper<Test> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("md5",md5);
//        return testMapper.selectOne(queryWrapper);
//    }


    /**
     * 文件下载接口 http:////localhost:9090/file/{fileUuid};
     * @param fileUuid
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUuid}")
    public void download(@PathVariable String fileUuid, HttpServletResponse response) throws IOException {
        //根据文件唯一标识码获取文件
        File finaluploadFile = new File(basePath + fileUuid);
        //设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileUuid,"UTF-8"));
        response.setContentType("application/octet-stream");

        //读取文件的字节流
        os.write(FileUtil.readBytes(finaluploadFile));
        os.flush();
        os.close();
    }
}
