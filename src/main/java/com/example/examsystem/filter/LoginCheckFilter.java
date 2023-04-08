//package com.example.examsystem.filter;
//
//import com.alibaba.fastjson.JSON;
//import com.example.examsystem.common.BaseContext;
//import com.example.examsystem.common.R;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.util.AntPathMatcher;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
//@Slf4j
//public class LoginCheckFilter implements Filter {
//    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                cookie.setHttpOnly(true);
//                if ("user".equals(cookie.getName())) {
//                    String value = cookie.getValue();
//                    log.info("Cookie中的user值为：{}", value);
//                }
//            }
//        }
////        String token = request.getHeader("Authorization").replace("Bearer ", "");
////        log.info(token);
//
//        String requestURI=request.getRequestURI();
//        log.info("拦截到请求：{}",requestURI);
//
//        String[] urls=new String[]{
//                "/user/login",
//        };
//
//        boolean check = check(urls,requestURI);
//        if (check){
//            log.info(String.valueOf(request.getSession().getAttribute("user")));
//            log.info("本次请求{}不需要处理",requestURI);
//            filterChain.doFilter(request,response);
//            return;
//        }
//        String user = String.valueOf(request.getSession().getAttribute("user"));
//        log.info(String.valueOf(request.getSession().getAttribute("user")));
//        log.info(user+"222222222222222222");
//
//        if (request.getSession().getAttribute("user")!=null){
//            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
//            Integer userId = (Integer) request.getSession().getAttribute("user");
//            BaseContext.setCurrentId(userId);
//            filterChain.doFilter(request,response);
//            return;
//        }
//        log.info("用户未登录");
//        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
//        return;
//    }
//
//    private boolean check(String[] urls, String requestURI) {
//        for (String url:urls){
//            boolean match = PATH_MATCHER.match(url,requestURI);
//            if (match){
//                return true;
//            }
//        }
//        return false;
//    }
//}
