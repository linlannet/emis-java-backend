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
package net.linlan.emis.core.frame;

import org.springframework.lang.Nullable;

import com.google.common.base.Function;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.sys.base.entity.BaseConfigWhole;

/**
 * 设置Vo对象，对BaseConfigWhole进行转换
 *
 * @author Linlan
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class EmisSettingVo {

    /**
     * 配置编号
     */
    private String               id;
    /**
     * 配置名称
     */
    private String               name;
    /**
     * 配置值
     */
    private String               content;
    /**
     * 分类标记
     */
    private String               category;
    /**
     * 是否禁用0否1是
     */
    private Boolean              disabled;
    /**
     * 配置说明
     */
    private String               description;

    /**
     * 数据赋值TO方法，将BaseConfigWhole的input对象赋值给EmisSettingVo
     */
    public static final Function TO = new Function<BaseConfigWhole, EmisSettingVo>() {
                                        @Nullable
                                        @Override
                                        public EmisSettingVo apply(@Nullable BaseConfigWhole input) {
                                            return new EmisSettingVo(input);
                                        }
                                    };

    /**
     * @param input 数据库查询出的entity对象，对VO对象进行赋值，默认的字段为全部字段，可结合实际调整
     */
    public EmisSettingVo(BaseConfigWhole input) {
        this.id = input.getId();
        this.name = input.getCfgKey();
        this.content = input.getCfgValue();
        this.category = input.getCfgCatagory();
        this.disabled = input.getIsDisabled();
        this.description = input.getDescription();
    }

    public static BaseConfigWhole transTo(EmisSettingVo input) {
        BaseConfigWhole result = new BaseConfigWhole();
        if (ObjectUtils.isNotEmpty(input.getId())) {
            result.setId(input.getId());
        }
        result.setCfgKey(input.getName());
        result.setCfgValue(input.getContent());
        if (ObjectUtils.isNotEmpty(input.getDisabled())) {
            result.setIsDisabled(input.getDisabled());
        } else {
            result.setIsDisabled(Boolean.FALSE);
        }
        result.setCfgCatagory(input.getCategory());
        result.setDescription(input.getDescription());
        return result;
    }
}
