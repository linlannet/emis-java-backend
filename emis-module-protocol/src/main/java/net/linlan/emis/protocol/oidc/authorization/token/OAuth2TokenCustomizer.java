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
package net.linlan.emis.protocol.oidc.authorization.token;

import java.security.Principal;
import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

import lombok.Setter;

import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserInfoService;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.*;

/**
 * 令牌定制器
 *
 * @author Linlan
 * Created on 2022/12/26 21:44
 */
@Setter
@SuppressWarnings({ "AlibabaClassNamingShouldBeCamel" })
public class OAuth2TokenCustomizer implements
                                   org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
        //@formatter:off
        Set<String> authorizedScopes = context.getAuthorizedScopes();
        if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
            OAuth2Authorization authorization = context.getAuthorization();
            if (!Objects.isNull(authorization)){
                Authentication authentication = authorization.getAttribute(Principal.class.getName());
                if (!Objects.isNull(authentication)){
                    //存在 UserInfoService 实时查询用户信息
                    if (!Objects.isNull(userInfoService)){
                        UserDetails principal = (UserDetails) authentication.getPrincipal();
                        UserInfo userInfo = userInfoService.findById(principal.getId());
                        if (!Objects.isNull(userInfo)) {
                            BeanUtils.merge(userInfo.toUserDetails(principal.getAuthorities()), principal);
                        }
                    }
                    UserDetails user = (UserDetails) authentication.getPrincipal();
                        boolean hasCustomClaims=false;
                        OidcUserInfo.Builder userInfoBuilder = OidcUserInfo.builder();
                        // Customize headers/claims for id_token
                        // 用户主体
                        userInfoBuilder.subject(user.getId());
                        if (authorizedScopes.contains(EMAIL)) {
                            hasCustomClaims=true;
                            userInfoBuilder.email(Objects.toString(user.getEmail(), ""));
                            userInfoBuilder.emailVerified(!Objects.isNull(user.getEmailVerified()) && user.getEmailVerified());
                        }
                        if (authorizedScopes.contains(PHONE)) {
                            hasCustomClaims=true;
                            userInfoBuilder.phoneNumber(Objects.toString(user.getMobile(), ""));
                            userInfoBuilder.phoneNumberVerified(user.getMobileVerified() ? user.getMobile() : "");
                        }
                        if (authorizedScopes.contains(PROFILE)) {
                            hasCustomClaims=true;
                            //用户名
                            userInfoBuilder.preferredUsername(user.getUsername());
                            //昵称
                            userInfoBuilder.nickname(Objects.toString(user.getNickName(), ""));
                            //名称
                            userInfoBuilder.name(Objects.toString(user.getFullName(), ""));
                            userInfoBuilder.picture(user.getAvatarUrl());
                            userInfoBuilder.updatedAt(user.getLastTime() == null ? null : new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(user.getLastTime()));
                        }
                        if (hasCustomClaims){
                            context.getClaims().claims(claims ->
                                    claims.putAll(userInfoBuilder.build().getClaims()));
                        }
                }
            }
        }
        //@formatter:on
    }

    private UserInfoService userInfoService;

}
