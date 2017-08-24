package com.georgeren.mytoutiao.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by georgeRen on 2017/8/24.
 */

public class StringUtil {
    public static String getStringNum(String s) {
        String regex = "[^0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.replaceAll("").trim();
    }
}
