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
package net.linlan.emis.manage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.core.RandomUtils;
import net.linlan.emis.core.constant.TriggerType;
import net.linlan.emis.manage.client.IdentitySourceClient;
import net.linlan.emis.manage.processor.IdentitySourceEventPostProcessor;
import net.linlan.emis.manage.processor.IdentitySourceSyncDeptPostProcessor;
import net.linlan.emis.manage.processor.IdentitySourceSyncUserPostProcessor;
import net.linlan.emis.manage.vo.VerifyDept;
import net.linlan.emis.manage.vo.VerifyUser;

/**
 * 身份验证源配置抽象类，只适合目前流行的钉钉、企业微信等，暂不支持AD, LDAP
 *
 * @author Linlan
 *
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class AbstractDefaultIdentityVerifySource<T extends IdentitySourceConfig>
                                                         implements IdentityVerifySource<T> {

    public static final String URL_JOINER = "/";

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * sync
     *
     * @param triggerType {@link TriggerType} 执行方式
     */
    @Override
    public void sync(TriggerType triggerType) {
        String batch = RandomUtils.randomCode(7);
        //拉取部门
        List<VerifyDept> deptList = identitySourceClient.getDeptList();
        //处理部门信息
        identitySourceSyncDeptPostProcessor.process(batch, getId(), deptList, new Date(),
            triggerType);

        // 拉取用户，根据部门拉取用户，而不是直接调用getUserList()，第一考虑性能，也是为了数据准确性。
        LocalDateTime userListStartTime = LocalDateTime.now();
        List<VerifyUser> userList = new ArrayList<>();
        for (VerifyDept dept : deptList) {
            userList.addAll(identitySourceClient.getUserList(dept));
        }
        userList = userList.stream().distinct().collect(Collectors.toList());
        // 处理用户数据
        identitySourceSyncUserPostProcessor.process(batch, getId(), userList, userListStartTime,
            triggerType);
    }

    protected List<VerifyUser> getUser(String id) {
        List<VerifyUser> userList = new ArrayList<>();
        userList.add(getIdentitySourceClient().getUser(id));
        return userList;
    }

    protected List<VerifyDept> getDept(String id) {
        List<VerifyDept> deptList = new ArrayList<>();
        deptList.add(getIdentitySourceClient().getDept(id));
        return deptList;
    }

    protected List<String> getIdList(String id) {
        List<String> idList = new ArrayList<>();
        idList.add(id);
        return idList;
    }

    /**
     * 身份源ID
     */
    private final String                                id;

    /**
     * 身份源名称
     */
    private final String                                name;

    /**
     * 提供商配置
     */
    private final T                                     config;

    /**
     * 身份源提供商客户端
     */
    protected final IdentitySourceClient                identitySourceClient;

    /**
     * 身份源同步后处理器
     */
    protected final IdentitySourceSyncUserPostProcessor identitySourceSyncUserPostProcessor;

    /**
     * 身份源同步后处理器
     */
    protected final IdentitySourceSyncDeptPostProcessor identitySourceSyncDeptPostProcessor;

    /**
     * 身份源事件接收后处理器
     */
    protected final IdentitySourceEventPostProcessor    identitySourceEventPostProcessor;
}
