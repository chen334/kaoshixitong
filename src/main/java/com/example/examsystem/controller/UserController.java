package com.example.examsystem.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.common.R;
import com.example.examsystem.entity.User;
import com.example.examsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(@RequestBody User user, HttpSession session, HttpServletRequest servletRequest, HttpServletResponse response){
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
}
