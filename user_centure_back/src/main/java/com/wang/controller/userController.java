package com.wang.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wang.common.BaseResponse;
import com.wang.common.ErrorCode;
import com.wang.common.ResultUtils;
import com.wang.domain.User;
import com.wang.domain.request.UserLoginRequest;
import com.wang.domain.request.UserRegisterRequest;
import com.wang.exception.BusinessException;
import com.wang.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wang.constant.UserConstant.ADMIN_ROLE;
import static com.wang.constant.UserConstant.USER_LOGIN_STATE;

/**
 *
 */
@Api(tags = "用户接口")
@Slf4j
@RestController
@RequestMapping(value = "/user")
//@CrossOrigin(origins = {"http://124.221.242.250"},allowCredentials = "true")
public class userController {
    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    @ApiOperation("用户注册接口")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
          throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登入接口
     */
    @PostMapping("/login")
    @ApiOperation("用户登入接口")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"登入参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        User user = userService.doLogin(userAccount, userPassword, request);
        if(user ==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "登录失败，请重试！");
        }
        return ResultUtils.success(user);

    }

    /**
     * 用户注销接口
     */
    @PostMapping("/logout")
    @ApiOperation("用户注销接口")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求为空");
        }
        int i = userService.userLogout(request);
        return ResultUtils.success(i);

    }

    /**
     * 获取当前用户
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN,"查询用户为空");
        }
        Long id = currentUser.getId();
        User user = userService.getById(id);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 根据用户名查询
     */
    @ApiOperation("用户名查询")
    @GetMapping("/search")
    public BaseResponse<List<User>>  search(String username, HttpServletRequest request) {
//        if(StringUtils.isBlank(username))
//            return ResultUtils.error(ErrorCode.NULL_ERROR,"请求参数为空");
//        鉴权
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH,"鉴权失败");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            wrapper.like("username", username);
        }
        List<User> userList = userService.list(wrapper);
        List<User> users = userList.stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());

        return ResultUtils.success(users);
    }

    /**
     * 根据id删除
     */
    @ApiOperation("删除用户接口")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteById(@RequestBody long id, HttpServletRequest request) {

        //        鉴权
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH,"鉴权失败");
        }


        if (id <= 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"id传入错误");
        }
        boolean flag = userService.removeById(id);
        return ResultUtils.success(flag);
    }


    //    鉴权
    private Boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        log.info("user_role" + user.getUserRole());
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }


}
