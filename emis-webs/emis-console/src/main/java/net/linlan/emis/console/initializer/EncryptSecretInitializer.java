/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.initializer;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.support.manage.init.AbstractSystemInitializer;
import net.linlan.emis.support.manage.init.InitializationException;
import net.linlan.emis.support.utils.AesUtils;
import static net.linlan.emis.core.constant.SettingConstants.AES_SECRET;

/**
 * EncryptSecretInitializer
 *
 * @author Linlan
 * Created on 2024/04/04 21:24
 */
@Component
public class EncryptSecretInitializer extends AbstractSystemInitializer {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() throws InitializationException {
        EmisSettingVo optional = settingService.getByKey(AES_SECRET);
        if (Objects.isNull(optional)) {
            EmisSettingVo setting = new EmisSettingVo();
            setting.setName(AES_SECRET);
            setting.setContent(AesUtils.generateKey());
            setting.setDescription(
                "This aes secret is automatically created during system initialization.");
            settingService.saveSetting(setting);
        }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    /**
     * EmisSettingService
     */
    private final EmisSettingService settingService;

    /**
     *
     * @param settingService {@link EmisSettingService}
     */
    public EncryptSecretInitializer(EmisSettingService settingService) {
        this.settingService = settingService;
    }
}
