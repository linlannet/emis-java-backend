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
package net.linlan.emis.protocol.form.model;

import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import net.linlan.emis.protocol.base.AbstractProtocolConfig;
import net.linlan.emis.protocol.constant.FormEncryptType;
import net.linlan.emis.protocol.constant.FormSubmitType;
import net.linlan.emis.protocol.entity.ProtocolFormConfig;

/**
 * Form 协议配置
 *
 * @author Linlan
 * Created on 2022/8/28 21:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class FormProtocolConfig extends AbstractProtocolConfig {

    //@Serial
    private static final long                   serialVersionUID = -3671812647788723766L;

    /**
     * 登录URL
     */
    private String                              loginUrl;

    /**
     * 登录名属性名称
     */
    private String                              usernameField;

    /**
     * 登录密码属性名称
     */
    private String                              passwordField;

    /**
     * 用户名加密类型
     */
    private FormEncryptType                     usernameEncryptType;

    /**
     * 用户名加密秘钥
     */
    private String                              usernameEncryptKey;

    /**
     * 登录密码加密类型
     */
    private FormEncryptType                     passwordEncryptType;

    /**
     * 登录密码加密秘钥
     */
    private String                              passwordEncryptKey;

    /**
     * 登录提交方式
     */
    private FormSubmitType                      submitType;

    /**
     * 登录其他信息
     */
    private List<ProtocolFormConfig.OtherField> otherField;

    /**
     * 是否配置
     */
    private Boolean                             configured;

    public List<ProtocolFormConfig.OtherField> getOtherField() {
        return CollectionUtils.isEmpty(otherField) ? Collections.emptyList() : otherField;
    }
}
