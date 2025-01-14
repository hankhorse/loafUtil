package org.net.Util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author
 * @version 1.0 进制转换工具类
 */
public final class ConvertUtil {

    /**
     * 16进制数字字符集
     */
    private static String hexString = "0123456789ABCDEF";

    private static int seqSeed = 0;

    protected static final String HEX_DIGITS[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 填充字符啊
     */
    public static final char FILL_ZERO_CHAR = '0';

    /**
     * 构造器
     */
    private ConvertUtil() {
    }

    /**
     * 单字节int类型的整数10进制转换16进制
     *
     * @param k 10进制整数
     * @return k/2后转化成的2位16进制字符串
     */
    public static String halfIntTo16byte(int k) {
        if (k % 2 != 0)
            return "00";
        String hex = Integer.toHexString(k / 2).toUpperCase();
        while (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;

    }

    /**
     * int类型的整数10进制转换16进制
     *
     * @param k 10进制整数
     * @return k/2后转化成的4位16进制字符串
     */
    public static String halfIntTo16(int k) {
        if (k % 2 != 0)
            return "0000";
        String hex = Integer.toHexString(k / 2).toUpperCase();
        while (hex.length() < 4) {
            hex = "0" + hex;
        }
        return hex;

    }

    /**
     * int类型的整数16进制转换10进制
     *
     * @param str 16进制字符串
     * @return 10进制整数
     */
    public static int int16To10(String str) {
        return Integer.valueOf(str, 16);
    }

    /**
     * int类型的整数16进制转换10进制
     *
     * @param str 16进制字符串
     * @return 10进制整数 *2
     */
    public static int doubleInt16To10(String str) {
        return Integer.valueOf(str, 16) * 2;
    }

    /**
     * long类型的整数10进制转换16进制
     *
     * @param k 10进制整数
     * @return 8位16进制字符串
     */
    public static String long10To16(long k) {
        String hex = Long.toHexString(k).toUpperCase();
        while (hex.length() < 8) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 把16进制字符串转换成字节数组
     *
     * @param hex 16进制字符串
     * @return 对应的字节数组
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        String hexNew = hex.toUpperCase();
        char[] achar = hexNew.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * MD5校验 拍照专用 （待定）
     *
     * @param c
     * @return
     */
    @SuppressWarnings("unused")
    private static String checkMD5(String mD5) {
        return "";
    }

    /**
     * 符合C的MD5算法 "1234567890ABCDEF" C.MD5 --> AB3825D5AEAEC5925B05D44BEB7DDC7D
     * java.MD5 --> 3A6BFF0799C7389F522F3847C33A468F
     */
    public static String mD5Encode(String origin) {
        String resultString = null;

        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hexStringToByte(origin));
            byte b[] = md.digest();
            resultString = bytesToHexString(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static String int10To16(int k) {
        String hex = Integer.toHexString(k).toUpperCase();
        while (hex.length() < 4)
            hex = "0" + hex;

        return hex;
    }

    /**
     * unicode编码
     *
     * @param gbString 编码前字符串
     * @return String 编码后字符串
     */
    public static String unicodeEncode(String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex])
                    .toUpperCase();
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + hexB;
        }
        return unicodeBytes;
    }

    /**
     * unicode解码
     *
     * @param dataStr 解码前字符串
     * @return String 解码后字符串
     */
    public static String unicodeDecode(String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuilder buffer = new StringBuilder();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    /**
     * unicode还原如：0061 toUnicode后为\u0061
     *
     * @param str unicode编码字符串，无标识\U
     * @return String 加上\U后的字符串
     */
    public static String toUnicode(String str) {
        String newStr = "";
        int start = 0;
        int len = str.length();
        for (int i = 0; i < len / 4; i++) {
            newStr += "\\u" + str.substring(start, start + 4);
            start += 4;
        }
        return newStr;
    }

    public static final synchronized int newSeq() {
        if (++seqSeed > 999) {
            seqSeed = 0;
        }
        return seqSeed;
    }

    public static String uuid() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }

    /**
     * wang.qj add 2013-04-25
     * 为808协议位置附加信息新增
     *
     * @param k
     * @return
     */
    public static String short10To16(int k) {
        String hex = Integer.toHexString(k).toUpperCase();
        while (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 十六进制转字符串 <br>
     * Description:十六进制转字符串 <br>
     * Author:曲锐(qur) <br>
     * Date:2013-3-16
     *
     * @param hex
     * @return String
     */
    public static String hexStr2Str(String hex) {
        byte[] baKeyword = new byte[hex.length() / 2];
        String result = "";
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        hex.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            result = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }

    /**
     * byte转16进制 <br>
     * Description:byte转16进制 <br>
     *
     * @param b
     * @return String
     */
    public static final String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        return new StringBuilder(HEX_DIGITS[n / 16]).append(
                HEX_DIGITS[n % 16]).toString();
    }

    /**
     * Char转Byte <br>
     * Description:Char转Byte <br>
     * Date:2013-3-14
     *
     * @param c
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) hexString.indexOf(c);
    }

    /**
     * 十六进制字符串转成byte
     *
     * @param hexString
     * @return byte
     */
    public static byte hexStringtToByte(String hexString) {
        byte result = '0';
        if (hexString == null || hexString.equals("")) {
            return 0;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toUpperCase().toCharArray();
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            result = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }

        return result;
    }


    public static String word(String hex) {
        byte[] buf = buildMsgByteArray(hex);
        int cursor = 0;
        int i = 0;
        i = buf[cursor++] & 0xff;
        i = (i << 8) | (buf[cursor++] & 0xff);
        int deviceInt = (i < 0) ? 32768 * 2 + i : i;

        String deviceStr = String.valueOf(deviceInt);

        char[] ary1 = deviceStr.toCharArray();

        char[] ary2 = {'0', '0', '0', '0'};

        System.arraycopy(ary1, 0, ary2, ary2.length - ary1.length, ary1.length);

        String result = new String(ary2);

        return result;
    }

    /**
     * 将报文按照每两位存入byte数组
     *
     * @param msg
     * @return byte[]
     */
    public static byte[] buildMsgByteArray(String msg) {
        int msglength = msg.length() / 2;
        // 将msg字符串每两位存入byte[]中
        byte[] results = new byte[msglength];
        String[] strarray = new String[msglength];
        for (int i = 0; i < msglength; i++) {
            if (i == 0) {
                strarray[i] = msg.substring(i, i + 2);
            } else {
                strarray[i] = msg.substring(i * 2, i * 2 + 2);
            }
            results[i] = hexStringtToByte(strarray[i]);
        }
        return results;
    }

    /**
     * 返回CRC16校验和字符串（16进制，无空格）
     *
     * @param msg
     * @return String
     */
    public static String getCrc16Str(byte[] msg) {
        String result = "";
        result = Integer.toHexString(caculateCRC16(msg)).toUpperCase();
        if (result.length() > 4) {
            result = result.substring(result.length() - 4, result.length());
        } else if (result.length() < 4) {
            result = "0000".substring(result.length(), 4) + result;
        }

        return result;
    }

    /**
     * CRC16校验和代码
     *
     * @param msg
     * @return short
     */
    public static short caculateCRC16(byte[] msg) {
        short crc = (short) 0xFFFF;
        int i, j;
        boolean c15, bit;
        for (i = 0; i < msg.length; i++) {
            for (j = 0; j < 8; j++) {
                c15 = ((crc >> 15 & 1) == 1);
                bit = ((msg[i] >> (7 - j) & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= 0x1021;
                }
            }
        }
        return crc;
    }

    /**
     * BCC校验码生成，非通用
     */
    public static byte calBcc(byte[] bs) {
        byte calCheck = 0;
        for (int i = 2; i < bs.length; i++) {
            calCheck ^= (bs[i] & 0xff);
        }
        return calCheck;
    }

    /**
     * BCC校验码生成, 通用型
     */
    public static byte calBcc(byte[] bs, int startIndex, int endIndex) {
        byte calCheck = 0;

        if (bs.length <= endIndex || startIndex > endIndex) {
            return 0;
        }

        for (int i = startIndex; i <= endIndex; i++) {
            calCheck ^= (bs[i] & 0xff);
        }

        return calCheck;
    }

    /**
     * 字符串转换成十六进制字符串
     */
    public static String str2HexStr(String str) {
        char[] chars = hexString.toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }

    public static String binaryString2hexString(String bString) {
        if ((bString == null) || (bString.equals("")) || (bString.length() % 8 != 0))
            return null;
        StringBuilder tmp = new StringBuilder();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += (Integer.parseInt(bString.substring(i + j, i + j + 1)) << 4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }


    private static byte toByte(char c) {
        byte b = (byte) hexString.indexOf(c);
        return b;
    }

    public static String date2hexDate(Date date) {
        String ret = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        ret = df.format(date);
        ret = ret.substring(2);
        return ret;
    }

    public static byte[] hexStringToByteArray(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        String hexNew = hex.toUpperCase();
        char[] achar = hexNew.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (charToByte(achar[pos]) << 4 | charToByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * 上行报文bcc验证，最后两位
     *
     * @param messageHex
     * @return
     */
    public static boolean bccVerifyMessage(String messageHex) {
        boolean ret = false;
        byte[] verifyMessageByte = ConvertUtil.hexStringToByteArray(messageHex.substring(0, messageHex.length() - 2));
        byte bcc = ConvertUtil.calBcc(verifyMessageByte);
        byte srcBcc = ConvertUtil.hexStringtToByte(messageHex.substring(messageHex.length() - 2));
        if (bcc == srcBcc) {
            ret = true;
        }
        return ret;
    }

    public static float floatRound(float value) {
        return Math.round(value * 10) / 10f;
    }
}
