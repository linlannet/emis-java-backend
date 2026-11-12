/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.configuration;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import net.linlan.emis.EmisOpenApiApplication;
import net.linlan.emis.openapi.constant.OpenApiConstants;
import net.linlan.emis.support.manage.version.AppVersionUtils;

/**
 * OpenAPI配置，包含文档
 *
 * @author Linlan
 * Created on 2022/9/5 21:00
 */
@Configuration
public class OpenApiConfiguration {

    /**
     * 权限管理 RestAPI
     *
     * @return {@link GroupedOpenApi}
     */
    @Bean
    public GroupedOpenApi openapiRestApi() {
        return GroupedOpenApi.builder().group(OpenApiConstants.OPEN_API_NAME)
            .pathsToMatch(OpenApiConstants.OPEN_API_PATH + "/**").build();
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
            .description("REST API 文档")
            //服务条款网址
            .termsOfService("https://linlan.net/emis")
            //内容
            .contact(contact)
            //版本
            .version(AppVersionUtils.getVersion(EmisOpenApiApplication.class));
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(info());
    }

    private final Environment environment;

    public OpenApiConfiguration(Environment environment) {
        this.environment = environment;
    }

}
