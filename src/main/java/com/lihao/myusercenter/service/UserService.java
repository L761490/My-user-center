package com.lihao.myusercenter.service;

import com.lihao.myusercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 92164
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-07-14 08:02:55
*/
public interface UserService extends IService<User> {

    /**
     * @Author LiHao
     * @Description 用户注册
     * @Date 8:44 2022/7/14
     * @Param [userAccount, userPassword, checkedPassword]
     * @return long
     */
    long userRegister(String userAccount, String userPassword, String checkedPassword);

    /**
     * @Author LiHao
     * @Description 用户登录
     * @Date 9:45 2022/7/14
     * @Param [userAccount, userPassword]
     * @return com.lihao.myusercenter.model.domain.User
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * @Author LiHao
     * @Description 用户脱敏
     * @Date 15:44 2022/7/14
     * @Param [originUser]
     * @return com.lihao.myusercenter.model.domain.User
     */
    User getSafetyUser(User originUser);

    /**
     * @Author LiHao
     * @Description 用户注销
     * @Date 20:45 2022/7/14
     * @Param [request]
     * @return int
     */
    int userLogOut(HttpServletRequest request);
}