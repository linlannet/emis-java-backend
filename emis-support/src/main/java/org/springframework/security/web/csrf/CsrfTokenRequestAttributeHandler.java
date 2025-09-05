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
package org.springframework.security.web.csrf;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;

/**
 * An implementation of the {@link CsrfTokenRequestHandler} interface that is capable of
 * making the {@link CsrfToken} available as a request attribute and resolving the token
 * value as either a header or parameter value of the request.
 *
 * @author Steve Riesenberg
 * @since 5.8
 */
public class CsrfTokenRequestAttributeHandler implements CsrfTokenRequestHandler {

    private String csrfRequestAttributeName = "_csrf";

    /**
     * The {@link CsrfToken} is available as a request attribute named
     * {@code CsrfToken.class.getName()}. By default, an additional request attribute that
     * is the same as {@link CsrfToken#getParameterName()} is set. This attribute allows
     * overriding the additional attribute.
     * @param csrfRequestAttributeName the name of an additional request attribute with
     * the value of the CsrfToken. Default is {@link CsrfToken#getParameterName()}
     */
    public final void setCsrfRequestAttributeName(String csrfRequestAttributeName) {
        this.csrfRequestAttributeName = csrfRequestAttributeName;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       Supplier<CsrfToken> deferredCsrfToken) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");
        Assert.notNull(deferredCsrfToken, "deferredCsrfToken cannot be null");

        request.setAttribute(HttpServletResponse.class.getName(), response);
        CsrfToken csrfToken = new SupplierCsrfToken(deferredCsrfToken);
        request.setAttribute(CsrfToken.class.getName(), csrfToken);
        String csrfAttrName = (this.csrfRequestAttributeName != null)
            ? this.csrfRequestAttributeName
            : csrfToken.getParameterName();
        request.setAttribute(csrfAttrName, csrfToken);
    }

    private static final class SupplierCsrfToken implements CsrfToken {

        private final Supplier<CsrfToken> csrfTokenSupplier;

        private SupplierCsrfToken(Supplier<CsrfToken> csrfTokenSupplier) {
            this.csrfTokenSupplier = csrfTokenSupplier;
        }

        @Override
        public String getHeaderName() {
            return getDelegate().getHeaderName();
        }

        @Override
        public String getParameterName() {
            return getDelegate().getParameterName();
        }

        @Override
        public String getToken() {
            return getDelegate().getToken();
        }

        private CsrfToken getDelegate() {
            CsrfToken delegate = this.csrfTokenSupplier.get();
            if (delegate == null) {
                throw new IllegalStateException("csrfTokenSupplier returned null delegate");
            }
            return delegate;
        }

    }

}
