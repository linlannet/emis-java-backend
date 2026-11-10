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
package net.linlan.emis.verify.wechatwork;

import java.util.Objects;

import javax.validation.ConstraintViolationException;

import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.manage.IdentitySourceValidator;
import net.linlan.emis.manage.exception.ApiCallException;
import net.linlan.emis.manage.exception.InvalidClientConfigException;
import net.linlan.emis.support.validation.ValidationUtils;
import net.linlan.emis.verify.wechatwork.vo.WeChatWorkAccessTokenResponse;

/**
 * 企业微信身份源客户端配置验证器
 *
 * @author Linlan
 *
 */
@Slf4j
public class WeChatWorkValidator implements IdentitySourceValidator<WeChatWorkConfig> {

    protected RestOperations restOperations;

    public WeChatWorkValidator() {
        restOperations = new RestTemplate();
    }

    @Override
    public Boolean validate(WeChatWorkConfig config) throws InvalidClientConfigException {
        try {
            ValidationUtils.ValidationResult<WeChatWorkConfig> validationResult = ValidationUtils
                .validateEntity(config);
            if (validationResult.isHasErrors()) {
                log.error("校验企业微信配置失败:{}", validationResult.getMessage());
                throw new ConstraintViolationException(validationResult.getConstraintViolations());
            }

            String queryParams = "?corpid=" + config.getCorpId() + "&corpsecret="
                                 + config.getSecret();
            WeChatWorkAccessTokenResponse response = restOperations.getForObject(
                WeChatWorkConstant.ACCESS_TOKEN_URL + queryParams,
                WeChatWorkAccessTokenResponse.class);

            if (Objects.nonNull(response)) {
                if (response.getErrCode() != 0) {
                    throw new ApiCallException(response.getErrMsg());
                }
                return true;
            }
        } catch (Exception exception) {
            log.error("企业微信身份源参数验证发生错误 [MESSAGE: {}]", exception.getMessage());
            throw new ApiCallException(exception.getMessage());
        }

        return false;
    }
}
