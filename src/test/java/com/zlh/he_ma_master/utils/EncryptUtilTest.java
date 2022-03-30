package com.zlh.he_ma_master.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class EncryptUtilTest {

    @Test
    public void getTokenTest(){
        for (int i = 0; i < 100; i++) {
            String token = EncryptUtil.getToken("021313131231312");
            System.out.println(token);
            Assertions.assertEquals(32,token.length());
        }
    }

}
