/*
 * emis-module-provider - provider身份提供商
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
package net.linlan.emis.manage.template;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.support.exception.TemplateNotExistException;

/**
 *
 * @author Linlan
 */
@Slf4j
public class BindIdentityProviderTemplate {

    public BindIdentityProviderTemplate() {
        configFreemarkerTemplate();
    }

    /**
     * 返回绑定模版
     *
     * @param response       {@link HttpServletResponse}
     * @param result       {@link ResponseResult}
     * @throws IOException  异常
     */
    public void sendAuthorizationResponse(HttpServletResponse response,
                                          ResponseResult<Void> result) throws IOException {
        //@formatter:off
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(ContentType.TEXT_HTML.getMimeType());
            Template template = freemarkerTemplateConfiguration.getTemplate("bind_redirect.ftlh");
            Map<String, Object> data = new HashMap<>(16);
            data.put("nonce", System.currentTimeMillis());
            ObjectMapper objectMapper = new ObjectMapper();
            data.put("result", objectMapper.writeValueAsString(result));
            template.process(data, response.getWriter());
        } catch (Exception e) {
            log.error("返回绑定提供商Template异常", e);
            //Html
            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        //@formatter:on
    }

    private void configFreemarkerTemplate() {
        try {
            //模板存放路径
            freemarkerTemplateConfiguration
                .setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/template/"));
            //编码
            freemarkerTemplateConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.name());
            //国际化
            freemarkerTemplateConfiguration.setLocale(new Locale("zh_CN"));
        } catch (Exception exception) {
            throw new TemplateNotExistException(exception);
        }
    }

    /**
     * freemarker 配置实例化
     */
    private final Configuration freemarkerTemplateConfiguration = new Configuration(
        Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
}
