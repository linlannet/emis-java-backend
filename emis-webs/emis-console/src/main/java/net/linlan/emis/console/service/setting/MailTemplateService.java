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
package net.linlan.emis.console.service.setting;

import java.util.List;

import net.linlan.emis.console.pojo.result.setting.EmailTemplateListResult;
import net.linlan.emis.console.pojo.result.setting.EmailTemplateResult;
import net.linlan.emis.console.pojo.save.setting.EmailCustomTemplateSaveParam;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.entity.TemplateInfo;

/**
 * <p>
 * 邮件模板 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-13
 */
public interface MailTemplateService extends SettingService {
    /**
     * 根据邮件模板类型获取配置
     *
     * @param type {@link MailType}
     * @return SettingMailTemplateEntity
     */
    TemplateInfo getEmailTemplateByType(MailType type);

    /**
     * 添加邮件模板
     *
     * @param type  {@link MailType}
     * @param param {@link EmailCustomTemplateSaveParam}
     * @return SettingMailTemplateEntity
     */
    TemplateInfo saveCustomEmailTemplate(MailType type, EmailCustomTemplateSaveParam param);

    /**
     * 邮件模板详情
     *
     * @param templateType {@link MailType}
     * @return {@link EmailTemplateResult}
     */
    EmailTemplateResult getEmailTemplate(MailType templateType);

    /**
     * 获取邮件模板列表
     *
     * @return {@link List}
     */
    List<EmailTemplateListResult> getEmailTemplateList();

    /**
     * 禁用自定义模块
     *
     * @param type {@link MailType}
     */
    void disableCustomEmailTemplate(MailType type);
}
