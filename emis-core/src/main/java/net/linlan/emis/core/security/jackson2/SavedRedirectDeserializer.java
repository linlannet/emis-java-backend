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
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.linlan.emis.core.security.savedredirect.SavedRedirect;

/**
 * @author Linlan
 */
public class SavedRedirectDeserializer extends JsonDeserializer<SavedRedirect> {

    private static final TypeReference<List<SavedRedirect.Parameter>> SIMPLE_CROSS_SITE_REDIRECT_PARAMETER_LIST = new TypeReference<List<SavedRedirect.Parameter>>() {
    };

    @Override
    public SavedRedirect deserialize(JsonParser jp,
                                     DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        SavedRedirect result = new SavedRedirect();
        result.setMethod(jsonNode.get("method").asText());
        result.setAction(jsonNode.get("action").asText());
        List<SavedRedirect.Parameter> parameters = mapper.convertValue(jsonNode.get("parameters"),
            SIMPLE_CROSS_SITE_REDIRECT_PARAMETER_LIST);
        result.setParameters(parameters);
        return result;
    }

}
