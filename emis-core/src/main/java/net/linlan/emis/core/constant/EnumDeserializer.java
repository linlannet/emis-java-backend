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
package net.linlan.emis.core.constant;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import net.linlan.commons.core.StringUtils;
import net.linlan.emis.support.manage.enums.ListEnumDeserializer;
import net.linlan.utils.enums.BaseEnumString;

/**
 * EnumDeserializer
 *
 * @author Linlan
 */
public class EnumDeserializer extends JsonDeserializer<BaseEnumString>
                              implements ContextualDeserializer {
    /**
     * 记录枚举字段的类，用于获取其定义的所有枚举值
     */
    private Class<? extends BaseEnumString> propertyClass;

    public EnumDeserializer() {
    }

    public EnumDeserializer(Class<? extends BaseEnumString> propertyClass) {
        this.propertyClass = propertyClass;
    }

    @Override
    public BaseEnumString deserialize(JsonParser jsonParser,
                                      DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        // 调用Class的这个方法，获取枚举类的所有枚举值
        return Arrays.stream(propertyClass.getEnumConstants())
            .filter(e -> Objects.equals(e.getKey(), value)).findAny()
            .orElseThrow(() -> new IllegalArgumentException(
                "No such code of " + propertyClass.getSimpleName()));
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
                                                BeanProperty property) {
        // 获取枚举字段的类型Class
        return new ListEnumDeserializer(
            (Class<? extends BaseEnumString>) property.getType().getRawClass());
    }
}
