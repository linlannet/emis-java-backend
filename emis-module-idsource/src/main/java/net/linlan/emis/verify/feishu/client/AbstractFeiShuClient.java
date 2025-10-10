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
package net.linlan.emis.verify.feishu.client;

import java.util.concurrent.TimeUnit;

import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.emis.manage.client.AbstractIdentitySourceClient;
import net.linlan.emis.manage.exception.ApiCallException;
import net.linlan.emis.verify.feishu.FeiShuConfig;
import net.linlan.emis.verify.feishu.vo.FeishuAccessTokenRequest;
import net.linlan.emis.verify.feishu.vo.FeishuAccessTokenResponse;
import static net.linlan.emis.verify.feishu.FeiShuConstant.ACCESS_TOKEN_URL;
import static net.linlan.emis.verify.feishu.FeiShuConstant.LOGGER_NAME;

/**
 * AbstractDingTalkDataProcessor
 *
 * @author Linlan
 *
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j(topic = LOGGER_NAME)
public abstract class AbstractFeiShuClient extends AbstractIdentitySourceClient<FeiShuConfig> {

    /**
     * 缓存
     */
    private Cache<String, String> cache;

    protected RestOperations      restOperations;

    protected AbstractFeiShuClient(FeiShuConfig config) {
        super(config);
        this.restOperations = new RestTemplate();
        String token = getAccessToken();
        cache.put(ACCESS_KEY, token);
    }

    /**
     * 获取访问令牌
     *
     * @return {@link String}
     */
    protected String getAccessToken() {
        if (ObjectUtils.isNotEmpty(cache)
            && ObjectUtils.isNotEmpty(cache.getIfPresent(ACCESS_KEY))) {
            return cache.getIfPresent(ACCESS_KEY);
        }
        FeishuAccessTokenRequest request = new FeishuAccessTokenRequest(getConfig().getAppId(),
            getConfig().getAppSecret());
        log.debug("获取飞书 Access Token 入参: {}", JSON.toJSONString(request));
        FeishuAccessTokenResponse response = postToken(request);
        Assert.notNull(response, "获取Token返回结果为空");
        log.debug("获取飞书 Access Token 返回: {}", JSON.toJSONString(response));
        if (response.isSuccess()) {
            cache = CacheBuilder.newBuilder()
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .expireAfterWrite(response.getExpire(), TimeUnit.SECONDS).build();
            cache.put(ACCESS_KEY, response.getTenantAccessToken());
            return cache.getIfPresent(ACCESS_KEY);
        }
        log.error("获取飞书 Access Token 失败: {}", JSON.toJSONString(response));
        throw new ApiCallException("获取飞书 Access Token 失败");
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
            ACCESS_TOKEN_URL, HttpMethod.POST, requestEntity, FeishuAccessTokenResponse.class);
        return response.getBody();
    }

    /**
     * 设置 RestOperations
     * @param restOperations    请求操作
     */
    public void setRestOperations(RestOperations restOperations) {
        Assert.notNull(restOperations, "restOperations cannot be null");
        this.restOperations = restOperations;
    }
}
