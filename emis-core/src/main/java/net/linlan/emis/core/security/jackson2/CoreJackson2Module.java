/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.security.jackson2;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import net.linlan.emis.core.security.authentication.IdpAuthentication;
import net.linlan.emis.core.security.authentication.SmsAuthentication;
import net.linlan.emis.core.security.mfa.MfaAuthentication;
import net.linlan.emis.core.security.savedredirect.SavedRedirect;
import net.linlan.emis.support.security.authentication.CustomAuthenticationProvider;
import net.linlan.emis.support.security.authentication.CustomAuthenticationProviderMixin;
import net.linlan.emis.support.security.authentication.CustomWebAuthenticationDetails;
import net.linlan.emis.support.security.authentication.CustomWebAuthenticationDetailsMixin;
import net.linlan.emis.support.userdetails.*;
import net.linlan.emis.support.userdetails.geo.GeoLocation;
import net.linlan.emis.support.userdetails.geo.GeoLocationMixin;
import net.linlan.emis.support.userdetails.geo.GeoLocationProvider;
import net.linlan.emis.support.userdetails.geo.GeoLocationProviderMixin;
import net.linlan.emis.support.userdetails.useragent.UserAgent;
import net.linlan.emis.support.userdetails.useragent.UserAgentMixin;

/**
 *
 * @author Linlan
 */
public class CoreJackson2Module extends SimpleModule {
    public CoreJackson2Module() {
        super(CoreJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(UserDetails.class, UserDetailsMixin.class);
        context.setMixInAnnotations(SavedRedirect.class, SavedRedirectMixin.class);
        context.setMixInAnnotations(IdpAuthentication.class, IdpAuthenticationTokenMixin.class);
        context.setMixInAnnotations(MfaAuthentication.class, MfaAuthenticationTokenMixin.class);
        context.setMixInAnnotations(SmsAuthentication.class, SmsAuthenticationTokenMixin.class);
        //20250814 添加 Custom定义，用于Jackson反序列化
        context.setMixInAnnotations(CustomWebAuthenticationDetails.class,
            CustomWebAuthenticationDetailsMixin.class);
        context.setMixInAnnotations(CustomAuthenticationProvider.class,
            CustomAuthenticationProviderMixin.class);
        context.setMixInAnnotations(GeoLocation.class, GeoLocationMixin.class);
        context.setMixInAnnotations(GeoLocationProvider.class, GeoLocationProviderMixin.class);
        context.setMixInAnnotations(UserAgent.class, UserAgentMixin.class);
        //20250816 添加 UserDetails内用到的类，用于Jackson反序列化
        context.setMixInAnnotations(Organization.class, OrganizationMixin.class);
        context.setMixInAnnotations(OrganizationType.class, OrganizationTypeMixin.class);
        context.setMixInAnnotations(Group.class, GroupMixin.class);
        context.setMixInAnnotations(Application.class, ApplicationMixin.class);
        context.setMixInAnnotations(ApplicationGroup.class, ApplicationGroupMixin.class);

    }

}
