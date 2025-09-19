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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class SimpleGrantedAuthorityDeserializer extends JsonDeserializer<SimpleGrantedAuthority> {
    private static final Logger logger = LoggerFactory
        .getLogger(SimpleGrantedAuthorityDeserializer.class);

    @Override
    public SimpleGrantedAuthority deserialize(JsonParser jp,
                                              DeserializationContext ctxt) throws IOException {
        // 处理三种情况：
        // 1. 直接字符串值 "ROLE_ADMIN"
        // 2. 对象格式 {"authority": "ROLE_ADMIN"}
        // 3. 数组中的元素

        JsonToken currentToken = jp.getCurrentToken();
        // 情况1：直接字符串
        if (currentToken == JsonToken.VALUE_STRING) {
            logger.debug("尝试反序列化 SimpleGrantedAuthority，当前令牌: {}", jp.getCurrentToken());
            return new SimpleGrantedAuthority(jp.getText());
        }

        // 情况2：对象格式
        if (currentToken == JsonToken.START_OBJECT) {
            JsonNode node = jp.getCodec().readTree(jp);
            logger.debug("尝试反序列化 SimpleGrantedAuthority，当前令牌: {}", jp.getCurrentToken());
            if (node.has("authority")) {
                return new SimpleGrantedAuthority(node.get("authority").asText());
            }
            if (node.has("role")) { // 兼容其他可能的字段名
                return new SimpleGrantedAuthority(node.get("role").asText());
            }
        }

        // 情况3：值可能是通过树模型传递的
        if (currentToken == JsonToken.VALUE_EMBEDDED_OBJECT
            || currentToken == JsonToken.FIELD_NAME) {
            JsonNode node = jp.getCodec().readTree(jp);
            logger.debug("尝试反序列化 SimpleGrantedAuthority，当前令牌: {}", jp.getCurrentToken());
            String authority = node.asText();
            if (authority != null && !authority.isEmpty()) {
                return new SimpleGrantedAuthority(authority);
            }
        }

        throw new IOException("无法反序列化 SimpleGrantedAuthority: 不支持的格式. 当前令牌: " + currentToken);
    }
}
