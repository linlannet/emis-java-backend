/*
 * emis-support - support支撑模块
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
package net.linlan.emis.support.constant;

import java.time.format.DateTimeFormatter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * 系统常量
 *
 * @author Linlan
 */
public interface EmisConstants {
    /**
     * 冒号
     */
    String            COLON                               = ":";
    /**
     * API
     */
    String            API_PATH                            = "/api";
    /**
     * API
     */
    String            V1_API_PATH                         = API_PATH + "/V1";
    /**
     * 根节点
     */
    String            ROOT_NODE                           = "root";
    /**
     * SYSTEM_INFO_ENDPOINT
     */
    String            CONTEXT_ENDPOINT                    = V1_API_PATH + "/context";
    /**
     * 文件存储
     */
    String            CONTEXT_ENDPOINT_GROUP_NAME         = "系统信息";

    /**
     * LOCAL
     */
    String            LOCAL                               = "Local Login";
    /**
     * 基本认证
     */
    String            BASIC                               = "Basic";
    /**
     * 社交登录
     */
    String            SOCIAL_SIGN_ON                      = "Social Sign On";
    /**
     * 记住我
     */
    String            REMEMBER_ME                         = "RememberMe";
    /**
     * Desktop
     */
    String            DESKTOP                             = "Desktop";
    /**
     * Kerberos
     */
    String            KERBEROS                            = "Kerberos";
    /**
     * SAML
     */
    String            SAML_TRUST                          = "SAML v2.0 Trust";
    /**
     * MS AD
     */
    String            MS_AD_TRUST                         = "MS AD Trust";
    /**
     * CAS
     */
    String            CAS                                 = "CAS";
    /**
     * WsFederation
     */
    String            WS_FEDERATION                       = "WsFederation";
    /**
     * JWT
     */
    String            JWT                                 = "Jwt";
    /**
     * http 头
     */
    String            HTTP_HEADER                         = "HttpHeader";
    /**
     * DEFAULT_CSRF_COOKIE_NAME
     */
    String            DEFAULT_CSRF_COOKIE_NAME            = "emis-csrf-cookie";
    /**
     * DEFAULT_CSRF_HEADER_NAME
     */
    String            DEFAULT_CSRF_HEADER_NAME            = "emis-csrf";
    /**
     * 登录秘钥
     */
    String            LOGIN_SECRET                        = "LOGIN_SECRET";
    /**
     * 加密常量
     */
    String            ENCRYPT_SECRET                      = "ENCRYPT_SECRET";

    /**
     * BIND MFA 秘钥
     */
    String            BIND_MFA_SECRET                     = "BIND_MFA_SECRET";

    /**
     * 验证码
     */
    String            CAPTCHA_CODE_SESSION                = "LOGIN_CAPTCHA";
    /**
     * session详情
     */
    String            SESSION_DETAILS                     = "SESSION_DETAILS";

    /**
     * SAVED_REQUEST
     */
    String            SAVED_REQUEST                       = "SPRING_SECURITY_SAVED_REQUEST";

    String            DEFAULT_DATE_FORMATTER_PATTERN      = "yyyy-MM-dd";

    String            DEFAULT_DATE_TIME_FORMATTER_PATTERN = "yyyy-MM-dd HH:mm:ss";

    DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER         = DateTimeFormatter
        .ofPattern(DEFAULT_DATE_TIME_FORMATTER_PATTERN);

    /**
     * IP库存放目录
     */
    String            IPADDRESS_FILE_DIRECTORY            = System.getProperty("user.home")
                                                            + "/.linlan/ip/";

    /**
     * IP库压缩包存放目录
     */
    String            IPADDRESS_FILE_TAR                  = IPADDRESS_FILE_DIRECTORY
                                                            + "GeoLite2-City.tar.gz";

    /**
     * IP库存放路径
     */
    String            IPADDRESS_FILE_PATH                 = IPADDRESS_FILE_DIRECTORY
                                                            + "GeoLite2-City.mmdb";

    /**
     * SHA256校验文件存放路径
     */
    String            SHA256_FILE_PATH                    = IPADDRESS_FILE_DIRECTORY
                                                            + "GeoLite2-City.tar.gz.sha256";

    /**
     * 默认管理员用户名
     */
    String            DEFAULT_ADMIN_USERNAME              = "admin";

    /**
     * 初始化授权对象
     */
    Authentication    EMIS_INIT_AUTHENTICATION            = new UsernamePasswordAuthenticationToken(
        "initial", "initial.linlan.net", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));

    /**
     * 默认路径分隔符
     */
    String            PATH_SEPARATOR                      = "/";
    /**
     * 默认用户名
     */
    String            SYSTEM_DEFAULT_USER_NAME            = "admin";
    /**
     * 忘记密码TOKEN
     */
    String            FORGET_PASSWORD_TOKEN_ID            = "token";
}
