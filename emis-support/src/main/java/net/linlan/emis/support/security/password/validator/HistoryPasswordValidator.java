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

import java.util.List;

import org.apache.commons.compress.utils.Lists;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.support.security.password.PasswordValidator;
import net.linlan.emis.support.security.password.exception.HistoryPasswordException;
import net.linlan.emis.support.security.password.exception.InvalidPasswordException;

/**
 * 历史密码验证器
 *
 * @author Linlan
 */
@Slf4j
@RequiredArgsConstructor
public class HistoryPasswordValidator implements PasswordValidator {

    @Override
    public void validate(String password) throws InvalidPasswordException {
        if (enabled) {
            historyPasswords.forEach(i -> {
                boolean matches = passwordEncoder.matches(password, i);
                if (matches) {
                    throw new HistoryPasswordException("密码于历史密码相同");
                }
            });
        }
    }

    /**
     * 启用
     */
    private final Boolean         enabled;
    /**
     * 历史密码
     */
    private final List<String>    historyPasswords;
    /**
     * PasswordEncoder
     */
    private final PasswordEncoder passwordEncoder;

    public HistoryPasswordValidator(Boolean enabled) {
        this.enabled = enabled;
        this.historyPasswords = Lists.newArrayList();
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public HistoryPasswordValidator(List<String> historyPasswords,
                                    PasswordEncoder passwordEncoder) {
        this.enabled = true;
        this.historyPasswords = historyPasswords;
        this.passwordEncoder = passwordEncoder;
    }
}
