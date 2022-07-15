package com.lihao.myusercenter.service;
import java.util.Date;

import com.lihao.myusercenter.mapper.UserMapper;
import com.lihao.myusercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author LiHao
 * @Description 用户服务测试
 * @Date 8:08 2022/7/14
 */
@SpringBootTest
//@SpringBootTest注解是SpringBoot自1.4.0版本开始引入的一个用于测试的注解
class UserServiceTest {
    @Resource
    /*
     @Resource(这个注解属于J2EE的),默认按照名称进行装配,名称可以通过name属性进行指定,
     如果没有指定name属性,当注解写在字段上时,默认取字段名进行按照名称查找,
     如果注解写在setter方法上默认取属性名进行装配。当找不到与名称匹配的bean时才按照
     类型进行装配。但是需要注意的是，如果name属性一旦指定,就只会按照名称进行装配。
     */
    private UserService userService;

    @Test
    public void testAddUSer(){
        User user = new User();
        user.setUsername("dogLiHao");
        user.setUserAccount("123");
        user.setAvatarUrl("https://images.zsxq.com/FtXfMM-_EZkfNgxGVNfH1UjP3Nt7?e=1661961599&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:yuN9BsAW8Gc7yFEkkNe5p-VaMXM=");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setEmail("123");
        user.setPhone("456");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void userRegister() {
        String userAccount = "lihao";
        String userPassword = "";
        String checkedPassword = "123456";
        long result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "li";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "lihao";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "li hao";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1,result);

        checkedPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "dogLiHao";
        checkedPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "lihao";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertTrue(result > 0);
    }
}