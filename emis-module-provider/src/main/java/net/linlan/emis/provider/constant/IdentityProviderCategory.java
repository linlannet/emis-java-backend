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
package net.linlan.emis.provider.constant;

import java.util.List;

import com.google.common.collect.Lists;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 身份源类型
 *
 * @author Linlan
 */
public enum IdentityProviderCategory implements BaseEnumString {
                                                                /**
                                                                 * 社交
                                                                 */
                                                                social("social", "社交", Lists
                                                                    .newArrayList(
                                                                        IdentityProviderType.QQ_OAUTH,
                                                                        IdentityProviderType.WECHAT,
                                                                        IdentityProviderType.GITEE_OAUTH,
                                                                        IdentityProviderType.GITHUB_OAUTH,
                                                                        IdentityProviderType.ALIPAY_OAUTH)),
                                                                /**
                                                                 * 企业
                                                                 */
                                                                enterprise("enterprise", "企业", Lists
                                                                    .newArrayList(
                                                                        IdentityProviderType.WECHAT_WORK_OAUTH,
                                                                        IdentityProviderType.DINGTALK_OAUTH,
                                                                        IdentityProviderType.FEISHU_OAUTH));

    private final String                     key;

    private final String                     value;

    private final List<IdentityProviderType> providers;

    IdentityProviderCategory(String key, String value, List<IdentityProviderType> providers) {
        this.key = key;
        this.value = value;
        this.providers = providers;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    public List<IdentityProviderType> getProviders() {
        return providers;
    }

    /**
     * 获取类型
     *
     * @param code 代码编码
     * @return {@link IdentityProviderCategory}
     */
    @EnumConvert
    public static IdentityProviderCategory getType(String code) {
        IdentityProviderCategory[] values = values();
        for (IdentityProviderCategory status : values) {
            if (String.valueOf(status.getKey()).equals(code)) {
                return status;
            }
        }
        throw new NullPointerException("未获取到对应平台");
    }
}
