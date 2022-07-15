package com.lihao.myusercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName BaseResponse
 * @Description 通用返回类
 * @Author LiHao
 * @Date 2022/7/15 8:25
 * @Version 1.0
 */
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -8467496640658877304L;

    /*
     * 状态码
     */
    private int code;

    /*
     * 返回数据对象
     */
    private T Data;

    /*
     * 状态码信息
     */
    private String message;
    /*
     * 状态码描述(详情)
     */
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        Data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code,data,message,"");
    }

    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(), errorCode.getDescription());
    }



}
