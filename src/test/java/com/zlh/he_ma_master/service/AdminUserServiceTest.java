package com.zlh.he_ma_master.service;
import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zlh.he_ma_master.entity.AdminUser;
import com.zlh.he_ma_master.entity.AdminUserToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.validation.Valid;

@SpringBootTest
public class AdminUserServiceTest {

    @Resource
    AdminUserService adminUserService;

    @Resource
    AdminUserTokenService adminUserTokenService;

    @Test
      void testAdminUserService(){
        AdminUser adminUser = new AdminUser();
        adminUser.setUserAccount("admin");
        adminUser.setUserPassword("e10adc3949ba59abbe56e057f20f883e");
        adminUser.setUserStatus(0);
        adminUser.setUserNickName("user_admin");
        adminUser.setCreateTime(new Date());
        adminUser.setUpdateTime(new Date());
        adminUser.setIsDelete(0);
        boolean save = adminUserService.save(adminUser);
        Assertions.assertTrue(save);
    }

    @Test
    void testDeleteAdminUser(){
        UpdateWrapper<AdminUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_account","admin");
        boolean remove = adminUserService.remove(wrapper);
        Assertions.assertTrue(remove);
    }

    @Test
    void testSelectAdminUser(){
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account","admin").
                eq("user_password","e10adc3949ba59abbe56e057f20f883e");
        AdminUser one = adminUserService.getOne(wrapper);
        System.out.println(one);
    }

    @Test
    void testLogin(){
        String login = adminUserService.login("admin", "e10adc3949ba59abbe56e057f20f883e");
        Assertions.assertNotNull(login);
    }

    @Test
    void testToken(){
        AdminUserToken userToken = adminUserTokenService.getById(1);
        Assertions.assertNotNull(userToken);
    }


}
