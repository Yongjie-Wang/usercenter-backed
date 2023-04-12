package com.wang.service;

import com.wang.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 24017
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-03-29 11:21:14
*/
public interface UserService extends IService<User> {

    //    用户登入态键

    /**
     * 注册逻辑
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @param planetCode
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);
    /**
     * 用户登入·
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User originalUser);

    /**
     * 用户注销
     */
    int userLogout(HttpServletRequest request);
}
