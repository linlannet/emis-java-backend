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

import net.linlan.commons.core.StringUtils;

/**
 * 数据脱敏
 */
public class DesensitizedUtils {
    /**
     * 姓名脱敏
     * @param name 姓名
     * @return 脱敏后的姓名
     */
    public static String convertName(String name) {
        if (name == null) {
            return null;
        }
        name = name.trim();
        if (name.length() > 1 && name.length() < 4) {
            return "*" + name.substring(1);
        } else if (name.length() > 3) {
            return "**" + name.substring(2);
        }
        return name;
    }

    /**
     * 生日脱敏
     * @param birthday 生日
     * @return 脱敏后的姓名
     */
    public static String convertBirthday(String birthday) {
        if (birthday == null) {
            return null;
        }
        birthday = birthday.trim();
        if (birthday.length() > 1 && birthday.length() <= 8) {
            return birthday.substring(0, 1) + "*****" + birthday.substring(birthday.length() - 2);
        } else if (birthday.length() > 8) {
            return birthday.substring(0, 1) + "*****" + birthday.substring(birthday.length() - 2);
        }
        return birthday;
    }

    /**
     * 手机号脱敏
     * @param phone 手机号码
     * @return 脱敏后的号码
     */
    public static String convertMobilePhone(String phone) {
        if (phone == null) {
            return null;
        }
        phone = phone.trim();
        if (phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
        }
        if (phone.length() < 11 && phone.length() > 4) {
            return phone.substring(0, 2) + "***" + phone.substring(phone.length() - 2);
        }
        return phone;
    }

    /**
     * 身份证号码脱敏
     * @param idCard 身份证号码
     * @return 脱敏之后的身份证号码
     */
    public static String convertIdCard(String idCard) {
        if (idCard == null) {
            return null;
        }
        idCard = idCard.trim();
        if (idCard.length() == 18) {
            return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4);
        } else if (idCard.length() == 15) {
            return idCard.substring(0, 6) + "******" + idCard.substring(idCard.length() - 3);
        }
        return idCard;
    }

    /**
     * 身份证号码脱敏,只保留前六位
     * @param idCard 身份证号码
     * @return 脱敏之后的身份证号码
     */
    public static String convertIdCardEnd(String idCard) {
        if (idCard == null) {
            return null;
        }
        idCard = idCard.trim();
        if (idCard.length() == 18) {
            return idCard.substring(0, 6) + "************";
        } else if (idCard.length() == 15) {
            return idCard.substring(0, 6) + "*********";
        }
        return idCard;
    }

    /**
     * 密码的全部字符都用*代替，比如：******
     *
     * @param password 密码
     * @return 脱敏后的密码
     */
    public static String convertPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.repeat('*', password.length());
    }

    /**
     * 车牌中间用*代替，如果是错误的车牌，不处理
     *
     * @param carNum 完整的车牌号
     * @return 脱敏后的车牌
     */
    public static String convertCarNum(String carNum) {
        if (StringUtils.isBlank(carNum)) {
            return StringUtils.EMPTY;
        }
        // 普通车牌
        if (carNum.length() == 7) {
            carNum = StringUtils.hide(carNum, 3, 6);
        } else if (carNum.length() == 8) {
            // 新能源车牌
            carNum = StringUtils.hide(carNum, 3, 7);
        }
        return carNum;
    }

    /***
     * 手机号码前三后四脱敏
     *
     * @param mobile  {@link String}
     * @return  {@link String}
     */
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * [电子邮箱] 邮箱前缀隐藏，用星号代替，@及后面的地址显示
     *
     * @param email {@link String}
     * @return {@link String}
     */
    public static String emailEncrypt(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        return email.replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2");
    }

    /**
     * 身份证前三后四脱敏
     * @param id {@link String}
     * @return {@link String}
     */
    public static String idEncrypt(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }

    /**
     * 护照前2后3位脱敏，护照一般为8或9位
     * @param id {@link String}
     * @return {@link String}
     */
    public static String idPassport(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.substring(0, 2) + new String(new char[id.length() - 5]).replace("\0", "*")
               + id.substring(id.length() - 3);
    }

}
