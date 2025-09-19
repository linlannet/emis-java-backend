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

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.alibaba.fastjson2.JSONObject;

import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.support.security.password.PasswordGenerator;
import net.linlan.utils.http.HttpResponseUtils;

/**
 * 生成一次性密码
 * @author Linlan
 */
@Slf4j
@Validated
@Tag(name = "动态密码生成端点")
@Component
@WebServlet(value = "/api/V1/password/generate")
public class PasswordGenerateEndpoint extends HttpServlet {

    @Resource
    private PasswordGenerator passwordGenerator;

    /**
     * 生成密码
     */
    @Operation(summary = "生成一次性密码")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String password = passwordGenerator.generatePassword();
        //其他信息
        ResponseResult<String> build = ResponseResult.ok().setResultData(password);
        HttpResponseUtils.flushResponse(resp, JSONObject.toJSONString(build));
    }

}
