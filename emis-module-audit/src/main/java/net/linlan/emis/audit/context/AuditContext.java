/*
 * emis-java-backend - 基于JAVA开发的企业级多身份管理系统后端工程，依托EFRAME框架开发，基于Springboot，采用支持 JAVA 1.8最高版本的framework、security，后端数据访问采用Mybatis框架，支持多种数据库一套代码适配，提供脚手架可快速生成Mapper等，在仓库功能上，借鉴主流的SSO和IAM框架，实现统一用户管理。前端提供VUE框架和REACT框架的控制台管理工程、企业个人门户工程和移动端应用扩展。
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
package net.linlan.emis.audit.context;

import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.collect.Lists;

import net.linlan.emis.audit.entity.Target;

/**
 * AuditContext
 *
 * @author Linlan
 */
public class AuditContext {

    /**
     * 内容
     */
    private static final TransmittableThreadLocal<String>              CONTENT         = new TransmittableThreadLocal<>();

    /**
     * Authentication
     */
    private static final TransmittableThreadLocal<Authentication>      AUTHENTICATION  = new TransmittableThreadLocal<>();

    /**
     * 目标对象
     */
    private static final TransmittableThreadLocal<List<Target>>        TARGET_LIST     = new TransmittableThreadLocal<>();

    /**
     * 额外数据
     */
    private static final TransmittableThreadLocal<Map<String, Object>> ADDITIONAL_DATA = new TransmittableThreadLocal<>();

    /**
     * Get Content
     *
     * @return {@link Object}
     */
    public static String getContent() {
        return CONTENT.get();
    }

    /**
     * Set Content
     */
    public static void setContent(String content) {
        CONTENT.set(content);
    }

    /**
     * Remove Content
     */
    public static void removeContent() {
        CONTENT.remove();
    }

    /**
     * Get Additional Content
     *
     * @return {@link Object}
     */
    public static Object getAdditionalData(String key) {
        return getAdditionalData().get(key);
    }

    /**
     * Get
     *
     * @return {@link Map}
     */
    public static Map<String, Object> getAdditionalData() {
        Map<String, Object> values = ADDITIONAL_DATA.get();
        if (CollectionUtils.isEmpty(values)) {
            ADDITIONAL_DATA.set(new HashMap<>(16));
        }
        return values;
    }

    /**
     * PUT
     *
     * @param key   {@link String}
     * @param value {@link Object}
     */
    public static void putAdditionalData(String key, Object value) {
        Map<String, Object> values = ADDITIONAL_DATA.get();
        if (CollectionUtils.isEmpty(values)) {
            HashMap<String, Object> map = new HashMap<>(16);
            map.put(key, value);
            ADDITIONAL_DATA.set(map);
            return;
        }
        values.put(key, value);
    }

    /**
     * PUT
     *
     * @param value {@link Map}
     */
    public static void putAdditionalData(Map<String, Object> value) {
        ADDITIONAL_DATA.set(value);
    }

    /**
     * Get Authentication
     *
     * @return {@link Authentication}
     */
    public static Authentication getAuthorization() {
        return AUTHENTICATION.get();
    }

    public static void setAuthorization(Authentication authorization) {
        AUTHENTICATION.set(authorization);
    }

    /**
     * Get Target
     *
     * @return {@link Object}
     */
    public static List<Target> getTarget() {
        return TARGET_LIST.get();
    }

    /**
     * Set Target
     */
    public static void setTarget(Target... target) {
        if (!Objects.isNull(target)) {
            TARGET_LIST.set(Lists.newArrayList(target));
        }
    }

    /**
     * Set Target
     */
    public static void setTarget(List<Target> targets) {
        if (!CollectionUtils.isEmpty(targets)) {
            TARGET_LIST.set(targets);
        }
    }

    /**
     * Add Target
     */
    public static void addTarget(Target target) {
        if (!Objects.isNull(target)) {
            List<Target> targetList = getTarget();
            if (Objects.isNull(targetList)) {
                targetList = new ArrayList<>();
            }
            targetList.add(target);
            TARGET_LIST.set(targetList);
        }
    }

    /**
     * Remove Content
     */
    public static void removeTarget() {
        TARGET_LIST.remove();
    }

    /**
     * Remove Authentication
     */
    public static void removeAuthentication() {
        AUTHENTICATION.remove();
    }

    /**
     * remove
     */
    public static void removeAdditionalData() {
        ADDITIONAL_DATA.remove();
    }

    /**
     * remove
     *
     * @param key {@link String}
     */
    public static void removeAdditionalData(String key) {
        Map<String, Object> values = ADDITIONAL_DATA.get();
        if (!CollectionUtils.isEmpty(values)) {
            values.remove(key);
        }
    }

    /**
     * remove
     */
    public static void removeAuditContext() {
        removeAdditionalData();
        removeContent();
        removeTarget();
        removeAuthentication();
    }

}
