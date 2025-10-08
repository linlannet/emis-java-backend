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
package net.linlan.emis.verify.dingtalk;

import javax.validation.ConstraintViolationException;

import com.aliyun.dingtalkcontact_1_0.models.GetOrgAuthInfoHeaders;
import com.aliyun.dingtalkcontact_1_0.models.GetOrgAuthInfoRequest;
import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.core.StringUtils;
import net.linlan.emis.manage.IdentitySourceValidator;
import net.linlan.emis.manage.exception.ApiCallException;
import net.linlan.emis.support.validation.ValidationUtils;
import net.linlan.emis.verify.dingtalk.client.AbstractDingTalkClient;

/**
 * 钉钉身份源客户端配置验证器
 *
 * @author Linlan
 *
 */
@Slf4j
public class DingTalkValidator implements IdentitySourceValidator<DingTalkConfig> {
    public static final String ACCESS_TOKEN_PERMISSION_DENIED = "Forbidden.AccessDenied.AccessTokenPermissionDenied";

    @Override
    public Boolean validate(DingTalkConfig config) {
        try {
            ValidationUtils.ValidationResult<DingTalkConfig> validationResult = ValidationUtils
                .validateEntity(config);
            if (validationResult.isHasErrors()) {
                log.error("校验钉钉配置失败：{}", validationResult.getMessage());
                throw new ConstraintViolationException(validationResult.getConstraintViolations());
            }
            //获取 AccessToken
            Client client = AbstractDingTalkClient.createClient();
            GetAccessTokenRequest request = new GetAccessTokenRequest()
                .setAppKey(config.getAppKey()).setAppSecret(config.getAppSecret());
            GetAccessTokenResponse accessToken = client.getAccessToken(request);
            //根据 corpId 获取企业信息
            if (StringUtils.isNotBlank(config.getCorpId())) {
                GetOrgAuthInfoHeaders getOrgAuthInfoHeaders = new GetOrgAuthInfoHeaders();
                getOrgAuthInfoHeaders.xAcsDingtalkAccessToken = accessToken.body.getAccessToken();
                GetOrgAuthInfoRequest getOrgAuthInfoRequest = new GetOrgAuthInfoRequest()
                    .setTargetCorpId(config.getCorpId());
                getDingTalkContact().getOrgAuthInfoWithOptions(getOrgAuthInfoRequest,
                    getOrgAuthInfoHeaders, new RuntimeOptions());
            }
            return true;
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code)
                && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("钉钉身份源参数验证发生错误 [CODE: {}, MESSAGE: {}]", err.getCode(), err.getMessage());
                throw new ApiCallException(err.getMessage());
            }
        } catch (Exception exception) {
            TeaException err = new TeaException(exception.getMessage(), exception);
            if (!com.aliyun.teautil.Common.empty(err.code)
                && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("钉钉身份源参数验证发生错误 [CODE: {}, MESSAGE: {}]", err.getCode(), err.getMessage());
                throw new ApiCallException(err.getMessage());
            }
        }
        return false;
    }

    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception Exception
     */
    public static com.aliyun.dingtalkcontact_1_0.Client getDingTalkContact() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkcontact_1_0.Client(config);
    }
}
