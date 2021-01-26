package com.alex.spring.boot.blog.service;

import com.alex.spring.boot.blog.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;


/**
 * 用户服务接口
 */

public interface UserService {
    /**
     * 登陆服务
     */
    Map login(User user);

    /**
     * 根据用户名查询用户
     */
    User findUserByName(String username);

    /**
     * 根据用户名加载用户
     */
    UserDetails loadUserByUsername(String name) throws UsernameNotFoundException;

    /**
     * 注销登录服务
     */
    void logout();

    /**
     * 注册用户服务
     */
    void register(User user);

    /**
     * 根据用户名分页搜索用户
     *
     * @param userName
     * @return
     */
    List<User> searchUserByName(String userName, Integer page, Integer showCount);

    /**
     * 分页查询用户
     *
     * @param page
     * @param showCount
     * @return
     */
    List<User> findUser(Integer page, Integer showCount);

    /**
     * 查询用户数
     *
     * @return
     */
    Long getUserCount();


    /**
     * 查询用户邮箱
     *
     * @return
     */
    String findUserMail();

    /**
     * 模糊查询用户名 返回记录数
     *
     * @param userName
     * @return
     */
    Long getUserCountByName(String userName);
    /**
     * 封禁或解禁用户
     *
     * @param id
     * @param state
     */
    void updateUserState(Integer id, Integer state);
}