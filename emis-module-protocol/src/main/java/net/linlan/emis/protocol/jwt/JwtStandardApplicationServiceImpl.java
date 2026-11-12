/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.jwt;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.app.constant.*;
import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.protocol.base.exception.AppNotExistException;
import net.linlan.emis.protocol.constant.*;
import net.linlan.emis.protocol.dto.ProtocolJwtConfigDto;
import net.linlan.emis.protocol.entity.ProtocolJwtConfig;
import net.linlan.emis.protocol.jwt.converter.AppJwtConfigConverter;
import net.linlan.emis.protocol.jwt.pojo.AppJwtSaveConfigParam;
import net.linlan.emis.protocol.repository.*;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.emis.support.validation.ValidationUtils;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * JWT 用户应用
 *
 * @author Linlan
 * Created on 2022/8/20 23:20
 */
@Component
@Slf4j
public class JwtStandardApplicationServiceImpl extends AbstractJwtApplicationService {

    /**
     * 更新应用配置
     *
     * @param appId {@link String}
     * @param config {@link Map}
     */
    @Override
    public void saveConfig(String appId, Map<String, Object> config) {
        AppJwtSaveConfigParam model;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.writeValueAsString(config);
            // 指定序列化输入的类型
            mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
            model = mapper.readValue(value, AppJwtSaveConfigParam.class);
        } catch (Exception e) {
            throw new SupportException(e.getMessage());
        }
        //@formatter:off
        ValidationUtils.ValidationResult<AppJwtSaveConfigParam> validationResult = ValidationUtils.validateEntity(model);
        if (validationResult.isHasErrors()) {
            throw new ConstraintViolationException(validationResult.getConstraintViolations());
        }
        //@formatter:on
        //1、修改基本信息
        Optional<AppInfo> optional = appRepository.findById(appId);
        if (!optional.isPresent()) {
            AuditContext.setContent("保存配置失败，应用 [" + appId + "] 不存在！");
            log.error(AuditContext.getContent());
            throw new AppNotExistException();
        }
        AppInfo appEntity = optional.get();
        appEntity.setInitLoginUrl(model.getInitLoginUrl());
        appEntity.setIsConfigured(true);
        appRepository.save(appEntity);
        //2、修改 JWT 配置
        Optional<ProtocolJwtConfig> jwt = appJwtConfigRepository.findByAppId(appId);
        if (!jwt.isPresent()) {
            AuditContext.setContent("保存配置失败，应用 [" + appId + "] 不存在！");
            log.error(AuditContext.getContent());
            throw new AppNotExistException();
        }
        ProtocolJwtConfig entity = jwt.get();
        ProtocolJwtConfig formConfig = appJwtConfigConverter.appJwtSaveConfigParamToEntity(model);
        BeanUtils.merge(formConfig, entity, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
        appJwtConfigRepository.save(entity);
    }

    /**
     * 获取配置
     *
     * @param appId {@link String}
     * @return {@link Map}
     */
    @Override
    public Object getConfig(String appId) {
        ProtocolJwtConfigDto configPo = appJwtConfigRepository.getByAppId(appId);
        return appJwtConfigConverter.entityConverterToFormConfigResult(configPo);
    }

    /**
     * 获取应用标志
     *
     * @return {@link String}
     */
    @Override
    public String getCode() {
        return AppProtocol.JWT.getKey();
    }

    /**
     * 获取应用名称
     *
     * @return {@link String}
     */
    @Override
    public String getName() {
        return AppProtocol.JWT.getValue();
    }

    /**
     * 获取应用描述
     *
     * @return {@link String}
     */
    @Override
    public String getDescription() {
        return "JWT（JSON Web Token）是在网络应用环境声明的一种基于 JSON 的开放标准。EMIS 使用 JWT 进行分布式站点的单点登录 （SSO）。JWT 单点登录基于非对称加密，由 EMIS 将用户状态和信息使用私钥加密，传递给应用后，应用使用公钥解密并进行验证。使用场景非常广泛，集成简单。";
    }

    /**
     * 获取应用类型
     *
     * @return {@link AppType}
     */
    @Override
    public AppType getType() {
        return AppType.STANDARD;
    }

    /**
     * 获取应用协议
     *
     * @return {@link AppProtocol}
     */
    @Override
    public AppProtocol getProtocol() {
        return AppProtocol.JWT;
    }

    /**
     * 获取base64图标
     *
     * @return {@link String}
     */
    @Override
    public String getBase64Icon() {
        return "data:image/svg+xml;base64,PHN2ZyB0PSIxNjYwOTc4MjExNDg3IiBjbGFzcz0iaWNvbiIgdmlld0JveD0iMCAwIDEwMjQgMTAyNCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHAtaWQ9IjM4NzEiIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIj48cGF0aCBkPSJNNTg5LjMxMiAyNzUuNDU2TDU4OC4yODggMGgtMTUzLjZsMS4wMjQgMjc1LjQ1NiA3Ni44IDEwNS40NzJ6IG0tMTUzLjYgNDcyLjA2NHYyNzYuNDhoMTUzLjZ2LTI3Ni40OEw1MTIuNTEyIDY0Mi4wNDh6IiBmaWxsPSIjRkZGRkZGIiBwLWlkPSIzODcyIj48L3BhdGg+PHBhdGggZD0iTTU4OS4zMTIgNzQ3LjUybDE2MS43OTIgMjIzLjIzMiAxMjMuOTA0LTkwLjExMi0xNjEuNzkyLTIyMy4yMzItMTIzLjkwNC0zOS45MzZ6IG0tMTUzLjYtNDcyLjA2NEwyNzIuODk2IDUyLjIyNGwtMTIzLjkwNCA5MC4xMTJMMzEwLjc4NCAzNjUuNTY4bDEyNC45MjggMzkuOTM2eiIgZmlsbD0iIzAwRjJFNiIgcC1pZD0iMzg3MyI+PC9wYXRoPjxwYXRoIGQ9Ik0zMTAuNzg0IDM2NS41NjhMNDguNjQgMjgwLjU3NiAxLjUzNiA0MjUuOTg0IDI2My42OCA1MTJsMTIzLjkwNC00MC45NnogbTMyNS42MzIgMTg2LjM2OGw3Ni44IDEwNS40NzIgMjYyLjE0NCA4NC45OTIgNDcuMTA0LTE0NS40MDgtMjYyLjE0NC04NC45OTJ6IiBmaWxsPSIjMDBCOUYxIiBwLWlkPSIzODc0Ij48L3BhdGg+PHBhdGggZD0iTTc2MC4zMiA1MTJsMjYyLjE0NC04Ni4wMTYtNDcuMTA0LTE0NS40MDhMNzEzLjIxNiAzNjUuNTY4bC03Ni44IDEwNS40NzJ6IG0tNDk2LjY0IDBMMS41MzYgNTk2Ljk5MiA0OC42NCA3NDIuNGwyNjIuMTQ0LTg0Ljk5MiA3Ni44LTEwNS40NzJ6IiBmaWxsPSIjRDYzQUZGIiBwLWlkPSIzODc1Ij48L3BhdGg+PHBhdGggZD0iTTMxMC43ODQgNjU3LjQwOEwxNDguOTkyIDg4MC42NGwxMjMuOTA0IDkwLjExMiAxNjIuODE2LTIyMy4yMzJWNjE3LjQ3MnpNNzEzLjIxNiAzNjUuNTY4bDE2MS43OTItMjIzLjIzMi0xMjMuOTA0LTkwLjExMi0xNjEuNzkyIDIyMy4yMzJ2MTMwLjA0OHoiIGZpbGw9IiNGQjAxNUIiIHAtaWQ9IjM4NzYiPjwvcGF0aD48L3N2Zz4=";
    }

    /**
     * 创建应用
     *
     * @param name     {@link String} 名称
     * @param icon     {@link String} 图标
     * @param remark   {@link String} 备注
     * @param groups {@link String} 应用分组
     */
    @Override
    public String create(String name, String icon, String remark, List<String> groups) {
        //1、创建应用
        AppInfo appEntity = createApp(name, icon, remark, groups, AuthorizationType.AUTHORIZATION);
        //jwt配置
        ProtocolJwtConfig jwtConfigEntity = new ProtocolJwtConfig();
        jwtConfigEntity.setAppId(appEntity.getId());
        jwtConfigEntity.setBindingType(JwtBindingType.POST.getKey());
        //id_token sub 类型
        jwtConfigEntity.setIdTokenSubjectType(JwtIdTokenSubjectType.USER_ID.getKey());
        //token有效期（秒）
        jwtConfigEntity.setIdTokenTimeToLive(600L);
        appJwtConfigRepository.save(jwtConfigEntity);
        // 创建RSA证书
        createCertificate(appEntity.getId(), appEntity.getCode(), AppCertUsingType.JWT_ENCRYPT);
        return String.valueOf(appEntity.getId());
    }

    private final AppJwtConfigConverter appJwtConfigConverter;

    public JwtStandardApplicationServiceImpl(AppJwtConfigRepository appJwtConfigRepository,
                                             AppGroupAssociationRepository appGroupAssociationRepository,
                                             AppJwtConfigConverter appJwtConfigConverter,
                                             AppCertRepository appCertRepository,
                                             AppRepository appRepository,
                                             AppAccountRepository appAccountRepository,
                                             AppAccessPolicyRepository appAccessPolicyRepository) {
        super(appJwtConfigRepository, appGroupAssociationRepository, appCertRepository,
            appRepository, appAccountRepository, appAccessPolicyRepository);
        this.appJwtConfigConverter = appJwtConfigConverter;
    }
}
