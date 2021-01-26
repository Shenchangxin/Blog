package com.alex.spring.boot.blog.auth;

import com.alex.spring.boot.blog.domain.User;
import com.alex.spring.boot.blog.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try{
            user = this.userService.findUserByName(username);
        }catch (Exception ex){
            throw new UsernameNotFoundException("未找到该用户");
        }
        return new MyUserDetails(user);
    }
}

