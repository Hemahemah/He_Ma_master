package com.zlh.he_ma_master.utils;

import com.zlh.he_ma_master.entity.MallUser;

/**
 * 存储用户信息
 * @author lh
 */
public class ThreadUser {

    private static final ThreadLocal<MallUser> USER_THREAD = new ThreadLocal<>();

    /**
     * 保存用户
     */
    public static void saveUser(MallUser mallUser){
        USER_THREAD.set(mallUser);
    }

    public static MallUser getUser(){
        return USER_THREAD.get();
    }

    /**
     * 移除用户
     */
    public static void removeUser(){
        USER_THREAD.remove();
    }
}
