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
package net.linlan.emis.verify.dingtalk.client;

import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson2.JSON;
import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.emis.manage.client.AbstractIdentitySourceClient;
import net.linlan.emis.manage.exception.ApiCallException;
import net.linlan.emis.verify.dingtalk.DingTalkConfig;
import static net.linlan.emis.verify.dingtalk.DingTalkConstants.LOGGER_NAME;

/**
 * AbstractDingTalkDataProcessor
 *
 * @author Linlan
 *
 */
@Slf4j(topic = LOGGER_NAME)
public abstract class AbstractDingTalkClient extends AbstractIdentitySourceClient<DingTalkConfig> {
    /**
     * 缓存
     */
    private Cache<String, String> cache;

    protected AbstractDingTalkClient(DingTalkConfig config) {
        super(config);
        String token = getAccessToken();
        cache.put(ACCESS_KEY, token);
    }

    /**
     * 获取访问令牌
     *
     * @return {@link String}
     */
    protected String getAccessToken() {
        try {
            DingTalkConfig config = getConfig();
            if (ObjectUtils.isNotEmpty(cache)
                && ObjectUtils.isNotEmpty(cache.getIfPresent(ACCESS_KEY))) {
                return cache.getIfPresent(ACCESS_KEY);
            }
            Client client = createClient();
            GetAccessTokenRequest request = new GetAccessTokenRequest()
                .setAppKey(config.getAppKey()).setAppSecret(config.getAppSecret());
            log.debug("获取钉钉 Access Token 入参: {}", JSON.toJSONString(request));
            GetAccessTokenResponse accessToken = client.getAccessToken(request);
            log.debug("获取钉钉 Access Token 返回: {}", JSON.toJSONString(accessToken));
            GetAccessTokenResponseBody body = accessToken.getBody();
            cache = CacheBuilder.newBuilder()
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .expireAfterWrite(body.getExpireIn(), TimeUnit.SECONDS).build();
            cache.put(ACCESS_KEY, body.getAccessToken());
            return cache.getIfPresent(ACCESS_KEY);
        } catch (Exception e) {
            log.error("获取钉钉 Access Token 失败: {}", e.getMessage());
            throw new ApiCallException("获取钉钉 Access Token 失败");
        }
    }

    /**
     * 使用 Token 初始化账号Client
     * @return 客户端连接抽象类
     */
    public static Client createClient() {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        try {
            return new Client(config);
        } catch (Exception e) {
            log.error("钉钉获取Client异常：{}", e.getMessage());
            throw new ApiCallException(e.getMessage());
        }
    }

}
