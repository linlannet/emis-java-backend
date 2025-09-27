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
package net.linlan.emis.core.endpoint;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Builder;
import lombok.Data;

// import net.linlan.emis.support.context.ServletContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.commons.core.annotation.PlatLog;
import net.linlan.emis.support.manage.enums.SecretType;
import net.linlan.emis.support.utils.AesUtils;
import net.linlan.utils.http.HttpContextUtils;

/**
 * 获取加密key
 *
 * @author Linlan
 */
@Validated
@Tag(name = "加密key管理")
@RestController
@RequestMapping("/api/V1/public")
public class PublicSecretEndpoint {

    /**
     * 获取加密key
     * @param type 公钥类型
     * @return  当前进程的加密key
     */
    @Validated
    @Operation(summary = "获取加密key")
    @PlatLog("获取加密key")
    @GetMapping("secret")
    public ResponseResult<PublicSecretResult> getPublicSecret(@RequestParam(value = "type", required = false) @NotNull(message = "秘钥类型不能为空") SecretType type) {
        //调用工具类生成秘钥
        String key = AesUtils.generateKey();
        HttpContextUtils.getSession().setAttribute(type.getKey(), key);
        return ResponseResult.ok(PublicSecretResult.builder().secret(key).build());
    }

    @Data
    @Builder
    public static class PublicSecretResult implements Serializable {

        private static final long serialVersionUID = -8631010419770301770L;

        /**
         * 秘钥
         */
        private String            secret;

    }

}
