package com.cmeplaza.basemodule.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 正则校验工具类
 */
public class RegularUtils {
    public static final String REGEX_INTERNET_URL = "[a-zA-z]+://[^\\s]*";
    private static final String REGEX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    //    private static final String REGEX_MOBILE_SIMPLE = "^(13[0-9]|14[5|7]|15[0-9]|17[0-9]|18[0-9])\\d{8}$";
    private static final String REGEX_MOBILE_SIMPLE = "^1[34578]\\d{9}$";//^1[34578]\d{9}$   ^(1[0-9]{2})\d{8}$
    private static final String REGEX_IDCARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    private static final String REGEX_IDCARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
    private static final String REGEX_CHZ = "[\\u4e00-\\u9fa5]";
    private static final String REGEX_USERNAME = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]\\w{5,18}$";

    private RegularUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * If u want more please visit http://toutiao.com/i6231678548520731137/
     */

    /**
     * 验证手机号（简单）
     *
     * @param string 待验证文本
     * @return true: 匹配<br>false: 不匹配
     */
    public static boolean isMobileSimple(String string) {
        return isMatch(REGEX_MOBILE_SIMPLE, string);
    }

    /**
     * string是否匹配regex
     *
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return true: 匹配<br>false: 不匹配
     */
    public static boolean isMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }

    /**
     * 验证账号（简单）
     *
     * @param account 待验证文本
     * @return true: 匹配<br>false: 不匹配
     */
    public static boolean isAccountSimple(String account) {
        return !TextUtils.isEmpty(account);
    }

    /**
     * 验证验证码（简单）
     */
    public static boolean isVerifyCode(String verifyCode) {
        return !TextUtils.isEmpty(verifyCode);
    }

    public static boolean isPasswordMatch(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        }
        if (pwd.length() < 4 || pwd.length() > 14) {
            return false;
        }
        return true;
    }

    /**
     * 验证身份证号码15位
     *
     * @param string 待验证文本
     * @return true: 匹配<br>false: 不匹配
     */
    public static boolean isIDCard15(String string) {
        return isMatch(REGEX_IDCARD15, string);
    }

    /**
     * 验证身份证号码18位
     *
     * @param string 待验证文本
     * @return true: 匹配<br>false: 不匹配
     */
    public static boolean isIDCard18(String string) {
        return isMatch(REGEX_IDCARD18, string);
    }

    /**
     * 验证邮箱
     *
     * @param string 待验证文本
     * @return true: 匹配<br>false: 不匹配
     */
    public static boolean isEmail(String string) {
        return isMatch(REGEX_EMAIL, string);
    }

    /**
     * 验证汉字
     *
     * @param string 待验证文本
     * @return true: 匹配<br>false: 不匹配
     */
    public static boolean isChz(String string) {
        return isMatch(REGEX_CHZ, string);
    }

    /**
     * 验证用户名
     * <p>取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位</p>
     *
     * @param string 待验证文本
     * @return true: 匹配<br>false: 不匹配
     */
    public static boolean isUsername(String string) {
        return isMatch(REGEX_USERNAME, string);
    }

    public static boolean isUrl(String url) {
        return isMatch(REGEX_INTERNET_URL, url);
    }
}
