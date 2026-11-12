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
package net.linlan.emis.console.service.setting.impl;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.linlan.emis.console.pojo.result.setting.SmsTemplateListResult;
import net.linlan.emis.console.service.setting.SmsTemplateService;
import net.linlan.emis.core.constant.Language;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.uext.constant.SmsType;

/**
 * <p>
 * 短信模版模板 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-13
 */
@Service
public class SmsTemplateServiceImpl extends SettingServiceImpl implements SmsTemplateService {

    /**
     * 获取短信模版列表
     *
     * @param language {@link Language}
     * @return {@link SmsTemplateListResult}
     */
    @Override
    public List<SmsTemplateListResult> getSmsTemplateList(Language language) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("sms/template/sms-template",
            new Locale(language.getLocale()));
        List<SmsTemplateListResult> results = Lists.newArrayList();
        SmsType[] values = SmsType.values();

        for (SmsType type : values) {
            SmsTemplateListResult result = new SmsTemplateListResult();
            String content = resourceBundle.getString(type.getKey());
            result.setContent(content);
            result.setType(type);
            result.setName(type.getValue());
            result.setLanguage(Language.getType(resourceBundle.getLocale().getLanguage()));
            results.add(result);
        }
        return results;
    }

    public SmsTemplateServiceImpl(EmisSettingService settingsRepository) {
        super(settingsRepository);
    }

}
