package org.net.Util;

import java.security.MessageDigest;
import java.util.logging.Logger;

public class MD5Utils {

  private static Logger logger = Logger.getLogger(MD5Utils.class.getName());

  public static String getMD5(String s) {
    char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    try {
      byte[] btInput = s.getBytes();
      // 获得MD5摘要算法的 MessageDigest 对象
      MessageDigest mdInst = MessageDigest.getInstance("MD5");
      // 使用指定的字节更新摘要
      mdInst.update(btInput);
      // 获得密文
      byte[] md = mdInst.digest();
      // 把密文转换成十六进制的字符串形式
      int j = md.length;
      char str[] = new char[j * 2];
      int k = 0;
      for (byte byte0 : md) {
        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
        str[k++] = hexDigits[byte0 & 0xf];
      }
      return new String(str);

    } catch (Exception e) {
      logger.warning("加密出错" + e.getMessage());
    }
    return "";
  }

  public static void main(String[] args) {
    System.out.println(getMD5("123456"));
  }

}