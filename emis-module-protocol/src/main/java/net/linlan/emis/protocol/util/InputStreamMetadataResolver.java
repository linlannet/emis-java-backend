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
package net.linlan.emis.protocol.util;

import java.io.InputStream;

import javax.annotation.Nonnull;

import org.opensaml.saml.metadata.resolver.impl.AbstractReloadingMetadataResolver;

import net.linlan.emis.support.utils.Md5Utils;
import net.shibboleth.utilities.java.support.resolver.ResolverException;

/**
 * InputStreamMetadataResolver
 *
 * @author Linlan
 * Created on  2022/5/30 23:50
 */
public class InputStreamMetadataResolver extends AbstractReloadingMetadataResolver {
    private InputStream inputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStreamMetadataResolver(@Nonnull final InputStream inputStream) {
        super();
        setInputStream(inputStream);
    }

    /**
     * Gets an identifier which may be used to distinguish this metadata in logging statements.
     *
     * @return identifier which may be used to distinguish this metadata in logging statements
     */
    @Override
    protected String getMetadataIdentifier() {
        return Md5Utils.md516(getInputStream().toString());
    }

    /**
     * Fetches metadata from a source.
     *
     * @return the fetched metadata, or null if the metadata is known not to have changed since the last retrieval
     */
    @Override
    protected byte[] fetchMetadata() {
        try {
            return inputstreamToByteArray(inputStream);
        } catch (ResolverException e) {
            throw new RuntimeException(e);
        }
    }
}
