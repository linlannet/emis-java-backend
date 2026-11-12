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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.setting.MailTemplateConverter;
import net.linlan.emis.console.pojo.result.setting.EmailTemplateListResult;
import net.linlan.emis.console.pojo.result.setting.EmailTemplateResult;
import net.linlan.emis.console.pojo.save.setting.EmailCustomTemplateSaveParam;
import net.linlan.emis.console.service.setting.MailTemplateService;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.entity.TemplateInfo;
import net.linlan.emis.uext.service.TemplateInfoService;
import net.linlan.utils.SpringContextUtils;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;
import static net.linlan.emis.uext.constant.MessageSettingConstants.SETTING_EMAIL_TEMPLATE_CACHE_NAME;

/**
 * <p>
 * 邮件模板 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-13
 */
@Service
@CacheConfig(cacheNames = SETTING_EMAIL_TEMPLATE_CACHE_NAME)
public class MailTemplateServiceImpl extends SettingServiceImpl implements MailTemplateService {

    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(MailTemplateServiceImpl.class);

    /**
     * 根据邮件模板类型获取配置
     *
     * @param type {@link MailType}
     * @return SettingMailTemplateEntity
     */
    @Override
    @Cacheable(key = "#type.key", unless = "#result == null")
    public TemplateInfo getEmailTemplateByType(MailType type) {
        return templateInfoService.getByType(type.getKey());
    }

    /**
     * 添加邮件模板
     *
     * @param type {@link MailType}
     * @return SettingMailTemplateEntity
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(key = "#result.type.key", unless = "#result == null")
    public TemplateInfo saveCustomEmailTemplate(MailType type,
                                                      EmailCustomTemplateSaveParam param) {
        //入参映射为实体
        TemplateInfo entity = messageDataConverter
            .emailTemplateConfigSaveParamConvertToEntity(param);
        entity.setType(type.getKey());
        //查询数据库
        TemplateInfo template = getSelf().getEmailTemplateByType(type);
        if (!Objects.isNull(template)) {
            BeanUtils.merge(entity, template, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
            templateInfoService.save(template);
            return template;
        }
        templateInfoService.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(type.getName())
            .type(TargetType.MAIL_TEMPLATE).build());
        return entity;
    }

    /**
     * 邮件模板详情
     *
     * @param templateType {@link MailType}
     * @return {@link EmailTemplateResult}
     */
    @Override
    public EmailTemplateResult getEmailTemplate(MailType templateType) {
        //构建查询条件
        //查询
        TemplateInfo template = getSelf().getEmailTemplateByType(templateType);
        //如果数据库为空，查找默认模板
        EmailTemplateResult result;
        if (Objects.isNull(template)) {
            result = messageDataConverter
                .mailTemplateTypeConvertToEmailTemplateDetailResult(templateType);
        }
        //查询数据库
        else {
            result = messageDataConverter.entityConvertToEmailTemplateDetailResult(template);
        }
        return result;
    }

    /**
     * 获取邮件模板列表
     * 系统自带邮件模板及内容存入枚举，
     * 如果用户进行了自定义模板内容，将会把自定义的存入数据库，使用用户自定义的
     *
     * @return {@link List}
     */
    @Override
    public List<EmailTemplateListResult> getEmailTemplateList() {
        //从枚举类获取信息
        List<MailType> values = Arrays.asList(MailType.values());
        //从数据库获取
        List<TemplateInfo> lists = templateInfoService.getList(new StringMap().map());
        return messageDataConverter.mailTemplateTypeConvertToEmailTemplateListResult(values, lists);
    }

    /**
     * 禁用自定义模块
     *
     * @param type {@link MailType}
     */
    @Override
    @CacheEvict(key = "#type.key")
    public void disableCustomEmailTemplate(MailType type) {
        templateInfoService.deleteByType(type.getKey());
    }

    private MailTemplateService getSelf() {
        return SpringContextUtils.getBean(MailTemplateService.class);
    }

    /**
     * 消息服务数据映射
     */
    private final MailTemplateConverter  messageDataConverter;
    /**
     * TemplateInfoService
     */
    private final TemplateInfoService templateInfoService;

    public MailTemplateServiceImpl(EmisSettingService settingsRepository,
                                   MailTemplateConverter messageDataConverter,
                                   TemplateInfoService templateInfoService) {
        super(settingsRepository);
        this.messageDataConverter = messageDataConverter;
        this.templateInfoService = templateInfoService;
    }
}
