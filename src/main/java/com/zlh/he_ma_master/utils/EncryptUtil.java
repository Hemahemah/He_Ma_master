package com.zlh.he_ma_master.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author lh
 */
public class EncryptUtil {

    /**
     * 生成32位token值 生成策略: 用户Id + 时间戳 + 6位随机数
     * @param userId 用户编号
     * @return token
     */
    public static String getToken(String userId){
        if(userId == null ){
            return null ;
        }
        String origin = userId + System.currentTimeMillis() + genRandomNum(6);
        StringBuilder sb = new StringBuilder() ;
        MessageDigest digest = null ;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null ;
        }
        //生成一组length=16的byte数组
        byte[] bs = digest.digest(origin.getBytes()) ;
        for (byte b : bs) {
            //byte转int为了不丢失符号位， 所以&0xFF
            int c = b & 0xFF;
            //如果c小于16，就说明，可以只用1位16进制来表示， 那么在前面补一个0
            if (c < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(c));
        }
        return sb.toString() ;
    }


    /**
     * 生成指定长度随机数
     * @param length 长度
     * @return int
     */
    public static int genRandomNum(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }
}
