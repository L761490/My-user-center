package com.lihao.myusercenter.exception;

import com.lihao.myusercenter.common.ErrorCode;

/**
 * @ClassName BusinessException
 * @Description 自定义异常类
 * @Author LiHao
 * @Date 2022/7/15 9:22
 * @Version 1.0
 */
public class BusinessException extends RuntimeException{

    private final int code;

    private final String description;

    public BusinessException(String message,int code, String description){
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
