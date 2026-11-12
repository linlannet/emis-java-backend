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
package net.linlan.emis.console.service.identitysource.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.pagehelper.Page;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.identitysource.IdentitySourceConverter;
import net.linlan.emis.console.pojo.other.IdentitySourceConfigValidatorParam;
import net.linlan.emis.console.pojo.query.identity.IdentitySourceListQuery;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceListResult;
import net.linlan.emis.console.pojo.save.authn.IdentityProviderCreateParam;
import net.linlan.emis.console.pojo.save.identitysource.IdentitySourceConfigSaveParam;
import net.linlan.emis.console.pojo.save.identitysource.IdentitySourceCreateParam;
import net.linlan.emis.console.pojo.save.identitysource.IdentitySourceCreateResult;
import net.linlan.emis.console.pojo.update.identity.IdentitySourceUpdateParam;
import net.linlan.emis.console.service.identitysource.IdentitySourceService;
import net.linlan.emis.idsource.config.StrategyConfig;
import net.linlan.emis.idsource.dto.IdentitySourceDto;
import net.linlan.emis.idsource.entity.IdentitySource;
import net.linlan.emis.idsource.param.IdentitySourceParam;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.emis.user.constant.AccountConstants;
import net.linlan.emis.verify.dingtalk.DingTalkConfig;
import net.linlan.emis.verify.feishu.FeiShuConfig;
import static net.linlan.emis.idsource.constant.IdentitySourceProvider.DINGTALK;
import static net.linlan.emis.idsource.constant.IdentitySourceProvider.FEISHU;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * <p>
 * 身份源配置 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-16
 */
@Slf4j
@Service
@AllArgsConstructor
@CacheConfig(cacheNames = { AccountConstants.IDS_CACHE_NAME })
public class IdentitySourceServiceImpl implements IdentitySourceService {

    /**
     * 身份源列表
     *
     * @param pageModel {@link  PageModel}
     * @return {@link List}
     */
    @Override
    public net.linlan.emis.support.repository.domain.Page<IdentitySourceListResult> getIdentitySourceList(IdentitySourceListQuery query,
                                                                PageModel pageModel) {
        IdentitySourceParam param = new IdentitySourceParam();
        param.setName(query.getName());
        param.setPage(pageModel.getCurrent());
        param.setLimit(pageModel.getPageSize());
        Page<IdentitySourceDto> list = identitySourceService.getPageDto(param);
        return identitySourceConverter.entityConverterToIdentitySourceListResult(list);
    }

    /**
     * 身份源详情
     *
     * @param id {@link String}
     * @return {@link IdentitySource}
     */
    @Override
    public IdentitySource getIdentitySource(String id) {
        return identitySourceService.findById(id);
    }

    /**
     * 保存身份源
     *
     * @param param {@link IdentityProviderCreateParam}
     * @return {@link IdentitySourceCreateResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentitySourceCreateResult createIdentitySource(IdentitySourceCreateParam param) {
        IdentitySource entity = identitySourceConverter.createParamConverterToEntity(param);
        identitySourceService.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return new IdentitySourceCreateResult(entity.getId());
    }

    /**
     * 修改身份源
     *
     * @param param {@link IdentitySourceUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateIdentitySource(IdentitySourceUpdateParam param) {
        IdentitySource source = identitySourceConverter.updateParamConverterToEntity(param);
        IdentitySource entity = getIdentitySource(param.getId());
        BeanUtils.merge(source, entity, LAST_MODIFIED_TIME, LAST_MODIFIED_BY);
        identitySourceService.update(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return true;
    }

    /**
     * 禁用身份源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean disableIdentitySource(String id) {
        IdentitySource optional = identitySourceService.findById(id);
        //身份源不存在
        if (optional == null) {
            AuditContext.setContent("操作失败，身份源不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        Integer count = identitySourceService.updateIdentitySourceStatus(id, Boolean.FALSE);
        AuditContext.setTarget(Target.builder().id(id).name(optional.getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return count > 0;
    }

    /**
     * 启用身份源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean enableIdentitySource(String id) {
        IdentitySource optional = identitySourceService.findById(id);
        //用户不存在
        if (optional == null) {
            AuditContext.setContent("操作失败，身份源不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        Integer count = identitySourceService.updateIdentitySourceStatus(id, Boolean.TRUE);
        AuditContext.setTarget(Target.builder().id(id).name(optional.getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return count > 0;
    }

    /**
     * 删除身份源
     *
     * @param id {@link  String}
     * @return {@link  Boolean}
     */
    @Override
    public Boolean deleteIdentitySource(String id) {
        IdentitySource optional = identitySourceService.findById(id);
        //用户不存在
        if (optional == null) {
            AuditContext.setContent("操作失败，身份源不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        identitySourceService.deleteById(id);
        AuditContext.setTarget(Target.builder().id(id).name(optional.getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return true;
    }

    /**
     * 保存身份源配置
     *
     * @param param {@link  IdentitySourceConfigSaveParam}
     * @return {@link  Boolean}
     */
    @Override
    public Boolean saveIdentitySourceConfig(IdentitySourceConfigSaveParam param) {
        IdentitySource entity = getIdentitySource(param.getId());
        //转换
        IdentitySource source = identitySourceConverter.saveConfigParamConverterToEntity(param,
            net.linlan.emis.idsource.constant.IdentitySourceProvider
                .getType(entity.getProvider()));
        //合并对象
        BeanUtils.merge(source, entity, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
        identitySourceService.update(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return true;
    }

    /**
     * 更新身份源策略
     *
     * @param id             {@link Long} 主键
     * @param strategyConfig {@link String} 策略
     */
    @Override
    public void updateStrategyConfig(String id, String strategyConfig) {
        IdentitySource optional = identitySourceService.findById(id);
        //用户不存在
        if (optional == null) {
            AuditContext.setContent("操作失败，身份源不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        identitySourceService.updateStrategyConfig(id,
            JSON.parseObject(strategyConfig, StrategyConfig.class));
        AuditContext.setTarget(Target.builder().id(id).name(optional.getName())
            .type(TargetType.IDENTITY_SOURCE).build());

    }

    /**
     * 身份源配置验证
     *
     * @param param {@link  IdentitySourceConfigValidatorParam}
     * @return {@link  Boolean}
     */
    @Override
    public Boolean identitySourceConfigValidator(IdentitySourceConfigValidatorParam param) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            if (param.getProvider().equals(DINGTALK.getKey())) {
                DingTalkConfig config = objectMapper.readValue(param.getConfig().toJSONString(),
                    DingTalkConfig.class);
                return !net.linlan.emis.support.validation.ValidationUtils.validateEntity(config)
                    .isHasErrors();
            } else if (param.getProvider().equals(FEISHU.getKey())) {
                FeiShuConfig config = objectMapper.readValue(param.getConfig().toJSONString(),
                    FeiShuConfig.class);
                return !net.linlan.emis.support.validation.ValidationUtils.validateEntity(config)
                    .isHasErrors();
            } else {
                throw new SupportException("暂未支持此提供商连接验证");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 身份验证源转换器
     */
    private final IdentitySourceConverter  identitySourceConverter;

    /**
     * 身份源
     */
    private final net.linlan.emis.idsource.service.IdentitySourceService identitySourceService;

}
