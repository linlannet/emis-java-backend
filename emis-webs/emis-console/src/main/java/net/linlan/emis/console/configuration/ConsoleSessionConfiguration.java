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
package net.linlan.emis.console.configuration;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.linlan.emis.console.cache.RedisUserCache;
import net.linlan.emis.core.security.jackson2.CoreJackson2Module;
import net.linlan.emis.core.security.jackson2.SimpleGrantedAuthorityDeserializer;
import net.linlan.emis.core.security.jackson2.UserDetailsDeserializer;
import net.linlan.emis.core.security.session.ClusterSessionRegistryImpl;
import net.linlan.emis.manage.jackjson.AuthenticationJacksonModule;
import net.linlan.emis.support.security.jackjson.SupportJackson2Modules;
import net.linlan.emis.support.userdetails.OrganizationType;
import net.linlan.emis.support.userdetails.UserDetails;

/**
 * 控制台Session配置
 * @author Linlan
 * Created on  2023/7/22 21:43
 */
@Configuration
public class ConsoleSessionConfiguration implements BeanClassLoaderAware {

    /**
     * 是session为Spring Security提供的
     *
     * 用于在集群环境下控制会话并发的会话注册表实现
     *
     * @return {@link SessionRegistry}
     */
    @Bean
    public SessionRegistry sessionRegistry(FindByIndexNameSessionRepository<? extends Session> sessionRepository,
                                           RedisSessionProperties redisSessionProperties) {
        return new ClusterSessionRegistryImpl<>(sessionRepository,
            redisSessionProperties.getNamespace());
    }

    /**
     * 配置TokenRepository
     *
     * @return {@link PersistentTokenRepository}
     */
    @Bean
    @ConditionalOnMissingBean(value = PersistentTokenRepository.class)
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }

    private ClassLoader loader;

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.loader = classLoader;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }

    /**
     * Customized {@link ObjectMapper} to add mix-in for class that doesn't have default
     * constructors
     * @return the {@link ObjectMapper} to use
     */
    private ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        CoreJackson2Module jackson2Module = new CoreJackson2Module();
        List<com.fasterxml.jackson.databind.Module> modules = SupportJackson2Modules.getModules(this.loader);
        mapper.registerModules(modules);
        mapper.registerModules(jackson2Module);
        mapper.registerModules(new AuthenticationJacksonModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    /**
     * 配置密码加密
     *
     * @return {@link BCryptPasswordEncoder}
     */
    @Bean
    @ConditionalOnMissingBean(value = BCryptPasswordEncoder.class)
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /** 解决authenticate的对象放入缓存有organizations、groups、applications，但是缓存后丢失了很多对象，采用自定义userCache进行处理
     * @param userDetailsService
     * @param userCache
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                               UserCache userCache) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setUserCache(userCache); // 注入RedisUserCache
        provider.setPasswordEncoder(passwordEncoder()); // 设置密码编码解码方式
        return provider;
    }

    /** 重新默认AbstractUserDetailsAuthenticationProvider内userCache
     * @param userDetailsRedisTemplate 用户详情REDIS模板
     * @return      用户缓存
     */
    @Bean
    public UserCache userCache(RedisTemplate<String, UserDetails> userDetailsRedisTemplate) {
        return new RedisUserCache(userDetailsRedisTemplate);
    }

    /** 用户详情REDIS模板
     * @param connectionFactory     缓存工厂
     * @return      自定义的UserDetails缓存模板
     */
    @Bean
    public RedisTemplate<String, UserDetails> userDetailsRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UserDetails> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 配置 Jackson2JsonRedisSerializer
        Jackson2JsonRedisSerializer<UserDetails> serializer = new Jackson2JsonRedisSerializer<>(
            UserDetails.class);

        ObjectMapper objectMapper = new ObjectMapper();

        // 注册模块
        // 注册 JavaTimeModule
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 处理 OrganizationType 的自定义序列化
        SimpleModule module = new SimpleModule();
        // 20250814 注册 自定义的用户解析方法，解决报错Could not read JSON: Cannot construct instance of `net.linlan.emis.support.userdetails.UserDetails
        module.addDeserializer(UserDetails.class, new UserDetailsDeserializer());
        // 20250817 新增 SimpleGrantedAuthority 反序列化器
        module.addDeserializer(SimpleGrantedAuthority.class,
            new SimpleGrantedAuthorityDeserializer());
        // 20250815 添加组织机构类型的类和值反序列化异常处理
        module.addSerializer(OrganizationType.class, new JsonSerializer<OrganizationType>() {
            @Override
            public void serialize(OrganizationType value, JsonGenerator gen,
                                  SerializerProvider provider) throws IOException {
                gen.writeString(value.getType()); // 序列化为字符串
            }
        }).addDeserializer(OrganizationType.class, new JsonDeserializer<OrganizationType>() {
            @Override
            public OrganizationType deserialize(JsonParser p,
                                                DeserializationContext ctxt) throws IOException {
                return OrganizationType.fromString(p.getValueAsString()); // 从字符串还原
            }
        });
        objectMapper.registerModule(module);

        serializer.setObjectMapper(objectMapper);
        template.setValueSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());

        return template;
    }

}
