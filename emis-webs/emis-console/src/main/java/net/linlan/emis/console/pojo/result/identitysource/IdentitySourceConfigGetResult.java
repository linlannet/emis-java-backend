/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.pojo.result.identitysource;

// import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import net.linlan.emis.idsource.config.JobConfig;
import net.linlan.emis.idsource.config.StrategyConfig;

/**
 * 获取身份源配置
 *
 * @author Linlan
 */
@Data
@Schema(description = "获取身份源配置")
public class IdentitySourceConfigGetResult implements Serializable {
    //@Serial
    private static final long serialVersionUID = -1440230086940289961L;

    /**
     * ID
     */
    @Parameter(description = "ID")
    private String            id;

    /**
     * 是否已配置
     */
    @Parameter(description = "是否已配置")
    private Boolean           configured;

    /**
     * 基础配置
     */
    @Parameter(description = "基础配置")
    private Object            basicConfig;

    /**
     * 策略配置
     */
    @Parameter(description = "策略配置")
    private StrategyConfig    strategyConfig;

    /**
     * 任务配置
     */
    @Parameter(description = "任务配置")
    private JobConfig         jobConfig;
}
