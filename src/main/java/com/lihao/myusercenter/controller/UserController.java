package com.lihao.myusercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihao.myusercenter.common.BaseResponse;
import com.lihao.myusercenter.common.ErrorCode;
import com.lihao.myusercenter.common.ResultUtils;
import com.lihao.myusercenter.exception.BusinessException;
import com.lihao.myusercenter.model.domain.User;
import com.lihao.myusercenter.model.domain.request.UserLoginRequest;
import com.lihao.myusercenter.model.domain.request.UserRegisterRequest;
import com.lihao.myusercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.lihao.myusercenter.constant.UserConstant.ADMIN_ROLE;
import static com.lihao.myusercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @ClassName UserController
 * @Description 用户接口
 * @Author LiHao
 * @Date 2022/7/14 10:24
 * @Version 1.0
 */
@RestController
/*
 * controller 层倾向于对请求参数本身的校验，不涉及业务逻辑本身(越少越好)
 * service 层是对业务逻辑的校验(有可能被 controller 之外的类调用)
 * 使用于编写restful风格的api，返回值默认为json类型
 */
@RequestMapping("/user")
/*
 * 注解 @RequestMapping 可以用在类定义处和方法定义处。
 * 类定义处：规定初步的请求映射，相对于web应用的根目录；
 * 方法定义处：进一步细分请求映射，相对于类定义处的URL。如果类定义处没有使用该注解，则方法标记的URL相对于根目录而言；
 */
public class UserController {

    @Resource
    private UserService userService;

    /*
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> UserRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkedPassword = userRegisterRequest.getCheckedPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkedPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkedPassword);
        return ResultUtils.success(result);
    }

    /*
     * 用户登录
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /*
     * 用户注销
     */
    @PostMapping("/logOut")
    public BaseResponse<Integer> userLogOut(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogOut(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /*
     * 以下两个功能只能管理员使用，所以需要加入鉴权判断
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        // 鉴权操作，判断是否是管理员
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        // 鉴权操作，判断是否是管理员
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /*
     * 判断是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request){
        // 仅管理员可操作
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User)userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
