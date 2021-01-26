//package com.alex.spring.boot.blog.controller;
//import com.alex.spring.boot.blog.domain.User;
//import com.alex.spring.boot.blog.dto.Result;
//import com.alex.spring.boot.blog.dto.StatusCode;
//import com.alex.spring.boot.blog.service.UserService;
//import com.alex.spring.boot.blog.util.FormatUtil;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.bind.annotation.*;
//import java.util.*;
//
///**
// * 主页控制器
// */
//@Api(tags ={"MainController"})
//@RestController
//@RequestMapping("/")
//public class MainController {
//
//    //private static final Long ROLE_USER_AUTHORITY_ID = 2L;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private FormatUtil formatUtil;
//
//
//    /**
//     * 登录返回token
//     */
//    @ApiOperation(value = "用户登录", notes = "用户名+密码 name+password 返回token")
//    @PostMapping("/login")
//    public Result login(User user) {
//        if (!formatUtil.checkStringNull(user.getName(), user.getPassword())) {
//            return Result.create(StatusCode.ERROR, "参数错误");
//        }
//
//        try {
//            Map map = userService.login(user);
//            return Result.create(StatusCode.OK, "登录成功",map);
//        } catch (UsernameNotFoundException unfe) {
//            return Result.create(StatusCode.LOGINERROR, "登录失败，用户名或密码错误");
//        } catch (RuntimeException re) {
//            return Result.create(StatusCode.LOGINERROR, re.getMessage());
//        }
//
//    }
//
//
//    /**
//     * 用户退出登录
//     */
//    @ApiOperation(value = "用户退出登录")
//    @GetMapping("/logout")
//    public Result logout() {
//
//        userService.logout();
//        return Result.create(StatusCode.OK, "退出成功");
//    }
//
//
//    /**
//     * 用户注册
//     */
//    @ApiOperation(value = "用户注册", notes = "用户名+密码+邮箱 name+password+mail")
//    @PostMapping("/register")
//    public Result register(User user) {
//        if (!formatUtil.checkStringNull(
//                user.getName(),
//                user.getPassword(),
//                user.getMail())){
//            return Result.create(StatusCode.ERROR, "注册失败，字段不完整");
//        }
//        try {
//            userService.register(user);
//            return Result.create(StatusCode.OK, "注册成功");
//        } catch (RuntimeException e) {
//            return Result.create(StatusCode.ERROR, "注册失败，" + e.getMessage());
//        }
//    }
//
//}
//


