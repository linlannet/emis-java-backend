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
package net.linlan.emis.protocol.jwt.converter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.mapstruct.Mapper;

import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.protocol.constant.JwtBindingType;
import net.linlan.emis.protocol.constant.JwtIdTokenSubjectType;
import net.linlan.emis.protocol.dto.ProtocolJwtConfigDto;
import net.linlan.emis.protocol.entity.ProtocolJwtConfig;
import net.linlan.emis.protocol.jwt.pojo.AppJwtConfigGetResult;
import net.linlan.emis.protocol.jwt.pojo.AppJwtProtocolEndpoint;
import net.linlan.emis.protocol.jwt.pojo.AppJwtSaveConfigParam;
import static net.linlan.emis.protocol.constant.ProtocolConstants.APP_CODE;
import static net.linlan.emis.protocol.constant.ProtocolConstants.JwtEndpointConstants.JWT_SSO_PATH;

/**
 * 应用映射
 *
 * @author Linlan
 * Created on 2023/02/12 22:45
 */
@Mapper(componentModel = "spring")
public interface AppJwtConfigConverter {

    /**
     * save 转 entity
     *
     * @param config {@link AppJwtSaveConfigParam}
     * @return {@link ProtocolJwtConfig}
     */
    default ProtocolJwtConfig appJwtSaveConfigParamToEntity(AppJwtSaveConfigParam config) {
        if (config == null) {
            return null;
        }

        ProtocolJwtConfig entity = new ProtocolJwtConfig();
        entity.setRedirectUrl(config.getRedirectUrl());
        entity.setTargetLinkUrl(config.getTargetLinkUrl());
        entity.setBindingType(config.getBindingType().getKey());
        entity.setIdTokenSubjectType(config.getIdTokenSubjectType().getKey());
        entity.setIdTokenTimeToLive(config.getIdTokenTimeToLive().longValue());
        return entity;
    }

    /**
     * po 转 result
     *
     * @param po {@link ProtocolJwtConfigDto}
     * @return {@link AppJwtConfigGetResult}
     */
    default AppJwtConfigGetResult entityConverterToFormConfigResult(ProtocolJwtConfigDto po) {
        if (po == null) {
            return null;
        }
        AppJwtConfigGetResult result = new AppJwtConfigGetResult();
        if (po.getAppId() != null) {
            result.setAppId(po.getAppId());
        }
        result.setInitLoginUrl(po.getInitLoginUrl());
        result.setRedirectUrl(po.getRedirectUrl());
        result.setTargetLinkUrl(po.getTargetLinkUrl());
        result.setBindingType(JwtBindingType.getType(po.getBindingType()));
        result.setIdTokenSubjectType(JwtIdTokenSubjectType.getType(po.getIdTokenSubjectType()));
        result.setIdTokenTimeToLive(String.valueOf(po.getIdTokenTimeToLive()));
        result.setProtocolEndpoint(getProtocolEndpointDomain(po.getAppCode()));
        return result;
    }

    /**
     * 获取协议端点
     *
     * @param appCode {@link String}
     * @return {@link AppJwtProtocolEndpoint}
     */
    default AppJwtProtocolEndpoint getProtocolEndpointDomain(String appCode) {
        //@formatter:off
        AppJwtProtocolEndpoint domain = new AppJwtProtocolEndpoint();
        Map<String,String> variables = new HashMap<>(16);
        variables.put(APP_CODE,appCode);
        StringSubstitutor sub = new StringSubstitutor(variables, "{", "}");
        //IDP SSO 端点
        domain.setIdpSsoEndpoint(sub.replace(PropertiesContextHelper.getPortalPublicBaseUrl()+JWT_SSO_PATH));
        return domain;
        //@formatter:on
    }
}
