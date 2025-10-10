/*
 * emis-java-backend - 基于JAVA开发的企业级多身份管理系统后端工程，依托EFRAME框架开发，基于Springboot，采用支持 JAVA 1.8最高版本的framework、security，后端数据访问采用Mybatis框架，支持多种数据库一套代码适配，提供脚手架可快速生成Mapper等，在仓库功能上，借鉴主流的SSO和IAM框架，实现统一用户管理。前端提供VUE框架和REACT框架的控制台管理工程、企业个人门户工程和移动端应用扩展。
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
package net.linlan.emis.manage.config;

import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import net.linlan.emis.manage.endpoint.EndpointMatcher;

/**
 * Base configurer
 *
 * @author Linlan
 *
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public abstract class AbstractConfigurer {
    private final ObjectPostProcessor<Object> objectPostProcessor;

    public AbstractConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        this.objectPostProcessor = objectPostProcessor;
    }

    /**
     * init
     *
     * @param httpSecurity {@link HttpSecurity}
     */
    public abstract void init(HttpSecurity httpSecurity);

    /**
     * configure
     *
     * @param httpSecurity {@link HttpSecurity}
     */
    public abstract void configure(HttpSecurity httpSecurity);

    /**
     * 获取请求匹配器
     *
     * @return {@link EndpointMatcher}
     */
    public abstract EndpointMatcher getEndpointMatcher();

    public final <T> T postProcess(T object) {
        return (T) this.objectPostProcessor.postProcess(object);
    }

    public final ObjectPostProcessor<Object> getObjectPostProcessor() {
        return this.objectPostProcessor;
    }

}
