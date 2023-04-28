package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.examsystem.entity.User;
import com.example.examsystem.mapper.UserMapper;
import com.example.examsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserService userService;
    @Override
    public String getUsernameByUid(Integer userId) {
        User user = userService.getById(userId);
        return user !=null?user.getUsername():null;
    }
}
