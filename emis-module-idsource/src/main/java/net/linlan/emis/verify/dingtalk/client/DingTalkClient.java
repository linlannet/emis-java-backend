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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiV2DepartmentGetRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserListRequest;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.taobao.api.ApiException;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.manage.client.IdentitySourceClient;
import net.linlan.emis.manage.exception.ApiCallException;
import net.linlan.emis.manage.vo.VerifyDept;
import net.linlan.emis.manage.vo.VerifyUser;
import net.linlan.emis.manage.vo.VerifyUserDetail;
import net.linlan.emis.verify.dingtalk.DingTalkConfig;
import net.linlan.emis.verify.dingtalk.DingTalkConstants;
import static net.linlan.emis.verify.dingtalk.DingTalkConstants.LOGGER_NAME;

/**
 * 钉钉组织机构
 *
 * @author Linlan
 *
 */
@Slf4j(topic = LOGGER_NAME)
public class DingTalkClient extends AbstractDingTalkClient implements IdentitySourceClient {

    public DingTalkClient(DingTalkConfig config) {
        super(config);
    }

    /**
     * 根据部门ID获取部门数据
     *
     * @param id {@link List} 部门ID
     * @return {@link List} 部门数据
     */
    @Override
    public VerifyDept getDept(String id) {
        try {
            com.dingtalk.api.DingTalkClient client = new DefaultDingTalkClient(
                DingTalkConstants.DEPARTMENT_GET);
            OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
            req.setDeptId(Long.valueOf(id));
            req.setLanguage(DingTalkConstants.ZH_CN);
            log.info("获取钉钉部门信息入参: {}", JSON.toJSONString(req));
            OapiV2DepartmentGetResponse execute = client.execute(req, getAccessToken());
            log.info("获取钉钉部门信息返回: {}", JSON.toJSONString(execute));
            //成功处理数据
            if (execute.isSuccess()) {
                OapiV2DepartmentGetResponse.DeptGetResponse result = execute.getResult();
                VerifyDept dept = new VerifyDept();
                dept.setDeptId(String.valueOf(result.getDeptId()));
                dept.setParentId(String.valueOf(result.getParentId()));
                dept.setName(result.getName());
                dept.setPriority(result.getOrder());
                return dept;
            }
            log.error("获取钉钉部门信息失败: {}", JSON.toJSONString(execute));
            throw new ApiCallException(execute.getErrmsg());
        } catch (ApiException e) {
            log.error("获取钉钉部门信息失败: {}", e.getErrMsg(), e);
            throw new ApiCallException(e.getErrMsg());
        }
    }

    /**
     * 根据部门ID获取指定部门及其下的子部门（以及子部门的子部门等等，递归）
     *
     * @param id {@link List} 部门ID
     * @return {@link List} 部门数据
     */
    @Override
    public List<VerifyDept> getDeptList(String id) {
        return getSubDeptList(id);
    }

    /**
     * 获取所有部门数据
     *
     * @return    结果列表
     */
    @Override
    public List<VerifyDept> getDeptList() {
        VerifyDept dept = getDept(getRootId());
        List<VerifyDept> deptList = getDeptList(getRootId());
        deptList.add(dept);
        return deptList;
    }

    /**
     * 根据科室ID获取子部门信息
     *
     * @param deptId {@link  String}
     * @return 机构用户IDs
     */
    private List<VerifyDept> getSubDeptList(String deptId) {
        List<VerifyDept> list = new ArrayList<>();
        try {
            //获取子部门
            com.dingtalk.api.DingTalkClient client = new DefaultDingTalkClient(
                DingTalkConstants.DEPARTMENT_LIST);
            OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
            req.setDeptId(Long.valueOf(deptId));
            req.setLanguage(DingTalkConstants.ZH_CN);
            log.info("获取钉钉子部门列表入参: {}", JSON.toJSONString(req));
            OapiV2DepartmentListsubResponse execute = client.execute(req, getAccessToken());
            log.info("获取钉钉子部门列表返回: {}", JSON.toJSONString(execute));
            if (execute.isSuccess()) {
                for (OapiV2DepartmentListsubResponse.DeptBaseResponse response : execute
                    .getResult()) {
                    VerifyDept dept = getDept(String.valueOf(response.getDeptId()));
                    // 递归查询子节点
                    String childDeptId = String.valueOf(response.getDeptId());
                    dept.setChildren(getSubDeptList(childDeptId));
                    list.add(dept);
                }
                return list;
            }
            log.error("获取钉钉子部门列表失败: {}", JSON.toJSONString(execute));
            throw new ApiCallException(execute.getErrmsg());
        } catch (ApiException e) {
            log.error("获取钉钉部门列表发生异常: {}", e.getErrMsg(), e);
            throw new ApiCallException(e.getErrMsg());
        }
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param id {@link String} 用户ID
     * @return {@link List} 用户信息
     */
    @Override
    public VerifyUser getUser(String id) {
        try {
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(id);
            log.info("获取钉钉用户信息入参: {}", JSON.toJSONString(req));
            com.dingtalk.api.DingTalkClient client = new DefaultDingTalkClient(
                DingTalkConstants.GET_USER);
            OapiV2UserGetResponse execute = client.execute(req, getAccessToken());
            log.info("获取钉钉用户信息返回: {}", JSON.toJSONString(execute));
            if (execute.isSuccess()) {
                OapiV2UserGetResponse.UserGetResponse result = execute.getResult();
                return buildUser(result.getUserid(), result.getActive(), result.getName(),
                    result.getDeptIdList(), result.getAvatar(), result.getEmail(),
                    result.getOrgEmail(), result.getMobile(), result.getStateCode());
            }
            log.error("获取钉钉用户信息失败: {}", JSON.toJSONString(execute));
            throw new ApiCallException(execute.getErrmsg());
        } catch (ApiException e) {
            log.error("获取钉钉用户信息异常: {}", e.getErrMsg(), e);
            throw new ApiCallException(e.getErrMsg());
        }
    }

    /**
     * 根据部门ID获取用户列表
     *
     * @param deptId {@link String}
     * @return {@link List} 用户列表
     */
    @Override
    public List<VerifyUser> getUserList(String deptId) {
        OapiV2UserListRequest req = new OapiV2UserListRequest();
        req.setDeptId(Long.valueOf(deptId));
        req.setCursor(DingTalkConstants.CURSOR);
        req.setSize(DingTalkConstants.SIZE);
        req.setContainAccessLimit(false);
        req.setLanguage(DingTalkConstants.ZH_CN);
        return getUserList(req);
    }

    /**
     * 根据全部用户列表
     *
     * @return    结果列表
     */
    @Override
    public List<VerifyUser> getUserList() {
        //获取全部部门
        List<VerifyDept> list = getDeptList();
        List<VerifyUser> users = new ArrayList<>();
        //根据部门获取用户信息
        for (VerifyDept dept : list) {
            users.addAll(getUserList(dept));
        }
        return users;
    }

    /**
     * 分页获取用户列表
     *
     * @param request {@link OapiV2UserListRequest}
     * @return {@link List} 用户列表
     */
    private List<VerifyUser> getUserList(OapiV2UserListRequest request) {
        List<VerifyUser> list = new ArrayList<>();
        try {
            log.info("分页获取用户列表入参：{}", JSON.toJSONString(request));
            com.dingtalk.api.DingTalkClient client = new DefaultDingTalkClient(
                DingTalkConstants.USER_LIST);
            OapiV2UserListResponse execute = client.execute(request, getAccessToken());
            log.info("分页获取用户列表返回：{}", JSON.toJSONString(execute));
            if (execute.isSuccess()) {
                OapiV2UserListResponse.PageResult result = execute.getResult();
                List<OapiV2UserListResponse.ListUserResponse> userList = result.getList();
                for (OapiV2UserListResponse.ListUserResponse response : userList) {
                    VerifyUser user = buildUser(response.getUserid(), response.getActive(),
                        response.getName(), response.getDeptIdList(), response.getAvatar(),
                        response.getEmail(), response.getOrgEmail(), response.getMobile(),
                        response.getStateCode());
                    list.add(user);
                }
                // 分页
                if (Boolean.TRUE.equals(result.getHasMore())) {
                    request.setCursor(result.getNextCursor());
                    list.addAll(getUserList(request));
                }
                return list;
            }
            log.error("获取钉钉用户列表失败：{}", JSON.toJSONString(execute));
            throw new ApiCallException(execute.getErrmsg());
        } catch (ApiException e) {
            log.error("获取钉钉用户列表异常：{}", e.getErrMsg(), e);
            throw new ApiCallException(e.getErrMsg());
        }
    }

    /**
     * build user
     *
     * @param userid     {@link  String}
     * @param active     {@link  Boolean}
     * @param name       {@link  String}
     * @param deptIdList {@link  List}
     * @param avatar     {@link  String}
     * @param email      {@link  String}
     * @param orgEmail   {@link  String}
     * @param mobile     {@link  String}
     * @param stateCode  {@link  String}
     * @return {@link  VerifyUser}
     */
    private VerifyUser buildUser(String userid, Boolean active, String name, List<Long> deptIdList,
                                 String avatar, String email, String orgEmail, String mobile,
                                 String stateCode) {
        VerifyUser user = new VerifyUser();
        user.setUserId(userid);
        user.setActive(active);
        user.setDeptIdList(deptIdList.stream().map(String::valueOf).collect(Collectors.toList()));
        user.setAvatarUrl(avatar);
        user.setEmail(email);
        user.setOrganEmail(orgEmail);
        user.setMobile(mobile);
        user.setMobileAreaCode(stateCode);
        VerifyUserDetail userDetail = new VerifyUserDetail();
        userDetail.setName(name);
        userDetail.setNickName(name);
        user.setUserDetail(userDetail);
        return user;
    }

    private String getRootId() {
        return "1";
    }
}
