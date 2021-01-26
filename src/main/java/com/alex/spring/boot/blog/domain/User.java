package com.alex.spring.boot.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.util.List;

/**
 * 用户
 */
@Data
@ToString
@ApiModel("用户")
public class User implements Serializable {

    /**
     * user(36) => 1436499(10)
     */
    private static final long serialVersionUID = 1436499L;


    @ApiModelProperty(value = "用户id", dataType = "Integer",hidden = true)
    private Integer id;

    @ApiModelProperty(value = "用户名", dataType = "String")
    private String name;

    @ApiModelProperty(value = "密码", dataType = "String")
    private String password;

    @ApiModelProperty(value = "邮箱", dataType = "String")
    private String mail;

    @ApiModelProperty(value = "用户状态", dataType = "Integer",hidden = true)
    private Integer state;

    @ApiModelProperty(value = "打赏码路径", dataType = "String",hidden = true)
    private String reward;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private List<Role> roles;//角色列表



}
