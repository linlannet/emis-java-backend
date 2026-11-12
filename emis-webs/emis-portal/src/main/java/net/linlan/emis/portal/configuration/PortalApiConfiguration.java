/*
 * emis-portal - portal门户平台
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
package net.linlan.emis.portal.configuration;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import net.linlan.emis.EmisPortalApplication;
import net.linlan.emis.portal.constant.PortalConstants;
import net.linlan.emis.support.manage.version.AppVersionUtils;

/**
 * 门户API配置
 *
 * @author Linlan
 * Created on  2020/6/16 21:28
 */
@Configuration
public class PortalApiConfiguration {

    /**
     * 门户管理 RestAPI
     *
     * @return {@link GroupedOpenApi}
     */
    @Bean
    public GroupedOpenApi portalRestApi() {
        return GroupedOpenApi.builder().group(PortalConstants.PORTAL_API_NAME)
            .pathsToMatch(PortalConstants.PORTAL_API_PATH + "/**").build();
    }

    /**
     * API INFO
     *
     * @return {@link Info}
     */
    private Info info() {
        Contact contact = new Contact();
        contact.setEmail("20400301@qq.com");
        contact.setName("EMIS");
        contact.setUrl("https://linlan.net/emis");
        return new Info()
            //title
            .title(environment.getProperty("spring.application.name"))
            //描述
            .description("EMIS 门户端 REST API 文档")
            //服务条款网址
            .termsOfService("https://linlan.net/emis")
            //内容
            .contact(contact)
            //版本
            .version(AppVersionUtils.getVersion(EmisPortalApplication.class));
    }

    /**
     * 定义openapi
     *
     * @return {@link OpenAPI}
     */
    @Bean
    public OpenAPI openApi() {
        OpenAPI openApi = new OpenAPI();
        openApi.setComponents(new Components());
        openApi.setPaths(new Paths());
        openApi.setInfo(info());
        return openApi;
    }

    private final Environment environment;

    public PortalApiConfiguration(Environment environment) {
        this.environment = environment;
    }
}
