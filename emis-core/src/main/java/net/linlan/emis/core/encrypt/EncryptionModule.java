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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author Linlan
 */
public class EncryptionModule extends SimpleModule {

    private final JsonEncryptType serializer;
    private final JsonEncryptType deserializer;

    public EncryptionModule() {
        this.serializer = null;
        this.deserializer = null;
    }

    public EncryptionModule(JsonEncryptType serializer, JsonEncryptType deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @Override
    public void setupModule(SetupContext setupContext) {
        setupContext.addBeanSerializerModifier(new EncryptedSerializerModifier(serializer));
        setupContext.addBeanDeserializerModifier(new EncryptedDeserializerModifier(deserializer));
    }

    public static ObjectMapper serializerEncrypt() {
        return createMapper(JsonEncryptType.ENCRYPT, JsonEncryptType.NONE);
    }

    public static ObjectMapper deserializerEncrypt() {
        return createMapper(JsonEncryptType.NONE, JsonEncryptType.ENCRYPT);
    }

    public static ObjectMapper serializerDecrypt() {
        return createMapper(JsonEncryptType.DECRYPT, JsonEncryptType.NONE);
    }

    public static ObjectMapper deserializerDecrypt() {
        return createMapper(JsonEncryptType.NONE, JsonEncryptType.DECRYPT);
    }

    public static ObjectMapper createMapper(JsonEncryptType serializer,
                                            JsonEncryptType deserializer) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new EncryptionModule(serializer, deserializer));
        return objectMapper;
    }
}
