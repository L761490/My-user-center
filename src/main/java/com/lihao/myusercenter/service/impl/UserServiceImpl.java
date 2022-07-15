package com.lihao.myusercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihao.myusercenter.common.ErrorCode;
import com.lihao.myusercenter.exception.BusinessException;
import com.lihao.myusercenter.model.domain.User;
import com.lihao.myusercenter.mapper.UserMapper;
import com.lihao.myusercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lihao.myusercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 92164
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2022-07-14 08:02:55
*/
/*
去 maven-repository导入apache commons lang，它跟java.lang这个包的作用类似，
Commons Lang这一组API也是提供一些基础的、通用的操作和处理，如自动生成toString()的结果、
自动实现hashCode()和equals()方法、数组操作、枚举、日期和时间的处理等等。
 */
@Service
@Slf4j
//打上这个注解之后，就可以在当前的这个类中使用log，用log来记录日志，这样后面系统出现了问题，可以在日志中去查找，类似于监控
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    @Resource
    /*
     * @Resource(该注解属于J2EE的)，默认按照名称进行装配，名称可以通过name属性进行指定，如果没有指定name属性，
     * 当注解写在字段上时，默认取字段名进行按照名称查找，如果注解写在setter方法上默认取属性名进行装配。
     * 当找不到与名称匹配的bean时才按照类型进行装配。注意：如果name属性一旦指定，就只会按照名称进行装配
     */
    private UserMapper userMapper;
    /*
    盐值，混淆密码
     */
    private static final String SALT = "lihao";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkedPassword) {
        // 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkedPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 账户长度不小于4位
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 密码和校验密码长度不小于8位
        if(userPassword.length() < 8 || checkedPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 密码和校验密码相等
        if(!userPassword.equals(checkedPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
            return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 账户长度不小于4位
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 密码和校验密码长度不小于8位
        if(userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("user login failed,userAccount Cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 用户脱敏
        User safetyUser = getSafetyUser(user);
        // 记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        return safetyUser;
    }

    /**
     * @Author LiHao
     * @Description 用户脱敏
     * @Date 15:42 2022/7/14
     * @Param [originUser]
     * @return com.lihao.myusercenter.model.domain.User
     */
    @Override
    public User getSafetyUser(User originUser){
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    /**
     * @Author LiHao
     * @Description 用户注销
     * @Date 20:47 2022/7/14
     * @Param [request]
     * @return int
     */
    @Override
    public int userLogOut(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




