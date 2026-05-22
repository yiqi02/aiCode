package com.yy.aicode.dto;

/*
用户注册请求
 */

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    //序列化请求 保证用户传入的数据可以写到硬盘
    private static final long serialVersionUID = 1L;

    //接收用户参数

    /*
     *账号
     */
    private String userAccount;

    /*
    *密码
     */
    private String userPassword;

    /*
     *确认密码
     */
    private String checkPassword;
}
