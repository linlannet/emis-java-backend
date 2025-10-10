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
package net.linlan.emis.manage;

import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.util.ClassUtils;

/**
 * Utility methods for {@link HttpMessageConverter}'s.
 *
 * @author Joe Grandja
 * @author luamas
 * @since 5.1
 */
public final class HttpMessageConverters {

    private static final boolean JACKSON2_JSON_PRESENT;
    private static final boolean JACKSON2_XML_PRESENT;

    private static final boolean GSON_PRESENT;

    private static final boolean JSONB_PRESENT;

    static {
        ClassLoader classLoader = HttpMessageConverters.class.getClassLoader();
        JACKSON2_JSON_PRESENT = ClassUtils
            .isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader)
                                && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator",
                                    classLoader);

        JACKSON2_XML_PRESENT = ClassUtils
            .isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);

        GSON_PRESENT = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
        JSONB_PRESENT = ClassUtils.isPresent("jakarta.json.bind.Jsonb", classLoader);
    }

    private HttpMessageConverters() {
    }

    public static GenericHttpMessageConverter<Object> getJsonMessageConverter() {
        if (JACKSON2_JSON_PRESENT) {
            return new MappingJackson2HttpMessageConverter();
        }
        if (GSON_PRESENT) {
            return new GsonHttpMessageConverter();
        }
        if (JSONB_PRESENT) {
            return new JsonbHttpMessageConverter();
        }
        return null;
    }

    public static GenericHttpMessageConverter<Object> getXmlMessageConverter() {
        if (JACKSON2_XML_PRESENT) {
            return new MappingJackson2XmlHttpMessageConverter();
        }
        return null;
    }

}
