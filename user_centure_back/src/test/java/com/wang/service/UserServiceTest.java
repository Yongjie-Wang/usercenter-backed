package com.wang.service;
import com.wang.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;

import static com.wang.constant.UserConstant.USER_LOGIN_STATE;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void addUser() {
        User user = new User();
        user.setId(null);
        user.setUsername("dogyupi");
        user.setUserAccount("200109014");
        user.setAvatarUrl("https://pic.code-nav.cn/user_avatar/1637737736167280641/aC6GyMMY-YZH]DL4(S5E$70ZEXHPE9DU.png");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("1387973923");
        user.setEmail("3033784563");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertEquals(true, result);


    }


    @Test
    void userRegister() {
        String userAccount = "aaron";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        String planetCode="37";
//        非空
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
      Assertions.assertEquals(-1,result);
////      账户长度不小于四位
//        userAccount="yu";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1,result);
////        密码不小于八位
//        userAccount="yupi";
//        userPassword = "123456";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1,result);
////        不包含特殊字符
//        userAccount="yu pi";
//        userPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1,result);
////        密码与检验密码不相同
//        checkPassword="123456789";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1,result);
////        账户不能重复
//        userAccount="dogyupi";
//        checkPassword="12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1,result);
////        校验成功
//        userAccount="yupi";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertTrue(result>0);

    }
    @Test()
    public void testSession(HttpServletRequest request){
        Object userObj= request.getSession().getAttribute(USER_LOGIN_STATE);
        User user= (User)userObj;
        System.out.println(user);

    }

}
