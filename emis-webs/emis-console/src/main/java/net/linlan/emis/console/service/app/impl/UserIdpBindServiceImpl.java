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
package net.linlan.emis.console.service.app.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.app.UserIdpBindConverter;
import net.linlan.emis.console.pojo.result.app.UserIdpBindListResult;
import net.linlan.emis.console.service.app.UserIdpBindService;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.ubind.entity.UserIdpBind;

/**
 * 用户身份提供商绑定
 *
 * @author Linlan
 * Created on  2021/12/11 21:10
 */
@Component
@Slf4j
@AllArgsConstructor
public class UserIdpBindServiceImpl implements UserIdpBindService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unbindUserIdpBind(String id) {
        UserIdpBind bind = ubindService.findById(id);
        //用户不存在
        if (bind == null) {
            AuditContext.setContent("解绑失败，用户身份提供商绑定关系不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        ubindService.deleteById(id);
        //@formatter:off
        List<Target> targets= new ArrayList<>();
        targets.add(Target.builder().id(bind.getUserId()).name(bind.getDescription()).type(TargetType.USER).build());
        targets.add(Target.builder().id(bind.getProviderId()).name(bind.getProviderType()).type(TargetType.IDENTITY_PROVIDER).build());
        AuditContext.setTarget(targets);
        //@formatter:on
        return true;
    }

    /**
     * 查询用户身份提供商绑定
     *
     * @param userId     {@link  String}
     * @return {@link Page}
     */
    @Override
    public List<UserIdpBindListResult> getUserIdpBindList(String userId) {
        //查询映射
        return userIdpBindConverter.userIdpBindEntityConvertToUserIdpBindListResult(
            ubindService.getList(new net.linlan.commons.script.json.StringMap()
                .put("userId", userId).map()));
    }

    /**
     * UserIdpBindConverter
     */
    private final UserIdpBindConverter userIdpBindConverter;

    /**
     * 用户身份提供商绑定服务
     */
    private final net.linlan.emis.ubind.service.UserIdpBindService ubindService;
}
