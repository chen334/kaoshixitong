package com.example.examsystem.controller;



import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.common.R;
import com.example.examsystem.entity.User;
import com.example.examsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;

    @PostMapping("/login")
    public R<User> login(@RequestBody User user, HttpServletRequest servletRequest, HttpServletResponse response){
        String name = user.getUsername();
        String password = String.valueOf(user.getPassword());
        if (StringUtils.isBlank(name)||StringUtils.isBlank(String.valueOf(password))){
            R.error("请输入用户名和密码");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,name);
        queryWrapper.eq(User::getPassword,password);
        User user1 = userService.getOne(queryWrapper);
        if (user1!=null){
            log.info(String.valueOf(user1));
//            request.getSession().setAttribute("user",user1.getId());
            session.setAttribute("user",user1.getId());
//            String session_id = String.valueOf(user1.getId());
//            Cookie cookie = new Cookie("user", session_id);
//            cookie.setPath("/");
//            cookie.setMaxAge(30 * 60); // 设置Cookie的有效期为30分钟
//            response.addCookie(cookie);
            
            Object user2 = session.getAttribute("user");
            log.info("1111111111111111111111111111111");
            log.info(String.valueOf(user2));
            log.info("1111111111111111111111111111111");
            return R.success(user1);
        }
        return R.error("账号密码不正确");
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session){
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/page")
    public R<Page> page(@RequestParam int pagenum, @RequestParam int pagesize, @RequestParam String username,@RequestParam String type){
        Page<User> userPage = new Page<>(pagenum,pagesize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(username!=null,User::getUsername,username);
        queryWrapper.like(type!=null,User::getType,type);
        userService.page(userPage,queryWrapper);
        log.info(String.valueOf(userPage));
        return R.success(userPage);
    }

    @PostMapping("/generator")
    public R generator(@RequestParam int classCode,@RequestParam int Num) {
        List<Integer> stuNum = new ArrayList<>();
        List<User> list = new ArrayList<>();
        for (int i=1;i<Num;i++){
            User user = new User();
            if (i<10){
                user.setUsername(classCode+String.valueOf(0)+String.valueOf(i));
            }else {
                user.setUsername(classCode+String.valueOf(i));
            }
            user.setPassword(123456);
            list.add(user);
        }
        userService.saveBatch(list);
        log.info(String.valueOf(stuNum));
        return R.success("成功");
    }

    @PostMapping("/save")
    public R<Boolean> save(@RequestBody User user){
        return R.success(userService.saveOrUpdate(user));
    }

    @PostMapping("/del/{id}")
    public R<Boolean> del(@PathVariable int id){
        return R.success(userService.removeById(id));
    }

    @PostMapping("/del/batch")
    public R deleteBatch(@RequestBody List<Integer> ids){
        return R.success(userService.removeByIds(ids));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
//        List<User> list = userMapper.findAll();
        List<User> list = userService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list,true);

        //浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }

    @PostMapping("/import")
    public R imp(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> list = reader.readAll(User.class);
        userService.saveBatch(list);
        return R.success(true);
    }

    @PostMapping("/geturl")
    public R geturl(@RequestBody Map<String, Integer> params){
        int uid = params.get("uid");
        log.info(String.valueOf(uid));
        return R.success(userService.listByIds(Collections.singleton(uid)));
    }

//    @PostMapping("/importUsers")
//    public ResponseEntity<String> importUsers(@RequestParam("file") MultipartFile file) {
//        try {
//            // 读取 Excel 文件
//            Workbook workbook = new XSSFWorkbook(file.getInputStream());
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // 遍历每一行
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//
//                // 从 Excel 行中读取用户信息
//                String username = row.getCell(0).getStringCellValue();
//                String email = row.getCell(1).getStringCellValue();
//                String password = row.getCell(2).getStringCellValue();
//
//                // 创建一个新的 User 对象
//                User user = new User();
//                user.setUsername(username);
//                user.setEmail(email);
//                user.setPassword(password);
//
//                // 保存新创建的用户
//                userService.save(user);
//            }
//
//            // 关闭 Workbook
//            workbook.close();
//
//            return ResponseEntity.ok("Users imported successfully!");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import users.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
}
