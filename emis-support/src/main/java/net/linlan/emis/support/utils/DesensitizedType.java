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

import java.util.function.Function;

/**
 * 脱敏类型
 *
 * @author Linlan
 */
public enum DesensitizedType {
                              /**
                               * 姓名，第2位星号替换
                               */
                              USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),

                              /**
                               * 密码，全部字符都用*代替
                               */
                              PASSWORD(DesensitizedUtils::convertPassword),

                              /**
                               * 身份证，中间10位星号替换
                               */
                              ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\d{4})",
                                  "$1** **** ****$2")),

                              /**
                               * 手机号，中间4位星号替换
                               */
                              PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),

                              /**
                               * 电子邮箱，仅显示第一个字母和@后面的地址显示，其他星号替换
                               */
                              EMAIL(s -> s.replaceAll("(^.)[^@]*(@.*$)", "$1****$2")),

                              /**
                               * 银行卡号，保留最后4位，其他星号替换
                               */
                              BANK_CARD(s -> s.replaceAll("\\d{15}(\\d{3})",
                                  "**** **** **** **** $1")),

                              /**
                               * 车牌号码，包含普通车辆、新能源车辆
                               */
                              CAR_NUM(DesensitizedUtils::convertCarNum);

    private final Function<String, String> desensitizer;

    DesensitizedType(Function<String, String> desensitizer) {
        this.desensitizer = desensitizer;
    }

    public Function<String, String> desensitizer() {
        return desensitizer;
    }
}
