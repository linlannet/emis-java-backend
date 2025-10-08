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
package net.linlan.emis.verify.wechatwork.client;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.linlan.emis.verify.wechatwork.vo.*;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.manage.client.IdentitySourceClient;
import net.linlan.emis.manage.exception.ApiCallException;
import net.linlan.emis.manage.vo.VerifyDept;
import net.linlan.emis.manage.vo.VerifyUser;
import net.linlan.emis.manage.vo.VerifyUserDetail;
import net.linlan.emis.verify.wechatwork.WeChatWorkConfig;
import net.linlan.emis.verify.wechatwork.WeChatWorkConstant;

/**
 * 企业微信提供商client
 *
 * @author Linlan
 *
 */
@Slf4j(topic = WeChatWorkConstant.LOGGER_NAME)
public class WeChatWorkClient extends AbstractWeChatWorkClient implements IdentitySourceClient {

    public static final String ACCESS_TOKEN = "?access_token=";

    public WeChatWorkClient(WeChatWorkConfig config) {
        super(config);
    }

    /**
     *  build user
     *
     * @param userid {@link  String}
     * @param active {@link  Boolean}
     * @param name {@link  String}
     * @param deptIdList {@link  List}
     * @param avatar {@link  String}
     * @param email {@link  String}
     * @param orgEmail {@link  String}
     * @param mobile {@link  String}
     * @return {@link  VerifyUser}
     */
    private VerifyUser buildUser(String userid, Boolean active, String name,
                                 List<String> deptIdList, String avatar, String email,
                                 String orgEmail, String mobile) {
        VerifyUser user = new VerifyUser();
        user.setUserId(userid);
        user.setActive(active);
        user.setDeptIdList(deptIdList);
        user.setAvatarUrl(avatar);
        user.setEmail(email);
        user.setOrganEmail(orgEmail);
        user.setMobile(mobile);
        user.setMobileAreaCode("86");
        VerifyUserDetail userDetail = new VerifyUserDetail();
        userDetail.setName(name);
        userDetail.setNickName(name);
        user.setUserDetail(userDetail);
        return user;
    }

    @Override
    public VerifyDept getDept(String id) {
        String queryParams = ACCESS_TOKEN + getAccessToken() + "&id=" + id;
        WeChatWorkDeptResponse deptDTO = restOperations
            .getForObject(WeChatWorkConstant.DEPT_DETAILS + queryParams, WeChatWorkDeptResponse.class);
        Assert.notNull(deptDTO, "获取部门详情返回结果为空");
        if (deptDTO.isSuccess()) {
            WeChatWorkDepartment department = deptDTO.getDepartment();
            VerifyDept dept = new VerifyDept();
            dept.setDeptId(String.valueOf(department.getId()));
            dept.setParentId(String.valueOf(department.getParentId()));
            dept.setName(department.getName());
            dept.setPriority(department.getPriority());
            return dept;
        }
        log.error("企业微信获取部门详情信息失败: {}", deptDTO.getErrMsg());
        throw new ApiCallException("企业微信获取部门详情信息失败: " + deptDTO.getErrMsg());
    }

    @Override
    public List<VerifyDept> getDeptList(String id) {
        String queryParams = ACCESS_TOKEN + getAccessToken() + "&id=" + id;
        WeChatWorkDeptListResponse deptDTO = restOperations
            .getForObject(WeChatWorkConstant.DEPT_URL + queryParams, WeChatWorkDeptListResponse.class);
        Assert.notNull(deptDTO, "获取部门列表返回结果为空");
        if (deptDTO.isSuccess()) {
            List<WeChatWorkDepartment> department = deptDTO.getDepartment();
            List<VerifyDept> deptList = department.stream().map(item -> {
                VerifyDept dept = new VerifyDept();
                dept.setDeptId(String.valueOf(item.getId()));
                dept.setParentId(String.valueOf(item.getParentId()));
                dept.setName(item.getName());
                return dept;
            }).collect(Collectors.toList());

            Map<String, List<VerifyDept>> groupDept = deptList.stream()
                .collect(Collectors.groupingBy(VerifyDept::getParentId));
            deptList.forEach(item -> item.setChildren(groupDept.get(item.getDeptId())));
            return deptList.stream().filter(dept -> dept.getParentId().equals(id))
                .collect(Collectors.toList());
        }
        log.error("企业微信获取子部门列表失败: {}", deptDTO.getErrMsg());
        throw new ApiCallException("企业微信获取子部门列表失败: " + deptDTO.getErrMsg());
    }

    @Override
    public List<VerifyDept> getDeptList() {
        return getDeptList(getRootId());
    }

    @Override
    public VerifyUser getUser(String userId) {
        String queryParams = ACCESS_TOKEN + getAccessToken() + "&userid=" + userId;
        WeChatWorkUserResponse user = restOperations
            .getForObject(WeChatWorkConstant.USER_DETAILS + queryParams, WeChatWorkUserResponse.class);
        Assert.notNull(user, "获取用户详情返回结果为空");
        if (user.isSuccess()) {
            return buildUser(user.getUserid(),
                WeChatWorkConstant.USER_ACTIVE.equals(user.getStatus()), user.getName(),
                user.getDepartment(), user.getAvatarUrl(), user.getEmail(), user.getBizMail(),
                user.getMobile());
        }
        log.error("获取用户详细信息失败: {}", user.getErrMsg());
        throw new ApiCallException("获取用户详细信息失败: " + user.getErrMsg());
    }

    @Override
    public List<VerifyUser> getUserList(String deptId) {
        // 部门递归获取子部门下面的成员
        String queryParams = ACCESS_TOKEN + getAccessToken() + "&department_id=" + deptId
                             + "&fetch_child=1";
        WeChatWorkUserListResponse userDTO = restOperations
            .getForObject(WeChatWorkConstant.WX_USER_URL + queryParams, WeChatWorkUserListResponse.class);
        Assert.notNull(userDTO, "获取用户列表返回结果为空");
        if (userDTO.isSuccess()) {
            List<WeChatWorkUserListResponse.UserList> userList = userDTO.getUserList();
            return userList.stream()
                .map(item -> buildUser(item.getUserid(),
                    WeChatWorkConstant.USER_ACTIVE.equals(item.getStatus()), item.getName(),
                    item.getDepartment(), item.getAvatarUrl(), item.getEmail(), item.getBizMail(),
                    item.getMobile()))
                .collect(Collectors.toList());
        }
        log.error("获取部门下用户列表信息失败: {}", userDTO.getErrMsg());
        throw new ApiCallException("获取部门下用户列表信息失败: " + userDTO.getErrMsg());
    }

    @Override
    public List<VerifyUser> getUserList() {
        return getUserList(getRootId());
    }

    private String getRootId() {
        return "1";
    }
}
