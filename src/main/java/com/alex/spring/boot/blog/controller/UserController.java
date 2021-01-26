package com.alex.spring.boot.blog.controller;

import com.alex.spring.boot.blog.domain.User;
import com.alex.spring.boot.blog.dto.PageResult;
import com.alex.spring.boot.blog.dto.Result;
import com.alex.spring.boot.blog.dto.StatusCode;
import com.alex.spring.boot.blog.service.UserService;
import com.alex.spring.boot.blog.util.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Api(tags = "用户api", description = "用户api", basePath = "/user")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FormatUtil formatUtil;


    /**
     * 登录返回token
     */
    @ApiOperation(value = "用户登录", notes = "用户名+密码 name+password 返回token")
    @PostMapping("/login")
    public Result login(User user) {
        if (!formatUtil.checkStringNull(user.getName(), user.getPassword())) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }

        try {
            Map map = userService.login(user);
            return Result.create(StatusCode.OK, "登录成功",map);
        } catch (UsernameNotFoundException unfe) {
            return Result.create(StatusCode.LOGINERROR, "登录失败，用户名或密码错误");
        } catch (RuntimeException re) {
            return Result.create(StatusCode.LOGINERROR, re.getMessage());
        }

    }


    /**
     * 用户退出登录
     */
    @ApiOperation(value = "用户退出登录")
    @GetMapping("/logout")
    public Result logout() {

        userService.logout();
        return Result.create(StatusCode.OK, "退出成功");
    }


    /**
     * 用户注册
     */
    @ApiOperation(value = "用户注册", notes = "用户名+密码+邮箱 name+password+mail")
    @PostMapping("/register")
    public Result register(User user) {
        if (!formatUtil.checkStringNull(
                user.getName(),
                user.getPassword(),
                user.getMail())){
            return Result.create(StatusCode.ERROR, "注册失败，字段不完整");
        }
        try {
            userService.register(user);
            return Result.create(StatusCode.OK, "注册成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "注册失败，" + e.getMessage());
        }
    }
    /**
     * 用户封禁或解禁
     *
     * @param id
     * @param state
     * @return
     */
    @ApiOperation(value = "用户封禁或解禁", notes = "用户id+状态 id+state")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/ban/{id}/{state}")
    public Result banUser(@PathVariable Integer id, @PathVariable Integer state) {

        if (!formatUtil.checkObjectNull(id, state)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }


        if (state == 0) {
            userService.updateUserState(id, state);
            return Result.create(StatusCode.OK, "封禁成功");
        } else if (state == 1) {
            userService.updateUserState(id, state);
            return Result.create(StatusCode.OK, "解禁成功");
        } else {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
    }
    @ApiOperation(value = "分页查询用户", notes = "页码+显示数量")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{page}/{showCount}")
    public Result findUser(@PathVariable Integer page, @PathVariable Integer showCount) {
        if (!formatUtil.checkPositive(page, showCount)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        PageResult<User> pageResult =
                new PageResult<>(userService.getUserCount(), userService.findUser(page, showCount));
        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }


    @ApiOperation(value = "根据用户名分页搜索用户", notes = "页码+显示数量+搜索内容")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/search/{page}/{showCount}")
    public Result searchUser(String userName, @PathVariable Integer page, @PathVariable Integer showCount) {
        if (!formatUtil.checkStringNull(userName)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        if (!formatUtil.checkPositive(page, showCount)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        PageResult<User> pageResult =
                new PageResult<>(userService.getUserCountByName(userName), userService.searchUserByName(userName, page, showCount));

        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }
}
