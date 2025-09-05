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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import net.linlan.emis.support.security.password.PasswordValidator;
import net.linlan.emis.support.security.password.exception.InvalidPasswordException;
import net.linlan.emis.support.security.password.exception.WeakPasswordException;

/**
 * 弱密码验证器
 *
 * @author Linlan
 */
@Slf4j
@RequiredArgsConstructor
public class WeakPasswordValidator implements PasswordValidator {
    @Override
    public void validate(String password) throws InvalidPasswordException {
        if (enabled) {
            boolean contains = dictionary.get(password).equals(Boolean.TRUE);
            if (contains) {
                throw new WeakPasswordException("密码为弱密码");
            }
        }
    }

    /**
     * 启用
     */
    private final Boolean              enabled;
    /**
     * 弱密码
     */
    private final Map<String, Boolean> dictionary;

    public WeakPasswordValidator(Boolean enabled) {
        this.enabled = enabled;
        this.dictionary = new HashMap<>(16);
    }

    public WeakPasswordValidator(List<String> list) {
        this.enabled = true;
        this.dictionary = new HashMap<>(16);
        var entriesToKeep = new HashSet<String>();
        Map<String, Boolean> newEntries = new HashMap<>(16);
        for (String dict : list) {
            entriesToKeep.add(dict);
            newEntries.put(dict, Boolean.TRUE);
        }
        synchronized (dictionary) {
            dictionary.keySet().retainAll(entriesToKeep);
            dictionary.putAll(newEntries);
        }
    }
}
