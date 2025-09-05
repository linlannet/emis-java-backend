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

import org.passay.PasswordData;
import org.passay.RepeatCharactersRule;
import org.passay.RuleResult;

import com.alibaba.fastjson2.JSONObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.support.security.password.PasswordValidator;
import net.linlan.emis.support.security.password.exception.InvalidPasswordException;
import net.linlan.emis.support.security.password.exception.PasswordContinuousSameCharException;

/**
 * 密码连续相同字符数
 *
 * @author Linlan
 */
@Slf4j
@RequiredArgsConstructor
public class PasswordContinuousSameCharValidator implements PasswordValidator {

    @Override
    public void validate(String password) throws InvalidPasswordException {
        //重复字符规则
        RepeatCharactersRule repeatCharactersRule = new RepeatCharactersRule(rule);
        org.passay.PasswordValidator passwordValidator = new org.passay.PasswordValidator(
            repeatCharactersRule);
        RuleResult validate = passwordValidator.validate(new PasswordData(password));
        if (!validate.isValid()) {
            log.error("密码存在连续相同字符数: {}", JSONObject.toJSONString(validate.getDetails()));
            throw new PasswordContinuousSameCharException("密码存在连续相同字符数");
        }
    }

    /**
     * 规则
     */
    private final Integer rule;

}
