package org.net.Util;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
* @Author : YangFeng
* @Desc :  String 工具类
* @Date : 2024/7/26
**/
public class StringUtils {


    /**
     *  字符串为空判断
     *  1、""
     *  2、null
     *  3、"null"
     *  4、"   "空白字符
    **/
    public static boolean isNullorEmpty(String str) {
        return str == null || str.isEmpty() || str.equals("null") || str.trim().isEmpty();
    }

    /**
    *  字符串不为空判断
    **/
    public static boolean isNotNullorEmpty(String str) {
        return !isNullorEmpty(str);
    }

    /**
    *  电话校验
    **/
    public static boolean isPhone(String phone) {
        return phone.matches("^1[3-9]\\d{9}$");
    }
    /**
     * 检查字符串是否符合电子邮件地址格式。
     *
     * @param email 待检查的字符串
     * @return 如果字符串符合电子邮件地址格式则返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        Pattern EMAIL_PATTERN = Pattern.compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 检查字符串是否符合URL格式。
     *
     * @param url 待检查的字符串
     * @return 如果字符串符合URL格式则返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        Pattern URL_PATTERN = Pattern.compile(
                "^(http|https|ftp)://"
                        + "[a-zA-Z0-9]+([\\-\\.]{1}[a-zA-Z0-9]+)*\\.[a-zA-Z]{2,5}"
                        + "(:[0-9]{1,5})?"
                        + "((/?)|(/{2}[a-zA-Z0-9]+([\\-\\.]{1}[a-zA-Z0-9]+)*(/?)*)*)$");
        return url != null && URL_PATTERN.matcher(url).matches();
    }

    /**
     * 使用指定字符对字符串'左侧'进行填充，以达到最小长度。
     * @example: ( "hello", 10, '0')  ------>  "00000hello"
     * @example: ( "hello", 5, '0')   ------>  "hello"
     *
     * @param str 要填充的字符串
     * @param size 填充后期望的长度
     * @param padChar 用于填充的字符
    **/
    public static String leftPad(String str, int size, char padChar) {
        if (isNullorEmpty(str)) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        return padding(pads, padChar).concat(str);
    }

    /**
     * 使用指定字符对字符串'右侧'进行填充，以达到最小长度。
     * @example: ( "hello", 10, '0')  ------>  "hello00000"
     * @example: ( "hello", 5, '0')   ------>  "hello"
     *
     * @param str 要填充的字符串
     * @param size 填充后期望的长度
     * @param padChar 用于填充的字符
    **/
    public static String rightPad(String str, int size, char padChar) {
        if (isNullorEmpty(str)) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        return str.concat(padding(pads, padChar));
    }

    /**
     * 根据指定次数重复一个字符，生成填充字符串。
     * @example: ( 5,'0')  ------>   "00000"
     * @example: ( -5,'0')  ------>  "IndexOutOfBoundsException"
     *
     * @param repeat 重复次数
     * @param padChar 需要重复的字符
    **/
    private static String padding(int repeat, char padChar)
            throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("不能使用负数进行填充: " + repeat);
        }
        final char[] buf = new char[repeat];
        Arrays.fill(buf, padChar);
        return new String(buf);
    }

    /**
    *  字符串 指定符号转为驼峰命名
     *  @example: ( "hello_world" , '_' )  ------> "helloWorld"
     *  @example: ( "string@util" , '@' )  ------> "stringUtil"
     *
     *  @param str 待转换的字符串。
     *  @param replaceChar 需要被替换的字符。
    **/
    public static String toHumplCase(String str, char replaceChar) {
        if (isNullorEmpty(str)) {
            return "";
        }
        //通过char字符数组操作
        char[] strCharArray = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean nextCharUpperFlag = false;
        for (char c : strCharArray) {
            if (c == replaceChar) {
                nextCharUpperFlag = true;
            } else {
                sb.append(nextCharUpperFlag ? Character.toUpperCase(c) : c);
                nextCharUpperFlag = false;
            }
        }

        return sb.toString();
    }
    /**
     * 驼峰 转成下划线、等指定符号
     * @example: ("toLowString", '_' ,false)  --------> "to_upper_string"
     * @example: ("toUpperString", '_' ,true) --------> "TO_UPPER_STRING"
     *
     * @param str  待转换的字符串
     * @param addChar  添加的字符
     * @param isUpper 结果是否都大写
     */
    public static String  humpToUnderline(String str, String addChar,boolean isUpper) {
        if (isNullorEmpty(str)) {
            return "";
        }
        if (!str.contains(addChar)) {
            return str;
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(addChar);
            }
            if (isUpper) {
                sb.append(Character.toUpperCase(c)); // 统一都转大写
            } else {
                sb.append(Character.toLowerCase(c)); // 统一都转小写
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        System.out.println(toHumplCase("helloMyworld__nameis_yf", '_'));
        System.out.println(leftPad("10", 10,'@'));
    }

}
