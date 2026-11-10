/*
 * emis-support - support支撑模块
 * Copyright © 2020-2025 Linlan (open@linlan.net)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.linlan.emis.support.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * Md5加密类
 * @author Linlan
 */
@Slf4j
public class Md5Utils {

    /**
     * 16位加密
     *
     * @param plainText plainText
     * @return String
     */
    public static String md516(String plainText) {
        return md516(plainText, StandardCharsets.UTF_8.name());
    }

    /**
     * 16位加密
     *
     * @param plainText 文本
     * @param charSet   字符集
     * @return md5 16长度的String
     */
    public static String md516(String plainText, String charSet) {
        String result = null;
        try {
            byte[] ptBytes = plainText.getBytes(charSet);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(ptBytes);
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte value : b) {
                i = value;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            // result = buf.toString(); //md5 32bit
            // result = buf.toString().substring(8, 24))); //md5 16bit
            result = buf.toString().substring(8, 24);
        } catch (Exception e) {
            log.error("{}", (Object) e.getStackTrace());
        }
        return result;
    }

    /**
     * 32位加密，默认为UTF-8
     *
     * @param plainText plainText
     * @return md5 32长度的String
     */
    public static String md532(String plainText) {
        return md532(plainText, StandardCharsets.UTF_8.name());
    }

    /**
     * 32位加密
     *
     * @param plainText 文本
     * @param charSet   字符集
     * @return md5 32长度的String
     */
    public static String md532(String plainText, String charSet) {
        String result = null;
        try {
            byte[] ptBytes = plainText.getBytes(charSet);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(ptBytes);
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte value : b) {
                i = value;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            // result = buf.toString(); //md5 32bit
            // result = buf.toString().substring(8, 24))); //md5 16bit
            result = buf.toString();
        } catch (Exception e) {
            log.error("{}", (Object) e.getStackTrace());
        }
        return result;
    }

    /**
     * byte数组 转 16进制 字符串
     *
     * @param b b
     * @return String
     */
    public static String byteToHexString(byte[] b) {
        StringBuilder result = new StringBuilder();
        for (byte value : b) {
            String hex = Integer.toHexString(value & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }

    public static String getMd5Code(String strObj) {
        String resultString;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToHexString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
        return resultString;
    }

    public static String encode(String text) {

        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0").append(hex);
                } else {
                    sb.append(hex);
                }
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            log.error("{}", (Object) e.getStackTrace());
        }

        return "";
    }

    /**
     * 默认分隔符
     */
    public static final String SPLIT_DEFAULT = ",";

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 16进制 转 byte数组
     *
     * @param hexString hexString
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * 统计字符串字符数（非连续）
     *
     * @param string {@link  String}
     * @return {@link  Map}
     */
    public static Map<Character, Long> statisticsCharCount(String string) {
        return Stream.of(string).flatMap((Function<String, Stream<Character>>) s -> {
            final char[] chars = s.toCharArray();
            final ArrayList<Character> list = new ArrayList<>();
            for (char aChar : chars) {
                list.add(aChar);
            }
            return list.stream();
        }).collect(Collectors.groupingBy(Character::charValue, Collectors.counting()));
    }

}
