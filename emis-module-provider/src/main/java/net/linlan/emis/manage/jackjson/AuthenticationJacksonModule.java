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
package net.linlan.emis.manage.jackjson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

import net.linlan.emis.manage.core.IdentityProviderAuthentication;
import net.linlan.emis.manage.core.IdentityProviderNotBindAuthentication;
import net.linlan.emis.manage.core.IdentityProviderUserDetails;
import net.linlan.emis.manage.core.OtpAuthentication;
import net.linlan.emis.provider.constant.IdentityProviderType;

/**
 *
 * @author Linlan
 * Created on  2023/7/14 21:45
 */
public class AuthenticationJacksonModule extends Module {

    /**
     * Method that returns a display that can be used by Jackson
     * for informational purposes, as well as in associating extensions with
     * module that provides them.
     */
    @Override
    public String getModuleName() {
        return AuthenticationJacksonModule.class.getName();
    }

    /**
     * Method that returns version of this module. Can be used by Jackson for
     * informational purposes.
     */
    @Override
    public Version version() {
        return new Version(1, 0, 0, null, null, null);
    }

    @Override
    public void setupModule(SetupContext context) {
        //@formatter:off
        context.setMixInAnnotations(IdentityProviderAuthentication.class, IdentityProviderAuthenticationTokenMixin.class);
        context.setMixInAnnotations(IdentityProviderNotBindAuthentication.class, IdentityProviderNotBindAuthenticationTokenMixin.class);
        context.setMixInAnnotations(OtpAuthentication.class, OtpAuthenticationTokenMixin.class);
        context.setMixInAnnotations(IdentityProviderUserDetails.class, IdentityProviderUserDetailsMixin.class);
        context.setMixInAnnotations(IdentityProviderType.class, IdentityProviderTypeMixin.class);
        //@formatter:on
    }

}
