package com.lihao.myusercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserLoginRequest
 * @Description TODO
 * @Author LiHao
 * @Date 2022/7/14 11:46
 * @Version 1.0
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 602108360210245181L;

    private String userAccount;
    private String userPassword;
}
