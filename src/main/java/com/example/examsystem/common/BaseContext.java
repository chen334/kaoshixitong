package com.example.examsystem.common;

/**
 * 基于ThreadLocal封装工具类,用户保存和获取当前登录用户的id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Integer id){
        threadLocal.set(Long.valueOf(id));
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
