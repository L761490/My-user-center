package com.lihao.myusercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserRegisterRequest
 * @Description 用户注册请求体
 * @Author LiHao
 * @Date 2022/7/14 11:35
 * @Version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 8914224831839285398L;

    private String userAccount;
    private String userPassword;
    private String checkedPassword;
}
