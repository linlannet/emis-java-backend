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
package net.linlan.emis.core.encrypt;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author Linlan
 */
public class EncryptedJsonDeserializer extends JsonDeserializer<Object> {

    private final JsonEncryptType deserializerJsonEncryptType;

    public EncryptedJsonDeserializer(JsonEncryptType deserializer) {
        this.deserializerJsonEncryptType = deserializer;
    }

    @Override
    public Object deserialize(final JsonParser parser,
                              final DeserializationContext context) throws IOException {
        String value = parser.getValueAsString();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        if (JsonEncryptType.ENCRYPT == deserializerJsonEncryptType) {
            return EncryptContextHelper.encrypt(value);
        } else if (JsonEncryptType.DECRYPT == deserializerJsonEncryptType) {
            return EncryptContextHelper.decrypt(value);
        }
        return value;
    }
}
