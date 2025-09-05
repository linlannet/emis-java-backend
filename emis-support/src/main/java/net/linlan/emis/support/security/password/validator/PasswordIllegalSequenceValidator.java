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
package net.linlan.emis.support.security.password.validator;

import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.PasswordData;
import org.passay.RuleResult;

import com.alibaba.fastjson2.JSONObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.support.security.password.PasswordValidator;
import net.linlan.emis.support.security.password.exception.InvalidPasswordException;
import net.linlan.emis.support.security.password.exception.PasswordIllegalSequenceException;

/**
 * 密码非法序列
 *
 * @author Linlan
 */
@Slf4j
@RequiredArgsConstructor
public class PasswordIllegalSequenceValidator implements PasswordValidator {
    @Override
    public void validate(String password) throws InvalidPasswordException {
        if (enabled) {
            //按字母顺序
            IllegalSequenceRule alphabeticalRule = new IllegalSequenceRule(
                EnglishSequenceData.Alphabetical);
            //数字
            IllegalSequenceRule numericalRule = new IllegalSequenceRule(
                EnglishSequenceData.Numerical);
            //键盘
            IllegalSequenceRule usQwertyRule = new IllegalSequenceRule(
                EnglishSequenceData.USQwerty);
            org.passay.PasswordValidator passwordValidator = new org.passay.PasswordValidator(
                alphabeticalRule, numericalRule, usQwertyRule);
            RuleResult validate = passwordValidator.validate(new PasswordData(password));
            if (!validate.isValid()) {
                log.error("密码非法序列异常: {}", JSONObject.toJSONString(validate.getDetails()));
                throw new PasswordIllegalSequenceException("密码非法序列异常");
            }
        }
    }

    /**
     * 启用
     */
    private final Boolean enabled;
}
