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

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;

import net.linlan.emis.manage.core.OtpAuthentication;

/**
 * OtpAuthenticationTokenDeserializer
 *
 * @author Linlan
 */
@SuppressWarnings("DuplicatedCode")
class OtpAuthenticationTokenDeserializer extends JsonDeserializer<OtpAuthentication> {

    private static final TypeReference<List<GrantedAuthority>> GRANTED_AUTHORITY_LIST = new TypeReference<List<GrantedAuthority>>() {
                                                                                      };

    private static final TypeReference<Object>                 OBJECT                 = new TypeReference<Object>() {
                                                                                      };

    @Override
    public OtpAuthentication deserialize(JsonParser jp,
                                         DeserializationContext deserializationContext) throws IOException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        boolean authenticated = readJsonNode(jsonNode, "authenticated").asBoolean();
        String recipient = readJsonNode(jsonNode, "recipient").asText();
        String type = readJsonNode(jsonNode, "type").asText();
        JsonNode principalNode = readJsonNode(jsonNode, "principal");
        Object principal = getPrincipal(mapper, principalNode);
        //权限
        List<GrantedAuthority> authorities = mapper.readValue(
            readJsonNode(jsonNode, "authorities").traverse(mapper), GRANTED_AUTHORITY_LIST);
        OtpAuthentication authentication = (!authenticated)
            ? new OtpAuthentication(principal, recipient, type)
            : new OtpAuthentication(principal, recipient, type, authorities);
        JsonNode detailsNode = readJsonNode(jsonNode, "details");
        if (detailsNode.isNull() || detailsNode.isMissingNode()) {
            authentication.setDetails(null);
        } else {
            Object details = mapper.readValue(detailsNode.toString(), OBJECT);
            authentication.setDetails(details);
        }
        return authentication;
    }

    private Object getPrincipal(ObjectMapper mapper, JsonNode principalNode) throws IOException {
        if (principalNode.isObject()) {
            return mapper.readValue(principalNode.traverse(mapper), Object.class);
        }
        return principalNode.asText();
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

}
