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
package net.linlan.emis.protocol.form.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.protocol.constant.FormEncryptType;
import net.linlan.emis.protocol.constant.FormSubmitType;
import net.linlan.emis.protocol.dto.ProtocolFormConfigDto;
import net.linlan.emis.protocol.entity.ProtocolFormConfig;
import net.linlan.emis.protocol.form.pojo.AppFormConfigGetResult;
import net.linlan.emis.protocol.form.pojo.AppFormProtocolEndpoint;
import net.linlan.emis.protocol.form.pojo.AppFormSaveConfigParam;
import static net.linlan.emis.protocol.constant.ProtocolConstants.APP_CODE;
import static net.linlan.emis.protocol.constant.ProtocolConstants.FormEndpointConstants.FORM_SSO_PATH;

/**
 * 应用映射
 *
 * @author Linlan
 * Created on 2020/8/14 22:45
 */
@Mapper(componentModel = "spring")
public interface AppFormConfigConverter {

    /**
     * save 转 entity
     *
     * @param config {@link AppFormSaveConfigParam}
     * @return {@link ProtocolFormConfig}
     */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "appId", ignore = true)
    ProtocolFormConfig appFormSaveConfigParamToEntity(AppFormSaveConfigParam config);

    /**
     * po 转 result
     *
     * @param po {@link ProtocolFormConfigDto}
     * @return {@link AppFormConfigGetResult}
     */
    default AppFormConfigGetResult entityConverterToFormConfigResult(ProtocolFormConfigDto po) {
        if (po == null) {
            return null;
        }
        AppFormConfigGetResult result = new AppFormConfigGetResult();
        if (po.getAppId() != null) {
            result.setAppId(po.getAppId());
        }
        result.setInitLoginUrl(po.getInitLoginUrl());
        result.setLoginUrl(po.getLoginUrl());
        result.setUsernameField(po.getUsernameField());
        result.setPasswordField(po.getPasswordField());
        result.setPasswordEncryptType(FormEncryptType.getType(po.getPasswordEncryptType()));
        result.setPasswordEncryptKey(po.getPasswordEncryptKey());
        result.setUsernameEncryptType(FormEncryptType.getType(po.getUsernameEncryptType()));
        result.setUsernameEncryptKey(po.getUsernameEncryptKey());
        result.setSubmitType(FormSubmitType.getType(po.getSubmitType()));
        List<ProtocolFormConfig.OtherField> list = po.getOtherField();
        if (list != null) {
            result.setOtherField(new ArrayList<>(list));
        }
        result.setProtocolEndpoint(getProtocolEndpointDomain(po.getAppCode()));
        return result;
    }

    /**
     * 获取协议端点
     *
     * @param appCode {@link String}
     * @return {@link AppFormProtocolEndpoint}
     */
    default AppFormProtocolEndpoint getProtocolEndpointDomain(String appCode) {
        //@formatter:off
        AppFormProtocolEndpoint domain = new AppFormProtocolEndpoint();
        Map<String,String> variables = new HashMap<>(16);
        variables.put(APP_CODE,appCode);
        StringSubstitutor sub = new StringSubstitutor(variables, "{", "}");
        //IDP SSO 端点
        domain.setIdpSsoEndpoint(sub.replace(PropertiesContextHelper.getPortalPublicBaseUrl()+FORM_SSO_PATH));
        return domain;
        //@formatter:on
    }
}
