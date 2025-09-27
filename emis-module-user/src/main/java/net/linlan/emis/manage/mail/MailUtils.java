/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.manage.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.web.util.HtmlUtils;

import net.linlan.emis.core.exception.uext.MailTemplateException;

/**
 * 邮件工具类
 *
 * @author Linlan
 */
public class MailUtils {
    /**
     * 获取内容文件
     *
     * @param resourceLocation {@link File}
     * @return {@link File}
     */
    public static String readEmailContent(@NonNull String resourceLocation) {
        try {
            ClassPathResource resource = new ClassPathResource(resourceLocation);
            InputStream inputStream = resource.getInputStream();
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            content = StringUtils.remove(content,
                StringUtils.substringBetween(content, "<!--", "-->"));
            content = StringUtils.remove(StringUtils.remove(content, "<!--"), "-->");
            content = HtmlUtils.htmlEscape(content);
            return content;
        } catch (IOException e) {
            throw new MailTemplateException("获取文件模板失败", e);
        }
    }

    public static boolean isEmailValidate(String email) {
        return true;
    }

}
