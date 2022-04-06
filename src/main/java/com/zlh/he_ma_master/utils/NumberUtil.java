package com.zlh.he_ma_master.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lh
 */
public class NumberUtil {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0-8])|(18[0-9]))\\d{8}$");

    public static boolean isPhone(String loginName){
        Matcher matcher = PHONE_PATTERN.matcher(loginName);
        return matcher.matches();
    }
}
