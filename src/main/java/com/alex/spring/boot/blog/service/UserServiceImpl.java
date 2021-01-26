package com.alex.spring.boot.blog.service;


import java.util.*;
import com.alex.spring.boot.blog.config.JwtConfig;
import com.alex.spring.boot.blog.domain.Role;
import com.alex.spring.boot.blog.mapper.RoleMapper;
import com.alex.spring.boot.blog.mapper.UserMapper;
import com.alex.spring.boot.blog.util.DateUtil;
import com.alex.spring.boot.blog.util.JsonWebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.alex.spring.boot.blog.domain.User;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * User 服务.
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JsonWebTokenUtil jsonWebTokenUtil;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private JwtConfig jwtConfig;


    /**
     * 登录
     * 返回token，用户名，用户角色
     */
    @Override
    public Map login(User user) throws UsernameNotFoundException, RuntimeException {

        User dbUser = this.findUserByName(user.getName());
        //此用户不存在 或 密码错误
        if (null == dbUser || !encoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //用户已被封禁
        if (0 == dbUser.getState()) {
            throw new RuntimeException("你已被封禁");
        }

        //用户名 密码 匹配 签发token
        final UserDetails userDetails = this.loadUserByUsername(user.getName());

        final String token = jsonWebTokenUtil.generateToken(userDetails);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }

        Map<String, Object> map = new HashMap<>(3);

        map.put("token", jwtConfig.getPrefix() + token);
        map.put("name", user.getName());
        map.put("roles", roles);
        return map;
    }

    /**
     * 注销登录
     */
    @Override
    public void logout() {

    }


    /**
     * 注册
     *
     * @param userToAdd
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(User userToAdd) throws RuntimeException {

        //有效 保存用户
        final String username = userToAdd.getName();
        if (userMapper.findUserByName(username) != null) {
            throw new RuntimeException("用户名已存在");
        }

        if (userMapper.findUserByMail(userToAdd.getMail()) != null) {
            throw new RuntimeException("邮箱已使用");
        }

        List<Role> roles = new ArrayList<>(1);
        roles.add(roleService.findRoleByName("USER"));
        userToAdd.setRoles(roles);//新注册用户赋予USER权限

        final String rawPassword = userToAdd.getPassword();
        userToAdd.setPassword(encoder.encode(rawPassword));//加密密码
        userToAdd.setState(1);//正常状态
        userMapper.saveUser(userToAdd);//保存角色
        //保存该用户的所有角色
        for (Role role : roles) {
            roleMapper.saveUserRoles(userToAdd.getId(), role.getId());
        }
    }

    /**
     * 修改用户密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param code        邮箱验证码
     */
//    @Transactional(rollbackFor = Exception.class)
//    public void updateUserPassword(String oldPassword, String newPassword, String code) {
//        //校验原密码
//        String name = jsonWebTokenUtil.getUsernameFromRequest(request);
//        User user = new User();
//        user.setName(name);
//
//        user = userMapper.findUserByName(user.getName()); //
//        if (!encoder.matches(oldPassword, user.getPassword())) {
//            throw new RuntimeException("密码错误");
//        }
//
//        //校验邮箱验证码
//        if (!checkMailCode(user.getMail(), code)) {
//            throw new RuntimeException("验证码无效");
//        }
//        //更新密码
//        user.setPassword(encoder.encode(newPassword));
//        userMapper.updateUser(user);
//
//    }

//    /**
//     * 更新用户邮箱
//     *
//     * @param newMail     新邮箱
//     * @param oldMailCode 旧邮箱验证码
//     * @param newMailCode 新邮箱验证码
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public void updateUserMail(String newMail, String oldMailCode, String newMailCode) {
//
//        //获取向旧邮箱发出的验证码
//        String userName = jsonWebTokenUtil.getUsernameFromRequest(request);
//        User user = userMapper.findUserByName(userName);
//
//        //与用户输入的旧邮箱验证码进行匹配
//        if (!checkMailCode(user.getMail(), oldMailCode)) {
//            throw new RuntimeException("旧邮箱无效验证码");
//        }
//
//        //检查新邮箱是否已存在
//        if (userMapper.findUserByMail(newMail) != null) {
//            throw new RuntimeException("此邮箱已使用");
//        }
//
//
//        //校验新邮箱验证码
//        if (!checkMailCode(newMail, newMailCode)) {
//            throw new RuntimeException("新邮箱无效验证码");
//        }
//
//
//        user.setMail(newMail);
//        //更新用户邮箱信息
//        userMapper.updateUser(user);
//
//    }

//    /**
//     * 重置密码
//     *
//     * @param userName
//     * @param mailCode
//     * @param newPassword
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public void forgetPassword(String userName, String mailCode, String newPassword) {
//        User user = userMapper.findUserByName(userName);
//        if (user == null) {
//            throw new RuntimeException("用户名不存在");
//        }
//
//
//        //与用户输入的邮箱验证码进行匹配
//        if (!checkMailCode(user.getMail(), mailCode)) {
//            throw new RuntimeException("无效验证码");
//        }
//        user.setPassword(encoder.encode(newPassword));
//        //更新密码
//        userMapper.updateUser(user);
//
//    }

    /**
     * 根据用户名分页搜索用户
     *
     * @param userName
     * @return
     */
    @Override
    public List<User> searchUserByName(String userName, Integer page, Integer showCount) {
        return userMapper.searchUserByName(userName, (page - 1) * showCount, showCount);
    }

    /**
     * 分页查询用户
     *
     * @param page
     * @param showCount
     * @return
     */
    @Override
    public List<User> findUser(Integer page, Integer showCount) {
        return userMapper.findUser((page - 1) * showCount, showCount);
    }

    /**
     * 查询用户数
     *
     * @return
     */
    @Override
    public Long getUserCount() {
        return userMapper.getUserCount();
    }


    /**
     * 查询用户邮箱
     *
     * @return
     */
    @Override
    public String findUserMail() {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        return user.getMail();
    }

    /**
     * 模糊查询用户名 返回记录数
     *
     * @param userName
     * @return
     */
    @Override
    public Long getUserCountByName(String userName) {
        return userMapper.getUserCountByName(userName);
    }


    /**
     * 封禁或解禁用户
     *
     * @param id
     * @param state
     */
    public void updateUserState(Integer id, Integer state) {
        User user = new User();
        user.setId(id);
        user.setState(state);
        //更改用户状态
        userMapper.updateUser(user);
    }

    /**
     * 根据用户名查询用户
     */
    @Override
    public User findUserByName(String username){
        return userMapper.findUserByName(username);
    }


    /**
     * 根据用户名查询用户
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userMapper.findUserByName(name);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(1);
        //用于添加用户的权限。将用户权限添加到authorities
        List<Role> roles = roleMapper.findUserRoles(user.getId());
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getName(), "***********", authorities);
    }

}
