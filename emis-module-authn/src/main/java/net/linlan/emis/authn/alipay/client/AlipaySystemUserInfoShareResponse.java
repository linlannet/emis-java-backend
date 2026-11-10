/*
 * emis-module-authn - authn认证模块
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
package net.linlan.emis.authn.alipay.client;

import com.aliyun.tea.NameInMap;
import com.aliyun.tea.TeaModel;
import com.aliyun.tea.Validation;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Linlan
 */
@Getter
@Setter
public class AlipaySystemUserInfoShareResponse extends TeaModel {
    @NameInMap("http_body")
    @Validation(required = true)
    public String httpBody;

    @NameInMap("code")
    @Validation(required = true)
    public String code;

    @NameInMap("msg")
    @Validation(required = true)
    public String msg;

    @NameInMap("sub_code")
    @Validation(required = true)
    public String subCode;

    @NameInMap("sub_msg")
    @Validation(required = true)
    public String subMsg;

    @NameInMap("user_id")
    @Validation(required = true)
    public String userId;

    @NameInMap("avatar")
    @Validation(required = true)
    public String avatar;

    @NameInMap("city")
    @Validation(required = true)
    public Long   city;

    @NameInMap("nick_name")
    @Validation(required = true)
    public String nickName;

    @NameInMap("province")
    @Validation(required = true)
    public Long   province;

    @NameInMap("gender")
    @Validation(required = true)
    public Long   gender;

}
