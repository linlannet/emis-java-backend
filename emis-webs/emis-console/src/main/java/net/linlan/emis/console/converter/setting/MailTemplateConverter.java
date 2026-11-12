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
package net.linlan.emis.console.converter.setting;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.linlan.emis.console.pojo.result.setting.EmailTemplateListResult;
import net.linlan.emis.console.pojo.result.setting.EmailTemplateResult;
import net.linlan.emis.console.pojo.save.setting.EmailCustomTemplateSaveParam;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.entity.TemplateInfo;
import static org.springframework.web.util.HtmlUtils.htmlUnescape;

import static net.linlan.emis.manage.mail.MailUtils.readEmailContent;

/**
 * 消息服务数据映射
 *
 * @author Linlan
 * Created on  2020/8/17 23:12
 */
@Mapper(componentModel = "spring")
public interface MailTemplateConverter {
    /**
     * 实体转为电子邮件模板配置返回
     *
     * @param template {@link TemplateInfo}
     * @return {@link EmailTemplateResult}
     */
    @Mapping(target = "custom", expression = "java(java.lang.Boolean.TRUE)")
    @Mapping(target = "desc", expression = "java(net.linlan.emis.uext.constant.MailType.getType(template.getType()).getValue())")
    @Mapping(target = "name", expression = "java(net.linlan.emis.uext.constant.MailType.getType(template.getType()).getName())")
    @Mapping(target = "content", expression = "java(org.springframework.web.util.HtmlUtils.htmlUnescape(template.getContent()))")
    EmailTemplateResult entityConvertToEmailTemplateDetailResult(TemplateInfo template);

    /**
     * 电子邮件模板配置更新参数转换为 entity
     *
     * @param param {@link EmailCustomTemplateSaveParam}
     * @return {@link TemplateInfo}
     */

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "content", expression = "java(org.springframework.web.util.HtmlUtils.htmlEscape(param.getContent()))")
    TemplateInfo emailTemplateConfigSaveParamConvertToEntity(EmailCustomTemplateSaveParam param);

    /**
     * 枚举列表转邮件模板类型列表
     *
     * @param values   {@link List>}
     * @param entities {@link List>}
     * @return {@link List>}
     */
    default List<EmailTemplateListResult> mailTemplateTypeConvertToEmailTemplateListResult(List<MailType> values,
                                                                                           List<TemplateInfo> entities) {
        List<EmailTemplateListResult> results = new ArrayList<>();
        //处理枚举
        for (MailType value : values) {
            EmailTemplateListResult cipher = new EmailTemplateListResult();
            cipher.setCode(value.getKey());
            cipher.setName(value.getName());
            cipher.setDescription(value.getValue());
            cipher.setContent(value.getContent());
            cipher.setContent(htmlUnescape(readEmailContent(value.getContent())));
            results.add(cipher);
        }
        // 数据库有，为自定义
        for (EmailTemplateListResult result : results) {
            for (TemplateInfo entity : entities) {
                if (result.getCode().equals(entity.getType())) {
                    result.setCustom(true);
                    result.setContent(htmlUnescape(entity.getContent()));
                }
            }
        }
        return results;
    }

    /**
     * 邮件类型枚举转为邮件模板详情返回
     *
     * @param templateType {@link MailType}
     * @return {@link EmailTemplateResult}
     */
    default EmailTemplateResult mailTemplateTypeConvertToEmailTemplateDetailResult(MailType templateType) {
        EmailTemplateResult result = new EmailTemplateResult();
        result.setContent(htmlUnescape(readEmailContent(templateType.getContent())));
        result.setDesc(templateType.getValue());
        result.setSender(templateType.getSender());
        result.setSubject(templateType.getSubject());
        result.setCustom(false);
        return result;
    }
}
