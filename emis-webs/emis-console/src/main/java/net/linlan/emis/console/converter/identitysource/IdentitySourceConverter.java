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
package net.linlan.emis.console.converter.identitysource;

import java.util.*;

import javax.validation.ConstraintViolationException;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;

import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceConfigGetResult;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceGetResult;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceListResult;
import net.linlan.emis.console.pojo.save.identitysource.IdentitySourceConfigSaveParam;
import net.linlan.emis.console.pojo.save.identitysource.IdentitySourceCreateParam;
import net.linlan.emis.console.pojo.update.identity.IdentitySourceUpdateParam;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.core.encrypt.EncryptionModule;
import net.linlan.emis.core.exception.user.IdentitySourceProviderNotSupportedException;
import net.linlan.emis.idsource.constant.IdentitySourceProvider;
import net.linlan.emis.idsource.dto.IdentitySourceDto;
import net.linlan.emis.idsource.entity.IdentitySource;
import net.linlan.emis.manage.IdentitySourceConfig;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.emis.support.validation.ValidationUtils;
import net.linlan.emis.verify.dingtalk.DingTalkConfig;
import net.linlan.emis.verify.feishu.FeiShuConfig;
import static net.linlan.emis.core.constant.SynchronizerConstants.EVENT_RECEIVE_PATH;
import static net.linlan.emis.idsource.constant.IdentitySourceProvider.DINGTALK;
import static net.linlan.emis.idsource.constant.IdentitySourceProvider.FEISHU;
import static net.linlan.emis.support.constant.EmisConstants.PATH_SEPARATOR;

/**
 * 身份源转换器
 *
 * @author Linlan
 * Created on  2022/2/13 21:37
 */
@Mapper(componentModel = "spring")
public interface IdentitySourceConverter {

    /**
     * 回调地址名称
     */
    String CALLBACK_URL = "callbackUrl";

    /**
     * 身份源平台枚举转换器到身份源平台结果
     *
     * @param values {@link IdentitySource}
     * @return {@link List}
     */
    default net.linlan.emis.support.repository.domain.Page<IdentitySourceListResult> entityConverterToIdentitySourceListResult(Page<IdentitySourceDto> values) {

        net.linlan.emis.support.repository.domain.Page<IdentitySourceListResult> result = new net.linlan.emis.support.repository.domain.Page<>();
        if (!CollectionUtils.isEmpty(values)) {
            ArrayList<IdentitySourceListResult> list = new ArrayList<>();
            for (IdentitySource entity : values) {
                list.add(entityConverterToIdentitySourceListResult(entity));
            }

            //@formatter:off
            result.setPagination(net.linlan.emis.support.repository.domain.Page.Pagination.builder()
                    .total(values.getTotal())
                    .totalPages(values.getPages())
                    .current(values.getPageNum())
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 身份源平台枚举转换器到身份源平台结果
     *
     * @param value {@link IdentitySource}
     * @return {@link List}
     */
    @Mapping(target = "desc", source = "provider.value")
    @Mapping(target = "provider", source = "provider.key")
    @Mapping(target = "icon", ignore = true)
    IdentitySourceListResult entityConverterToIdentitySourceListResult(IdentitySource value);

    /**
     * 创建入参转换为实体类
     *
     * @param param {@link IdentitySourceCreateParam}
     * @return {@link IdentitySource}
     */

    @Mapping(target = "basicConfig", ignore = true)
    @Mapping(target = "code", expression = "java(org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(32).toLowerCase())")
    @Mapping(target = "configured", expression = "java(Boolean.FALSE)")
    @Mapping(target = "enabled", expression = "java(Boolean.TRUE)")
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "jobConfig", ignore = true)
    @Mapping(target = "strategyConfig", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    IdentitySource createParamConverterToEntity(IdentitySourceCreateParam param);

    /**
     * entity转换为详细信息结果
     *
     * @param entity {@link IdentitySource}
     * @return {@link IdentitySourceGetResult}
     */
    IdentitySourceGetResult entityConverterToIdentitySourceGetResult(IdentitySource entity);

    /**
     * 保存入参转换为实体类
     *
     * @param param {@link IdentitySourceCreateParam}
     * @return {@link IdentitySource}
     */

    @Mapping(target = "code", ignore = true)
    @Mapping(target = "configured", ignore = true)
    @Mapping(target = "strategyConfig", ignore = true)
    @Mapping(target = "basicConfig", ignore = true)
    @Mapping(target = "jobConfig", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    IdentitySource updateParamConverterToEntity(IdentitySourceUpdateParam param);

    /**
     * 保存配置参数转entity
     *
     * @param param {@link  IdentitySourceConfigSaveParam}
     * @return {@link  IdentitySource}
     */

    @Mapping(target = "code", ignore = true)
    @Mapping(target = "configured", expression = "java(Boolean.TRUE)")
    @Mapping(target = "basicConfig", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    IdentitySource saveConfigParamConverterToEntity(IdentitySourceConfigSaveParam param);

    /**
     * 保存配置参数转entity
     *
     * @param param    {@link  IdentitySourceConfigSaveParam}
     * @param provider {@link  IdentitySourceProvider}
     * @return {@link  IdentitySource}
     */
    default IdentitySource saveConfigParamConverterToEntity(IdentitySourceConfigSaveParam param,
                                                                  IdentitySourceProvider provider) {
        try {
            ValidationUtils.ValidationResult<?> validationResult;
            IdentitySourceConfig clientConfig;
            ObjectMapper objectMapper = EncryptionModule.serializerEncrypt();
            if (provider.equals(DINGTALK)) {
                clientConfig = objectMapper.readValue(param.getBasicConfig().toJSONString(),
                    DingTalkConfig.class);
            } else if (provider.equals(FEISHU)) {
                clientConfig = objectMapper.readValue(param.getBasicConfig().toJSONString(),
                    FeiShuConfig.class);
            } else {
                throw new IdentitySourceProviderNotSupportedException();
            }
            //放置参数，并验证参数
            if (!Objects.nonNull(clientConfig)) {
                throw new NullPointerException("提供商配置不能为空!");
            }
            validationResult = ValidationUtils.validateEntity(clientConfig);
            //处理异常
            if (validationResult.isHasErrors()) {
                throw new ConstraintViolationException(validationResult.getConstraintViolations());
            }
            //封装数据
            IdentitySource source = saveConfigParamConverterToEntity(param);
            String value = objectMapper
                .activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                .writeValueAsString(clientConfig);
            source.setBasicConfig(value);
            return source;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * entity转换为配置结果
     *
     * @param entity {@link IdentitySource}
     * @return {@link IdentitySourceConfigGetResult}
     */
    default IdentitySourceConfigGetResult entityConverterToIdentitySourceConfigGetResult(IdentitySource entity) {
        if (entity == null) {
            return null;
        }
        IdentitySourceConfigGetResult identitySourceResult = new IdentitySourceConfigGetResult();
        if (entity.getId() != null) {
            identitySourceResult.setId(String.valueOf(entity.getId()));
        }
        identitySourceResult.setConfigured(entity.getIsConfigured());
        identitySourceResult.setJobConfig(entity.getJobConfig() == null ? null
            : com.alibaba.fastjson2.JSON.parseObject(entity.getJobConfig(),
                net.linlan.emis.idsource.config.JobConfig.class));
        identitySourceResult.setStrategyConfig(entity.getStrategyConfig());
        try {
            ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
            Map<String, Object> value = new HashMap<>(16);
            value.put(CALLBACK_URL, PropertiesContextHelper.getConsolePublicBaseUrl()
                                    + EVENT_RECEIVE_PATH + PATH_SEPARATOR + entity.getCode());
            IdentitySourceConfig config;
            // 指定序列化输入的类型
            objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            if (!Objects.isNull(entity.getBasicConfig())) {
                if (entity.getProvider().equals(DINGTALK)) {
                    config = objectMapper.readValue(entity.getBasicConfig(), DingTalkConfig.class);
                } else if (entity.getProvider().equals(FEISHU)) {
                    config = objectMapper.readValue(entity.getBasicConfig(), FeiShuConfig.class);
                } else {
                    throw new IdentitySourceProviderNotSupportedException();
                }
                value.putAll(BeanUtils.beanToMap(config));
            }
            identitySourceResult.setBasicConfig(value);
        } catch (JsonProcessingException e) {
            throw new SupportException(e.getMessage());
        }
        return identitySourceResult;
    }
}
