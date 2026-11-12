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
package net.linlan.emis.console.converter.authn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.linlan.emis.authn.alipay.AlipayIdentityProviderOAuth2Config;
import net.linlan.emis.authn.dingtalk.DingTalkIdentityProviderOAuth2Config;
import net.linlan.emis.authn.feishu.FeiShuIdentityProviderOAuth2Config;
import net.linlan.emis.authn.gitee.GiteeIdentityProviderOAuth2Config;
import net.linlan.emis.authn.github.GithubIdentityProviderOAuth2Config;
import net.linlan.emis.authn.qq.QqIdentityProviderOAuth2Config;
import net.linlan.emis.authn.wechat.WeChatIdentityProviderOAuth2Config;
import net.linlan.emis.authn.wechatwork.WeChatWorkIdentityProviderOAuth2Config;
import net.linlan.emis.console.pojo.result.authn.IdentityProviderListResult;
import net.linlan.emis.console.pojo.result.authn.IdentityProviderResult;
import net.linlan.emis.console.pojo.save.authn.IdentityProviderCreateParam;
import net.linlan.emis.console.pojo.update.authn.IdentityProviderUpdateParam;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.manage.client.IdentityProviderConfig;
import net.linlan.emis.provider.constant.IdentityProviderCategory;
import net.linlan.emis.provider.constant.IdentityProviderType;
import net.linlan.emis.provider.entity.IdentityProvider;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.validation.ValidationUtils;

/**
 * 身份提供商转换器
 *
 * @author Linlan
 * Created on  2020/8/22 23:59
 */
@Mapper(componentModel = "spring")
public interface IdentityProviderConverter {

    /**
     * 认证源平台枚举转换器到认证源平台结果
     *
     * @param values {@link  List}
     * @return {@link List}
     */
    default Page<IdentityProviderListResult> entityConverterToIdentityProviderResult(com.github.pagehelper.Page<? extends IdentityProvider> values) {
        Page<IdentityProviderListResult> result = new Page<>();
        ArrayList<IdentityProviderListResult> list = new ArrayList<>();
        for (IdentityProvider entity : values) {
            list.add(entityConverterToIdentityProviderResult(entity));
        }
        //@formatter:off
        result.setPagination(Page.Pagination.builder()
                .total(values.getTotal())
                .totalPages(values.getPages())
                .current(values.getPageNum())
                .build());
        //@formatter:on
        result.setList(list);
        return result;
    }

    /**
     * 实体转认证源详情
     *
     * @param entity {@link  IdentityProvider}
     * @return {@link  IdentityProviderListResult}
     */
    @Mapping(target = "desc", expression = "java(net.linlan.emis.provider.constant.IdentityProviderType.getIdentityProviderType(entity.getType()).desc())")
    IdentityProviderListResult entityConverterToIdentityProviderResult(IdentityProvider entity);

    /**
     * 保存入参转换为实体类
     *
     * @param param {@link IdentityProviderCreateParam}
     * @return {@link IdentityProvider}
     */
    default IdentityProvider identityProviderCreateParamConverterToEntity(IdentityProviderCreateParam param) {
        if (param == null) {
            return null;
        }
        IdentityProviderCategory category = IdentityProviderCategory.getType(param.getCategory());
        if (!category.getProviders().stream().map(IdentityProviderType::value)
            .collect(Collectors.toList()).contains(param.getType())) {
            throw new SupportException("认证源类型与认证源提供商不匹配");
        }
        try {
            IdentityProviderConfig identityProviderConfig = getIdentityProviderConfig(
                param.getType(), param.getConfig());
            ObjectMapper objectMapper = new ObjectMapper();
            // 指定序列化输入的类型
            objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            //封装数据
            IdentityProvider entity = new IdentityProvider();
            entity.setName(param.getName());
            entity.setCode(RandomStringUtils.randomAlphanumeric(32).toLowerCase());
            entity.setType(param.getType());
            entity.setCategory(param.getCategory());
            entity.setIsDisplayed(param.getDisplayed());
            entity.setIsEnabled(Boolean.TRUE);
            entity.setDescription(param.getRemark());
            //配置
            entity.setConfig(objectMapper.writeValueAsString(identityProviderConfig));
            return entity;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * entity转换为详细信息结果
     *
     * @param entity {@link IdentityProvider}
     * @return {@link IdentityProviderListResult}
     */
    default IdentityProviderResult entityConverterToIdentityProviderDetailResult(IdentityProvider entity) {
        if (entity == null) {
            return null;
        }
        IdentityProviderResult result = new IdentityProviderResult();
        if (entity.getId() != null) {
            result.setId(String.valueOf(entity.getId()));
        }
        result.setType(entity.getType());
        result.setDisplayed(entity.getIsDisplayed());
        result.setName(entity.getName());
        result.setRemark(entity.getDescription());
        //回调地址
        result.setRedirectUri(
            PropertiesContextHelper.getPortalPublicBaseUrl() + IdentityProviderType
                .getIdentityProviderType(entity.getType()).getLoginPathPrefix() + "/"
                              + entity.getCode());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 指定序列化输入的类型
            objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            IdentityProviderConfig config = objectMapper.readValue(entity.getConfig(),
                IdentityProviderConfig.class);
            result.setConfig(config);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 修改入参转换为实体类
     *
     * @param param {@link IdentityProviderUpdateParam}
     * @return {@link IdentityProvider}
     */
    default IdentityProvider identityProviderUpdateParamConverterToEntity(IdentityProviderUpdateParam param) {
        if (param == null) {
            return null;
        }
        IdentityProviderConfig identityProviderConfig = getIdentityProviderConfig(param.getType(),
            param.getConfig());
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定序列化输入的类型
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        try {
            //封装数据
            IdentityProvider identityProviderEntity = new IdentityProvider();
            identityProviderEntity.setName(param.getName());
            identityProviderEntity.setType(param.getType());
            identityProviderEntity.setIsDisplayed(param.getDisplayed());
            identityProviderEntity.setDescription(param.getRemark());
            //配置
            identityProviderEntity
                .setConfig(objectMapper.writeValueAsString(identityProviderConfig));
            return identityProviderEntity;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取认证源配置
     *
     * @param type   {@link  IdentityProviderType}
     * @param config {@link  JSONObject}
     * @return {@link  IdentityProviderConfig}
     */
    default IdentityProviderConfig getIdentityProviderConfig(String type, JSONObject config) {
        //开始处理不同提供商的配置
        IdentityProviderConfig identityProviderConfig;
        //微信扫码
        if (type.equals(IdentityProviderType.WECHAT.value())) {
            identityProviderConfig = config.to(WeChatIdentityProviderOAuth2Config.class);
        }
        //钉钉OAuth
        else if (type.equals(IdentityProviderType.DINGTALK_OAUTH.value())) {
            identityProviderConfig = config.to(DingTalkIdentityProviderOAuth2Config.class);
            //企业微信扫码
        } else if (type.equals(IdentityProviderType.WECHAT_WORK_OAUTH.value())) {
            identityProviderConfig = config.to(WeChatWorkIdentityProviderOAuth2Config.class);
            //QQ认证
        } else if (type.equals(IdentityProviderType.QQ_OAUTH.value())) {
            identityProviderConfig = config.to(QqIdentityProviderOAuth2Config.class);
            //飞书认证
        } else if (type.equals(IdentityProviderType.FEISHU_OAUTH.value())) {
            identityProviderConfig = config.to(FeiShuIdentityProviderOAuth2Config.class);
            //GITHUB认证
        } else if (type.equals(IdentityProviderType.GITHUB_OAUTH.value())) {
            identityProviderConfig = config.to(GithubIdentityProviderOAuth2Config.class);
        }
        //Gitee认证
        else if (type.equals(IdentityProviderType.GITEE_OAUTH.value())) {
            identityProviderConfig = config.to(GiteeIdentityProviderOAuth2Config.class);
        }
        //支付宝认证
        else if (type.equals(IdentityProviderType.ALIPAY_OAUTH.value())) {
            identityProviderConfig = config.to(AlipayIdentityProviderOAuth2Config.class);
        } else {
            throw new SupportException("不支持此身份提供商");
        }
        if (!Objects.nonNull(identityProviderConfig)) {
            throw new NullPointerException("提供商配置不能为空");
        }
        ValidationUtils.ValidationResult<?> validationResult = ValidationUtils
            .validateEntity(identityProviderConfig);
        if (validationResult.isHasErrors()) {
            throw new ConstraintViolationException(validationResult.getConstraintViolations());
        }
        return identityProviderConfig;
    }
}
