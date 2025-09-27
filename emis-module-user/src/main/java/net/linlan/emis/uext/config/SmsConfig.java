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
package net.linlan.emis.uext.config;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import net.linlan.emis.core.constant.Language;
import net.linlan.emis.manage.sms.SmsProviderConfig;
import net.linlan.emis.uext.constant.SmsProvider;
import net.linlan.emis.uext.constant.SmsType;

/**
 * 短信配置
 *
 * @author Linlan
 */
@Data
public class SmsConfig implements Serializable {

    private static final long    serialVersionUID = -8631010419770301731L;

    /**
     * 提供商
     */
    private SmsProvider          provider;

    /**
     * 语言
     */
    private Language             language;

    /**
     * 配置
     */
    private SmsProviderConfig    config;

    /**
     * 模版配置
     */
    private List<TemplateConfig> templates;

    public SmsConfig() {
    }

    @Data
    public static class TemplateConfig implements Serializable {

        private static final long serialVersionUID = -8631010419770301760L;

        @Parameter(description = "短信类型")
        private SmsType           type;
        @Parameter(description = "模板ID/CODE")
        private String            code;

        public TemplateConfig() {
        }
    }
}
