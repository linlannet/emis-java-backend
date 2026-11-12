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
package net.linlan.emis.console.service.authn.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.authn.IdentityProviderConverter;
import net.linlan.emis.console.pojo.query.authn.IdentityProviderListQuery;
import net.linlan.emis.console.pojo.result.authn.IdentityProviderCreateResult;
import net.linlan.emis.console.pojo.result.authn.IdentityProviderListResult;
import net.linlan.emis.console.pojo.result.authn.IdentityProviderResult;
import net.linlan.emis.console.pojo.save.authn.IdentityProviderCreateParam;
import net.linlan.emis.console.pojo.update.authn.IdentityProviderUpdateParam;
import net.linlan.emis.console.service.authn.IdentityProviderService;
import net.linlan.emis.provider.entity.IdentityProvider;
import net.linlan.emis.provider.param.IdentityProviderParam;
import net.linlan.emis.support.event.ConfigRefreshUtils;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.utils.BeanUtils;
import static net.linlan.emis.core.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;

/**
 * <p>
 * 认证源配置 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-16
 */
@Slf4j
@Service
@AllArgsConstructor
public class IdentityProviderServiceImpl implements IdentityProviderService {

    /**
     * 平台是否启用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean identityProviderIsEnable(String id) {
        return identityProviderService.identityProviderIsEnable(id);
    }

    /**
     * 通过平台类型获取
     *
     * @param provider {@link String}
     * @return {@link IdentityProvider}
     */
    @Override
    public List<IdentityProvider> getByIdentityProvider(String provider) {
        return identityProviderService.findListByType(provider);
    }

    /**
     * 认证源列表
     *
     * @param pageModel {@link PageModel }
     * @param query     {@link  IdentityProviderListQuery}
     * @return {@link List}
     */
    @Override
    public Page<IdentityProviderListResult> getIdentityProviderList(PageModel pageModel,
                                                                    IdentityProviderListQuery query) {
        IdentityProviderParam param = new IdentityProviderParam();
        param.setName(query.getName());
        param.setCategory(query.getCategory());
        param.setPage(pageModel.getCurrent());
        param.setLimit(pageModel.getPageSize());
        return identityProviderConverter
            .entityConverterToIdentityProviderResult(identityProviderService.getPageDto(param));
    }

    /**
     * 认证源详情
     *
     * @param id {@link String}
     * @return {@link IdentityProviderResult}
     */
    @Override
    public IdentityProviderResult getIdentityProvider(String id) {
        IdentityProvider optional = identityProviderService.findById(id);
        if (optional != null) {
            return identityProviderConverter.entityConverterToIdentityProviderDetailResult(optional);
        }
        return new IdentityProviderResult();
    }

    /**
     * 创建认证源
     *
     * @param param {@link IdentityProviderCreateParam}
     * @return {@link IdentityProviderCreateResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityProviderCreateResult createIdp(IdentityProviderCreateParam param) {
        //转换对象
        IdentityProvider data = identityProviderConverter
            .identityProviderCreateParamConverterToEntity(param);
        identityProviderService.save(data);
        ConfigRefreshUtils.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        AuditContext.setTarget(Target.builder().id(data.getId()).name(data.getName())
            .type(TargetType.IDENTITY_PROVIDER).build());
        return IdentityProviderCreateResult.builder().id(String.valueOf(data.getId()))
            .type(data.getType()).build();
    }

    /**
     * 更新身份验证源
     *
     * @param param {@link IdentityProviderUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateIdentityProvider(IdentityProviderUpdateParam param) {
        IdentityProvider optional = identityProviderService.findById(param.getId());
        if (optional != null) {
            IdentityProvider entity = optional;
            //转换对象
            IdentityProvider data = identityProviderConverter
                .identityProviderUpdateParamConverterToEntity(param);
            BeanUtils.merge(data, entity);
            identityProviderService.update(entity);
            ConfigRefreshUtils.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
            AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
                .type(TargetType.IDENTITY_PROVIDER)
                .typeName(TargetType.IDENTITY_PROVIDER.getValue()).build());
            return true;
        }
        throw new NullPointerException("系统不存在该身份源");
    }

    /**
     * 删除认证源
     *
     * @param id {@link  String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteIdentityProvider(String id) {
        IdentityProvider optional = identityProviderService.findById(id);
        //管理员不存在
        if (optional == null) {
            AuditContext.setContent("删除失败，认证源不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        identityProviderService.deleteById(id);
        ConfigRefreshUtils.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        AuditContext.setTarget(Target.builder().id(id).name(optional.getName())
            .type(TargetType.IDENTITY_PROVIDER).build());
        return Boolean.TRUE;
    }

    /**
     * 更改认证源状态
     *
     * @param id      {@link String}
     * @param enabled {@link Boolean}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateIdentityProviderStatus(String id, Boolean enabled) {
        IdentityProvider optional = identityProviderService.findById(id);
        //管理员不存在
        if (optional == null) {
            AuditContext.setContent("删除失败，认证源不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        boolean result = identityProviderService.updateIdentityProviderStatus(id, enabled);
        ConfigRefreshUtils.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        AuditContext.setTarget(Target.builder().id(id).name(optional.getName())
            .type(TargetType.IDENTITY_PROVIDER).build());
        return result;
    }

    /**
     * 身份源身份转换器
     */
    private final IdentityProviderConverter identityProviderConverter;
    /**
     * 身份源service
     */
    private final net.linlan.emis.provider.service.IdentityProviderService identityProviderService;
}
