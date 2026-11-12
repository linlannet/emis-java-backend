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
package net.linlan.emis.protocol.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.linlan.emis.app.constant.AppProtocol;
import net.linlan.emis.app.constant.AppType;
import net.linlan.emis.app.constant.AuthorizationType;
import net.linlan.emis.app.constant.InitLoginType;
import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.protocol.base.exception.AppNotExistException;
import net.linlan.emis.protocol.cas.converter.AppCasStandardConfigConverter;
import net.linlan.emis.protocol.cas.model.AppCasStandardConfigGetResult;
import net.linlan.emis.protocol.cas.model.AppCasStandardSaveConfigParam;
import net.linlan.emis.protocol.constant.ProtocolConstants;
import net.linlan.emis.protocol.dto.ProtocolCasConfigDto;
import net.linlan.emis.protocol.entity.ProtocolCasConfig;
import net.linlan.emis.protocol.repository.*;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.validation.ValidationUtils;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import static net.linlan.emis.protocol.constant.ProtocolConstants.APP_CODE_VARIABLE;

/**
 * Cas 用户应用
 *
 * @author Linlan
 * Created on  2022/8/20 23:20
 */
@Component
public class CasStandardApplicationServiceImpl extends AbstractCasApplicationService {
    private final Logger                        logger = LoggerFactory
        .getLogger(CasStandardApplicationServiceImpl.class);

    /**
     * AppCasStandardConfigConverter
     */
    private final AppCasStandardConfigConverter appCasStandardConfigConverter;

    protected CasStandardApplicationServiceImpl(AppCertRepository appCertRepository,
                                                AppGroupAssociationRepository appGroupAssociationRepository,
                                                AppAccountRepository appAccountRepository,
                                                AppAccessPolicyRepository appAccessPolicyRepository,
                                                AppRepository appRepository,
                                                AppCasConfigRepository appCasConfigRepository,
                                                AppCasStandardConfigConverter appCasStandardConfigConverter) {
        super(appCertRepository, appGroupAssociationRepository, appAccountRepository,
            appAccessPolicyRepository, appRepository, appCasConfigRepository);
        this.appCasStandardConfigConverter = appCasStandardConfigConverter;
    }

    /**
     * 更新应用配置
     *
     * @param appId  {@link String}
     * @param config {@link Map}
     */
    @Override
    public void saveConfig(String appId, Map<String, Object> config) {
        AppCasStandardSaveConfigParam model;
        try {
            String value = mapper.writeValueAsString(config);
            // 指定序列化输入的类型
            mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
            model = mapper.readValue(value, AppCasStandardSaveConfigParam.class);
        } catch (Exception e) {
            throw new SupportException(e.getMessage());
        }

        ValidationUtils.ValidationResult<AppCasStandardSaveConfigParam> validationResult = ValidationUtils
            .validateEntity(model);
        if (validationResult.isHasErrors()) {
            throw new ConstraintViolationException(validationResult.getConstraintViolations());
        }

        //1、修改基本信息
        Optional<AppInfo> optional = appRepository.findById(appId);
        if (!optional.isPresent()) {
            AuditContext.setContent("保存配置失败，应用 [" + appId + "] 不存在！");
            logger.error(AuditContext.getContent());
            throw new AppNotExistException();
        }
        AppInfo appEntity = optional.get();
        appEntity.setAuthorizationType(model.getAuthorizationType().getKey());
        appEntity.setInitLoginUrl(model.getInitLoginUrl());
        appEntity.setInitLoginType(model.getInitLoginType().getKey());
        appRepository.save(appEntity);

        //2、修改cas配置
        Optional<ProtocolCasConfig> cas = appCasConfigRepository.findByAppId(appId);
        if (!cas.isPresent()) {
            AuditContext.setContent("保存配置失败，应用 [" + appId + "] 不存在！");
            logger.error(AuditContext.getContent());
            throw new AppNotExistException();
        }
        ProtocolCasConfig entity = cas.get();
        entity.setSpCallbackUrl(model.getSpCallbackUrl());
        appCasConfigRepository.save(entity);

    }

    /**
     * 获取配置
     *
     * @param appId {@link String}
     * @return {@link Map}
     */
    @Override
    public Object getConfig(String appId) {
        ProtocolCasConfigDto po = appCasConfigRepository.getByAppId(appId);
        AppCasStandardConfigGetResult result = new AppCasStandardConfigGetResult();
        result.setAuthorizationType(po.getAuthorizationType());
        result.setInitLoginType(po.getInitLoginType());
        result.setInitLoginUrl(po.getInitLoginUrl());
        result.setSpCallbackUrl(po.getSpCallbackUrl());

        String baseUrl = PropertiesContextHelper.getPortalPublicBaseUrl();
        // 服务端URL配置前缀
        result.setServerUrlPrefix(
            baseUrl + ProtocolConstants.CasEndpointConstants.CAS_AUTHORIZE_BASE_PATH
                .replace(APP_CODE_VARIABLE, po.getAppCode()));
        return result;
    }

    /**
     * 获取应用标志
     *
     * @return {@link String}
     */
    @Override
    public String getCode() {
        return "cas";
    }

    /**
     * 获取应用名称
     *
     * @return {@link String}
     */
    @Override
    public String getName() {
        return "CAS";
    }

    /**
     * 获取应用描述
     *
     * @return {@link String}
     */
    @Override
    public String getDescription() {
        return "CAS（Central Authentication Service，集中式认证服务，版本 2.0）是一种基于挑战、应答的开源单点登录协议。在集成客户端和服务端之间网络通畅的情况下广泛在企业中使用，有集成简便，扩展性强的优点。";
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
        return AppProtocol.CAS;
    }

    /**
     * 获取表单Schema
     *
     * @return {@link Map}
     */
    @Override
    public List<Map> getFormSchema() {
        return new ArrayList<>();
    }

    /**
     * 获取base64图标
     *
     * @return {@link String}
     */
    @Override
    public String getBase64Icon() {
        return "data:image/svg+xml;base64,PHN2ZyB0PSIxNjYwOTc4MDU4MDA3IiBjbGFzcz0iaWNvbiIgdmlld0JveD0iMCAwIDMzODIgMTAyNCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHAtaWQ9IjMwMDQiIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIj48cGF0aCBkPSJNOTI5LjI2MTQzNCA3NzEuNDQ3OThIMjUzLjU5MDY2OFYyNTYuMjY2NTExaDY3NS42NzA3NjZWNzUuMTU1OTYzSDI2Ni42ODI5OTZzLTczLjEwNzE2NC00LjY2MzkzNC0xMzQuNzA0NDA1IDUzLjI4NTQ0NWMtNjEuNjk3MTgzIDU3LjgxNjEyMy01NS43MzQwMSAxMjkuNjQwNzA1LTU1LjczNDAxIDEyOS42NDA3MDV2NjkwLjcxMTk1M2g4NTMuMDE2ODUzVjc3MS40NDc5OHoiIGZpbGw9IiMxRjNGNzgiIHAtaWQ9IjMwMDUiPjwvcGF0aD48cGF0aCBkPSJNMTAxMC44ODAyNzcgMTAyMy45NTAwMjlIMC4wMzkyMzNWMjIzLjM1MjQ2M2MtMC4xMzMyNTUtMi43NjUwNDctMi41ODE4MjEtODMuMTAxMzA4IDY2LjAyNzk3OC0xNTIuMzk0MDRDMTM3LjU3NTMxMi0xLjM2NTg2NiAyMjguMjg4ODI2LTAuMDMzMzE0IDIzMi4xNjk4ODYgMC4wMzMzMTRoNzc4LjcxMDM5MXYzMjUuODQyNDFIMzI1Ljg4MTY0M1Y3MDguNTg0ODE0bDY4OS41NDU5NjktMS4yNjU5MjUgMC4wNDk5NzEgMzAuNzgxOTYzaC00LjU5NzMwNnYyODUuODQ5MTc3ek0zMC44Mzc4NTQgOTkzLjE2ODA2Nmg5NDkuMjc3MTE2di0yNTUuMDE3MjQzbC02ODUuMDE1MjkxIDEuMjY1OTI1VjI5NS4wNjA0NDZIOTgwLjA5ODMxM1YzMC43OTg2MjFIMjMxLjgwMzQzNGMtMS4xOTkyOTctMC4wMzMzMTQtODEuOTM1MzI0LTAuODE2MTg4LTE0My44NDkwNDcgNjEuNzk3MTI0LTU5LjI5ODU4OCA1OS44OTgyMzctNTcuMTgzMTYxIDEyOS40MjQxNjYtNTcuMTQ5ODQ3IDEzMC4xMjM3NTVsMC4wMzMzMTQgNzcwLjQ0ODU2NnoiIGZpbGw9IiMyNDNFOTAiIHAtaWQ9IjMwMDYiPjwvcGF0aD48cGF0aCBkPSJNMzI1Ny43OTcwNTcgNDIzLjkwMTYybC02NzYuNTcwMjM5IDUuMzEzNTUzVjI1Ni4yNjY1MTFoNjc2LjU3MDIzOVY3NS4xNTU5NjNIMjU4My44MjUyOTZzLTcyLjk5MDU2NS00LjY2MzkzNC0xMzQuNTU0NDkzIDUzLjI4NTQ0NWMtNjEuNzEzODQgNTcuODE2MTIzLTU1Ljc1MDY2NyAxMjkuNjQwNzA1LTU1Ljc1MDY2NyAxMjkuNjQwNzA1djM0OS4xMTIxMDloNjgwLjA4NDg0NnYxNjYuNzE4OThoLTY4MC4wODQ4NDZ2MTc1LjEzMDcxOGg4NjQuMjkzNTc4VjQyMy45MDE2MnoiIGZpbGw9IiMxRjNGNzgiIHAtaWQ9IjMwMDciPjwvcGF0aD48cGF0aCBkPSJNMzMxNS4zNjMzMjcgMTAyMy45NTAwMjlIMjMzNS4zMDQyNDdhMTUuMzkwOTgyIDE1LjM5MDk4MiAwIDAgMS0xNS4zOTA5ODItMTUuMzkwOTgyVjcyMS45MjY5OTZjMC04LjQ5NTAyMiA2Ljg5NTk1OS0xNS4zOTA5ODIgMTUuMzkwOTgyLTE1LjM5MDk4Mmg2NzYuMDg3MTg4di0zOC4wNjEwMzJIMjMzNS4zMDQyNDdhMTUuMzkwOTgyIDE1LjM5MDk4MiAwIDAgMS0xNS4zOTA5ODItMTUuMzkwOTgydi00MjkuNzQ4MTk0Yy0wLjExNjU5OC0yLjc4MTcwMy0yLjQzMTkwOC04My4xMDEzMDggNjYuMTYxMjM0LTE1Mi4zOTQwNCA3MS4zNTgxODktNzIuMzI0Mjg5IDE2Mi4xODgzMDEtNzEuMDI1MDUgMTY2LjA4NjAxNy03MC45MjUxMDloNzYzLjIwMjgxMWM4LjQ5NTAyMiAwIDE1LjM5MDk4MiA2Ljg5NTk1OSAxNS4zOTA5ODIgMTUuMzkwOTgydjI5NS4wNDM3ODlhMTUuMzkwOTgyIDE1LjM5MDk4MiAwIDAgMS0xNS4zOTA5ODIgMTUuMzkwOTgyaC02NjkuNjI0MzA5djM4LjU3NzM5Nmg2NzAuMjU3MjcxYTE1LjM5MDk4MiAxNS4zOTA5ODIgMCAwIDEgMTUuMzkwOTgyIDE1LjQwNzYzOWwtMC42NDk2MTkgNjI4LjczMTYwMmExNS4zOTA5ODIgMTUuMzkwOTgyIDAgMCAxLTE1LjM3NDMyNSAxNS4zOTA5ODJ6IG0tOTY0LjY2ODA5OC0zMC43ODE5NjNoOTQ5LjI5Mzc3M2wwLjYxNjMwNi01OTcuOTQ5NjM5aC02NzAuMjQwNjE1YTE1LjM5MDk4MiAxNS4zOTA5ODIgMCAwIDEtMTUuMzkwOTgyLTE1LjM5MDk4MnYtNjkuMzc2MDE3YzAtOC40OTUwMjIgNi44OTU5NTktMTUuMzkwOTgyIDE1LjM5MDk4Mi0xNS4zOTA5ODJoNjY5LjYwNzY1MlYzMC43OTg2MjFoLTc0OC4xNzgyODFjLTEuMTQ5MzI3IDAuMDMzMzE0LTgyLjEwMTg5My0wLjc0OTU2MS0xNDMuODE1NzMzIDYxLjc5NzEyNC01OC44NjU1MDggNTkuNDQ4NS01Ny4yOTk3NTkgMTI5LjQ5MDc5My01Ny4yODMxMDIgMTMwLjE5MDM4M3Y0MTQuOTIzNTQ3aDY3Ni4wODcxODhjOC40OTUwMjIgMCAxNS4zOTA5ODIgNi44NzkzMDIgMTUuMzkwOTgyIDE1LjM5MDk4MnY2OC44MjYzMzljMCA4LjQ5NTAyMi02Ljg5NTk1OSAxNS4zOTA5ODItMTUuMzkwOTgyIDE1LjM5MDk4MmgtNjc2LjA4NzE4OHYyNTUuODUwMDg4eiBtLTU3LjUzMjk1NiAyOS43MzI1NzhoLTMxNy44NjM3NTJhMTUuMzkwOTgyIDE1LjM5MDk4MiAwIDAgMS0xMy42NDIwMDYtOC4yNjE4MjZMMTg3Mi4yNDIyMzkgODQzLjYwNTdIMTQ3My4xMjYwOTZhMTExOTIzLjc4NjMyMyAxMTE5MjMuNzg2MzIzIDAgMCAwLTc5Ljk2OTgwOSAxNzAuNDMzNDcgMTUuMzc0MzI1IDE1LjM3NDMyNSAwIDAgMS0xMy45MjUxNzQgOC44NjE0NzRIMTAzOS44Mjk5ODFhMTUuNDU3NjA5IDE1LjQ1NzYwOSAwIDAgMS0xMy4xMjU2NDMtNy4zMjkwMzkgMTUuNDI0Mjk2IDE1LjQyNDI5NiAwIDAgMS0wLjYxNjMwNS0xNS4wMDc4NzNjODMuMTE3OTY1LTE2NC4zNTM2OTkgMTY0LjM4NzAxMy0zMzEuNTA1NzU4IDI0Mi45NzQyOTgtNDkzLjE0NDM4MSA3OC42MzcyNTctMTYxLjczODU2NSAxNTkuOTM5NjE5LTMyOC45NzM5MDkgMjQzLjE1NzUyNS00OTMuNTEwODM0YTE1LjQwNzYzOSAxNS40MDc2MzkgMCAwIDEgMTMuNzQxOTQ4LTguNDQ1MDUyaDI2NS40OTQ0MzdjNy4wNzkxODUgMCAxMy4wNDIzNTggNC43ODA1MzIgMTQuODQxMzA0IDExLjI5MzM4MyA4NS44MTYzODQgMTY0LjIzNzEwMSAxNjkuMDE3NjMzIDMzMC4wMjMyOTQgMjQ5LjUwMzgwNyA0OTAuMzk1OTkyIDgwLjk1MjU2NyAxNjEuMzM4Nzk5IDE2NC42ODY4MzcgMzI4LjE0MTA2MyAyNTAuOTg2MjcxIDQ5My4yNDQzMjNhMTUuMzc0MzI1IDE1LjM3NDMyNSAwIDAgMS0xMy42MjUzNSAyMi41MDM0ODF6IG0tMzA4LjU1MjU0MS0zMC43NjUzMDdoMjgzLjE2NzQxNWMtODIuMjY4NDYyLTE1Ny45NDA3OS0xNjIuMTIxNjczLTMxNy4wNjQyMi0yMzkuNDU5NjkyLTQ3MS4xNzM5MjEtNzkuNTcwMDQ0LTE1OC41NTcwOTUtMTYxLjgyMTg0OS0zMjIuMzk0NDMtMjQ2LjUzODg3Ny00ODQuNzE1OTg3SDE1MzUuNDM5NTg0Yy04MS42MTg4NDMgMTYxLjYwNTMwOS0xNjEuNDM4NzQgMzI1Ljc5MjQzOS0yMzguNjc2ODE3IDQ4NC42MzI3MDItNzUuMDg5MzM2IDE1NC40NDI4MzktMTUyLjYyNzIzNyAzMTMuOTE2MDY1LTIzMS44OTc0NTYgNDcxLjI1NzIwNmgzMDQuNTg4MTk3YzI2LjYxNzczNy01Ni44MTY3MDkgNTMuMjUyMTMxLTExMy42MzM0MTggODAuMDAzMTI0LTE3MC40NTAxMjdhMTUuMzkwOTgyIDE1LjM5MDk4MiAwIDAgMSAxMy45MjUxNzQtOC44MjgxNmg0MTguMjA0OTU4YzUuNzI5OTc2IDAgMTAuOTkzNTU4IDMuMTgxNDY5IDEzLjY0MjAwNiA4LjI2MTgyNmw4OS4zODA5NjIgMTcxLjAxNjQ2MXoiIGZpbGw9IiMyNDNFOTAiIHAtaWQ9IjMwMDgiPjwvcGF0aD48cGF0aCBkPSJNMTUxNi42MzM5MzcgNTkzLjMxOTAxOWgyOTYuNDc2MjgzbC0xNDguMTc5ODQzLTMxMi41NTAxOTktMTQ4LjI5NjQ0IDMxMi41NTAxOTl6IG0yNDEuNjU4NDAzLTUwOS4wNjgzODVsNDM4LjI5MzE4NyA4NjUuMDU5Nzk2aC0xOTkuNzgyOTM5bC04Ni45OTkwMjQtMTY5LjAzNDI5SDE0MjAuMDU3MTkxbC04MC42MzYwODUgMTY5LjAzNDI5SDExMzYuNDIzMzgzbDQyMi4wODYwMTctODY1LjA1OTc5NmgxOTkuNzgyOTR6IiBmaWxsPSIjMUYzRjc4IiBwLWlkPSIzMDA5Ij48L3BhdGg+PHBhdGggZD0iTTE2NjMuOTQ3NjIgMzgxLjc1OTY0Nmw4My42Njc2NDMgMTc1LjExNDA2MUgxNTgwLjI5NjYzNGw4My42NTA5ODYtMTc1LjExNDA2MW0wLTUzLjUzNTI5OGwtMTIwLjI3OTUyNCAyNTEuNzUyNDg4aDI0MC41NTkwNDhsLTEyMC4yNzk1MjQtMjUxLjc1MjQ4OHoiIGZpbGw9IiMyNDNFOTAiIHAtaWQ9IjMwMTAiPjwvcGF0aD48L3N2Zz4=";
    }

    /**
     * 创建应用
     *
     * @param name   {@link String} 名称
     * @param remark {@link String} 备注
     */
    @Override
    public String create(String name, String icon, String remark, List<String> groups) {
        //1、创建基础信息
        AppInfo appEntity = new AppInfo();
        appEntity.setName(name);
        appEntity.setCode(
            org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(32).toLowerCase());
        appEntity.setTemplate(getCode());
        appEntity.setType(AppType.STANDARD.getKey());
        appEntity.setIsEnabled(true);
        appEntity.setProtocol(getProtocol().getKey());
        appEntity.setClientId(idGenerator.generateId().toString().replace("-", ""));
        appEntity.setClientSecret(idGenerator.generateId().toString().replace("-", ""));
        appEntity.setInitLoginType(InitLoginType.PORTAL_OR_APP.getKey());
        appEntity.setAuthorizationType(AuthorizationType.AUTHORIZATION.getKey());
        appEntity.setDescription(remark);
        appRepository.save(appEntity);

        ProtocolCasConfig casEntity = new ProtocolCasConfig();
        casEntity.setAppId(appEntity.getId());
        casEntity.setSpCallbackUrl("");
        appCasConfigRepository.save(casEntity);
        return appEntity.getId();
    }

}
