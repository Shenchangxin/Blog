package com.alex.spring.boot.blog.service;

import com.alex.spring.boot.blog.domain.Role;

import java.util.List;

public interface RoleService {

    Integer findAdminRoleCount(String roleName);


    /**
     * 根据角色名查询
     */
    Role findRoleByName(String roleName);

    /**
     * 查询所有角色
     */
    List<Role> findAllRole();
}