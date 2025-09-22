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
package net.linlan.emis.audit.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Maps;

import net.linlan.commons.core.RequestIdUtils;
import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.entity.*;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.support.security.authentication.CustomWebAuthenticationDetails;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.support.userdetails.geo.GeoLocationParser;
import net.linlan.emis.support.userdetails.geo.GeoLocationProvider;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import net.linlan.frame.comm.constant.AdminType;
import net.linlan.utils.http.HttpContextUtils;
import net.linlan.utils.ip.IPUtils;
import static net.linlan.emis.core.logger.LogAspect.replaceBlank;

/**
 * 发布审计事件
 *
 * @author Linlan
 */
@Component
public class AuditEventPublish {

    /**
     * 发布 审计事件
     *
     * @param eventType {@link EventType}
     */
    public void publish(EventType eventType, String content, EventStatus eventStatus) {
        //@formatter:off
        //封装操作事件
        Event event = Event.builder()
                .type(eventType)
                .time(LocalDateTime.now())
                .content(content)
                .status(eventStatus).build();
        //封装地理位置
        GeoLocation geoLocationModal = getGeoLocation();
        //封装用户代理
        UserAgent userAgent = getUserAgent();
        //封装操作人
        Actor actor = getActor();
        //Publish AuditEvent
        applicationEventPublisher.publishEvent(new AuditEvent(RequestIdUtils.get(), HttpContextUtils.getSession().getId(),
                actor, event, userAgent, geoLocationModal, null));
        //@formatter:on
    }

    /**
     * 发布 审计事件
     *
     * @param eventType {@link EventType}
     */
    public void publish(EventType eventType, Authentication authentication, EventStatus eventStatus,
                        List<Target> targets) {
        //@formatter:off
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
//        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        //封装操作事件
        Event event = Event.builder()
                .type(eventType)
                .time(LocalDateTime.now())
                .status(eventStatus).build();
        if (authentication.getPrincipal() instanceof UserDetails){
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            String username = principal.getUsername();
            Map<String,String> content= Maps.newConcurrentMap();
//            content.put("auth_type", principal.getAuthType() == null ? "" : principal.getAuthType());
            content.put("auth_type", details.getAuthenticationProvider().getType());
            content.put("desc", username+"："+event.getType().getDesc());
            event.setContent(JSONObject.toJSONString(content));
        }
        //封装地理位置
        GeoLocation geoLocationModal = getGeoLocation(authentication);
        //封装用户代理
        UserAgent userAgent = getUserAgent(authentication);
        //封装操作人
        Actor actor = getActor(authentication);
        //Publish AuditEvent
        applicationEventPublisher.publishEvent(new AuditEvent(RequestIdUtils.get(), HttpContextUtils.getSession().getId(),
                actor, event, userAgent, geoLocationModal, targets));
        //@formatter:on
    }

    /**
     * 发布 审计事件
     *
     * @param eventType {@link EventType}
     */
    public void publish(EventType eventType, String content, Actor actor, EventStatus eventStatus) {
        //@formatter:off
        //封装操作事件
        Event event = Event.builder()
                .type(eventType)
                .time(LocalDateTime.now())
                .content(content)
                .status(eventStatus).build();
        //封装地理位置
        GeoLocation geoLocationModal = getGeoLocation();
        //封装用户代理
        UserAgent userAgent = getUserAgent();
        //Publish AuditEvent
        applicationEventPublisher.publishEvent(new AuditEvent(RequestIdUtils.get(), HttpContextUtils.getSession().getId(),
                actor, event, userAgent, geoLocationModal, null));
        //@formatter:on
    }

    /**
     * 发布 审计事件
     *
     * @param eventType {@link EventType}
     */
    public void publish(EventType eventType, Map<String, Object> parameters, String content,
                        List<Target> target, String result, EventStatus eventStatus) {
        //@formatter:off
        //封装操作事件
        Event event = Event.builder()
                .type(eventType)
                .time(LocalDateTime.now())
                .status(eventStatus).build();
        if (!Objects.isNull(parameters)){
            try {
                event.setParam(replaceBlank(JSONObject.toJSONString(parameters)));
            } catch (Exception e) {
                event.setParam(parameters.toString());
            }
        }
        //描述
        if (StringUtils.isNotBlank(content)){
            event.setContent(content);
        }
        //事件结果
        if (StringUtils.isNotBlank(result)){
            event.setResult(result);
        }
        //封装地理位置
        GeoLocation geoLocationModal = getGeoLocation();
        //封装用户代理
        UserAgent userAgent = getUserAgent();
        //封装操作人
        Actor actor = getActor();
        //Publish AuditEvent
        applicationEventPublisher.publishEvent(new AuditEvent(RequestIdUtils.get(), HttpContextUtils.getSession().getId(),
                actor, event, userAgent, geoLocationModal, target));
        //@formatter:on
    }

    /**
     * 发布 审计事件
     *
     * @param eventType {@link EventType}
     */
    public void publish(EventType eventType, List<Target> target, String result,
                        EventStatus eventStatus) {
        //@formatter:off
        //封装操作事件
        Event event = Event.builder()
                .type(eventType)
                .time(LocalDateTime.now())
                .status(eventStatus).build();
        //事件结果
        event.setResult(result);
        //封装地理位置
        GeoLocation geoLocationModal = getGeoLocation();
        //封装用户代理
        UserAgent userAgent = getUserAgent();
        //封装操作人
        Actor actor = getActor();
        //Publish AuditEvent
        applicationEventPublisher.publishEvent(new AuditEvent(RequestIdUtils.get(), HttpContextUtils.getSession().getId(),
                actor, event, userAgent, geoLocationModal, target));
        //@formatter:on
    }

    /**
     * 封装操作者
     * @return {@link Actor}
     */
    public static Actor getActor() {
        //@formatter:off
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Actor actor = Actor.builder()
                .id(getActorId(authentication))
                .type(getActorType(authentication))
                .build();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails){
            actor.setAuthType(((UserDetails) principal).getAuthType());
        }
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
        if (ObjectUtils.isNotEmpty(details.getAuthenticationProvider())) {
            actor.setAuthType(details.getAuthenticationProvider().getType());
        }
        return actor;
        //@formatter:on
    }

    /**
     * 封装操作者
     * @param authentication {@link Authentication}
     * @return {@link Actor}
     */
    private Actor getActor(Authentication authentication) {
        //@formatter:off
        Actor actor = Actor.builder()
                .id(getActorId(authentication))
                .type(getActorType(authentication))
                .build();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails){
            actor.setAuthType(((UserDetails) principal).getAuthType());
        }
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
        if (ObjectUtils.isNotEmpty(details.getAuthenticationProvider())) {
            actor.setAuthType(details.getAuthenticationProvider().getType());
        }
        return actor;
        //@formatter:on
    }

    private static Long getActorId(Authentication authentication) {
        //@formatter:off
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getAdminId();
        }
        if (principal instanceof Long) {
            return (Long) principal;
        }
        return null;
        //@formatter:on
    }

    /**
     * 获取行动者类型
     *
     * @param authentication {@link Authentication}
     * @return {@link AdminType}
     */
    private static String getActorType(Authentication authentication) {
        //@formatter:off
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return  ((UserDetails) principal).getUserType();
        }
        return null;
        //@formatter:on
    }

    /**
     * 获取用户代理
     *
     * @return {@link UserAgent}
     */
    private UserAgent getUserAgent() {
        //@formatter:off
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        net.linlan.emis.support.userdetails.useragent.UserAgent ua = userAgentParser.getUserAgent(request);
        return UserAgent.builder()
                .browser(ua.getBrowser())
                .browserType(ua.getBrowserType())
                .browserMajorVersion(ua.getBrowserMajorVersion())
                .platform(ua.getPlatform())
                .platformVersion(ua.getPlatformVersion())
                .deviceType(ua.getDeviceType())
                .build();
        //@formatter:on
    }

    /**
     * 获取地理位置
     *
     * @return {@link GeoLocation}
     */
    private GeoLocation getGeoLocation() {
        //@formatter:off
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String ip = IPUtils.getIpAddr(request);
        net.linlan.emis.support.userdetails.geo.GeoLocation geoLocation = geoLocationParser.getGeoLocation(ip);
        if (Objects.isNull(geoLocation)){
            return null;
        }
        if (IPUtils.internalIp(ip)){
            return GeoLocation.builder()
                    .ip(geoLocation.getIp())
                    .provider(GeoLocationProvider.fromString(geoLocation.getProvider()))
                    .build();
        }
        GeoPoint geoPoint = null;
        if (!Objects.isNull(geoLocation.getLatitude()) && !Objects.isNull(geoLocation.getLongitude())) {
            geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());
        }
        return  GeoLocation.builder()
                .ip(ip)
                .continentCode(geoLocation.getContinentCode())
                .continentName(geoLocation.getContinentName())
                .countryCode(geoLocation.getCountryCode())
                .countryName(geoLocation.getCountryName())
                .provinceCode(geoLocation.getProvinceCode())
                .provinceName(geoLocation.getProvinceName())
                .cityCode(geoLocation.getCityCode())
                .cityName(geoLocation.getCityName())
                .point(geoPoint)
                .provider(GeoLocationProvider.fromString(geoLocation.getProvider()))
                .build();
        //@formatter:on
    }

    /**
     * 获取用户代理
     *
     * @return {@link UserAgent}
     */
    private UserAgent getUserAgent(Authentication authentication) {
        //@formatter:off
//        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
//        return getUserAgent();

        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
        net.linlan.emis.support.userdetails.useragent.UserAgent userAgent = details.getUserAgent();
        if (Objects.isNull(userAgent)){
            return getUserAgent();
        }
        return UserAgent.builder()
                .browser(userAgent.getBrowser())
                .browserType(userAgent.getBrowserType())
                .browserMajorVersion(userAgent.getBrowserMajorVersion())
                .platform(userAgent.getPlatform())
                .platformVersion(userAgent.getPlatformVersion())
                .deviceType(userAgent.getDeviceType())
                .build();
        //@formatter:on
    }

    /**
     * 获取地理位置
     *
     * @return {@link GeoLocation}
     */
    private GeoLocation getGeoLocation(Authentication authentication) {
        //@formatter:off
//        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
//        return getGeoLocation();

        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
        net.linlan.emis.support.userdetails.geo.GeoLocation geoLocation = details.getGeoLocation();
        if (Objects.isNull(geoLocation)){
            return getGeoLocation();
        }
        GeoPoint geoPoint = null;
        if (!Objects.isNull(geoLocation.getLatitude()) && !Objects.isNull(geoLocation.getLongitude())) {
            geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());
        }
        return  GeoLocation.builder()
                .ip(geoLocation.getIp())
                .continentCode(geoLocation.getContinentCode())
                .continentName(geoLocation.getContinentName())
                .countryCode(geoLocation.getCountryCode())
                .countryName(geoLocation.getCountryName())
                .provinceCode(geoLocation.getProvinceCode())
                .provinceName(geoLocation.getProvinceName())
                .cityCode(geoLocation.getCityCode())
                .cityName(geoLocation.getCityName())
                .point(geoPoint)
                .provider(GeoLocationProvider.fromString(geoLocation.getProvider()))
                .build();
        //@formatter:on
    }

    /**
     * ApplicationEventPublisher
     */
    private final ApplicationEventPublisher applicationEventPublisher;
    /**
     * 地理位置
     */
    private final GeoLocationParser         geoLocationParser;
    /**
     * UserAgentParser
     */
    private final UserAgentParser           userAgentParser;

    public AuditEventPublish(ApplicationEventPublisher applicationEventPublisher,
                             GeoLocationParser geoLocationParser, UserAgentParser userAgentParser) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.geoLocationParser = geoLocationParser;
        this.userAgentParser = userAgentParser;
    }

}
