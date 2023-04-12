package com.wang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.common.ErrorCode;
import com.wang.domain.User;
import com.wang.exception.BusinessException;
import com.wang.service.UserService;
import com.wang.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wang.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 24017
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-03-29 11:21:14
 */
@SuppressWarnings({"all"})
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
//盐值
    final static String SALT = "wang";

    @Resource
    private UserMapper userMapper;

    /**
     * 注册逻辑
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @param planetCode
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,checkPassword)) {
             throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");

        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");


        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"注册密码过短");
        }
//        账户不能重复
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户重复");
        }
        //        星球编码不能重复
       wrapper = new QueryWrapper<>();
        wrapper.eq("planetCode", planetCode);
       count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号重复");
        }
        //  密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        星球编码校验
        if(planetCode.length()>5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号过长");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户包含特殊字符");
        }

//   加密

        String password = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
//插入数据
        User user = new User();
        user.setUserPassword(password);
        user.setUserAccount(userAccount);
        user.setPlanetCode(planetCode);
        boolean flag = this.save(user);
        if (!flag) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"注册失败");
        }
        return user.getId();
    }

    /**
     * 用户登入
     *
     * @param userAccount
     * @param userPassword
     * @return
     */
    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;

        }
        if (userPassword.length() < 8) {
            return null;
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
//        加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
//        进行查询
        QueryWrapper<User> wrapper = new QueryWrapper();
        wrapper.eq("userAccount", userAccount);
        wrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(wrapper);
//        用户不存在
        if (user == null) {
            log.info("user login failed,userAccount cannot match userPassword");
            return null;
        }
        //        脱敏
        User safetyUser = this.getSafetyUser(user);
//        记录用户登入态
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE,user);
        return safetyUser;

    }
    @Override
    public User getSafetyUser(User originalUser){
        if(originalUser==null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originalUser.getId());
        safetyUser.setUsername(originalUser.getUsername());
        safetyUser.setUserAccount(originalUser.getUserAccount());
        safetyUser.setAvatarUrl(originalUser.getAvatarUrl());
        safetyUser.setGender(originalUser.getGender());
        safetyUser.setPhone(originalUser.getPhone());
        safetyUser.setEmail(originalUser.getEmail());
        safetyUser.setPlanetCode(originalUser.getPlanetCode());
        safetyUser.setUserStatus(originalUser.getUserStatus());

        safetyUser.setUserRole(originalUser.getUserRole());
         safetyUser.setCreateTime(originalUser.getCreateTime());
        return safetyUser;
    }

    /**
     * 注销
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
         request.getSession().removeAttribute(USER_LOGIN_STATE);
         return 1;

    }
}




