/*
 * emis-module-idsource - idsource身份源
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
package net.linlan.emis.verify.feishu;

import javax.validation.ConstraintViolationException;

import org.springframework.http.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.manage.IdentitySourceValidator;
import net.linlan.emis.manage.exception.ApiCallException;
import net.linlan.emis.manage.exception.InvalidClientConfigException;
import net.linlan.emis.support.validation.ValidationUtils;
import net.linlan.emis.verify.feishu.vo.FeishuAccessTokenRequest;
import net.linlan.emis.verify.feishu.vo.FeishuAccessTokenResponse;
import static net.linlan.emis.verify.feishu.FeiShuConstant.APP_ACCESS_TOKEN_URL;

/**
 * 飞书身份源客户端配置验证器
 *
 * @author Linlan
 *
 */
@Slf4j
public class FeiShuValidator implements

                             IdentitySourceValidator<FeiShuConfig> {
    protected RestOperations restOperations;

    public FeiShuValidator() {
        this.restOperations = new RestTemplate();
    }

    @Override
    public Boolean validate(FeiShuConfig config) throws InvalidClientConfigException {
        try {
            ValidationUtils.ValidationResult<FeiShuConfig> validationResult = ValidationUtils
                .validateEntity(config);
            if (validationResult.isHasErrors()) {
                log.error("校验飞书配置失败：{}", validationResult.getMessage());
                throw new ConstraintViolationException(validationResult.getConstraintViolations());
            }

            FeishuAccessTokenRequest request = new FeishuAccessTokenRequest(config.getAppId(),
                config.getAppSecret());
            FeishuAccessTokenResponse response = postToken(request);
            if (response.getCode() != 0) {
                throw new ApiCallException(response.getMsg());
            }
            return true;
        } catch (Exception exception) {
            log.error("飞书身份源参数验证发生错误 [MESSAGE: {}]", exception.getMessage());
            throw new ApiCallException(exception.getMessage());
        }
    }

    /**
     * 获取token
     * @param json {@link Object}
     * @return {@link FeishuAccessTokenResponse}
     */
    private FeishuAccessTokenResponse postToken(Object json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(JSON.toJSONString(json), headers);
        ResponseEntity<FeishuAccessTokenResponse> response = restOperations.exchange(
            APP_ACCESS_TOKEN_URL, HttpMethod.POST, requestEntity, FeishuAccessTokenResponse.class);
        return response.getBody();
    }

}
