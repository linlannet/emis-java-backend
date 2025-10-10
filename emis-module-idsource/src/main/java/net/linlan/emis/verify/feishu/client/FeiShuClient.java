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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.core.StringUtils;
import net.linlan.emis.manage.client.IdentitySourceClient;
import net.linlan.emis.manage.exception.ApiCallException;
import net.linlan.emis.manage.vo.VerifyDept;
import net.linlan.emis.manage.vo.VerifyUser;
import net.linlan.emis.manage.vo.VerifyUserDetail;
import net.linlan.emis.verify.feishu.FeiShuConfig;
import net.linlan.emis.verify.feishu.vo.FeishuBaseRequest;
import net.linlan.emis.verify.feishu.vo.FeishuDepartmentResponse;
import net.linlan.emis.verify.feishu.vo.FeishuDeptListRequest;
import net.linlan.emis.verify.feishu.vo.FeishuDetailsResponse;
import net.linlan.emis.verify.feishu.vo.FeishuListResponse;
import net.linlan.emis.verify.feishu.vo.FeishuUserListRequest;
import net.linlan.emis.verify.feishu.vo.FeishuUserResponse;
import static net.linlan.emis.verify.feishu.FeiShuConstant.*;

/**
 * 飞书提供商client
 *
 * @author Linlan
 * 
 */
@Slf4j(topic = LOGGER_NAME)
public class FeiShuClient extends AbstractFeiShuClient implements IdentitySourceClient {

    public FeiShuClient(FeiShuConfig config) {
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
     * @param country {@link  String}
     * @param nickName {@link  String}
     * @return {@link  VerifyUser}
     */
    private VerifyUser buildUser(String userid, Boolean active, String name,
                                 List<String> deptIdList, String avatar, String email,
                                 String orgEmail, String mobile, String country, String nickName) {
        VerifyUser user = new VerifyUser();
        user.setUserId(userid);
        user.setActive(active);
        user.setDeptIdList(deptIdList);
        user.setAvatarUrl(avatar);
        user.setEmail(email);
        user.setOrganEmail(orgEmail);
        user.setMobile(mobile);
        try {
            Phonenumber.PhoneNumber cn = PhoneNumberUtil.getInstance().parse(mobile, "CN");
            user.setMobile(String.valueOf(cn.getNationalNumber()));
            user.setMobileAreaCode(String.valueOf(cn.getCountryCode()));
        } catch (NumberParseException e) {
            throw new RuntimeException(e);
        }
        VerifyUserDetail userDetail = new VerifyUserDetail();
        userDetail.setName(name);
        userDetail.setNickName(nickName);
        user.setUserDetail(userDetail);
        return user;
    }

    private List<VerifyDept> getDepartmentList(String deptId, List<VerifyDept> departmentList,
                                               String pageToken) {
        FeishuListResponse<FeishuDepartmentResponse> deptResponse = request(
            String.format(DEPARTMENT_LIST, deptId),
            new FeishuDeptListRequest().setPageToken(pageToken),
            new ParameterizedTypeReference<FeishuListResponse<FeishuDepartmentResponse>>() {
            }, getAccessToken());
        if (deptResponse.isSuccess()) {
            FeishuListResponse.ListData<FeishuDepartmentResponse> data = deptResponse.getData();
            List<FeishuDepartmentResponse> items = data.getItems();
            if (!CollectionUtils.isEmpty(items)) {
                List<VerifyDept> deptList = items.stream().map(item -> {
                    String departmentId = item.getOpenDepartmentId();
                    VerifyDept dept = new VerifyDept();
                    dept.setDeptId(departmentId);
                    dept.setParentId(item.getParentDepartmentId());
                    dept.setName(item.getName());
                    dept.setPriority(item.getPriority());
                    dept.setChildren(getDepartmentList(departmentId, new ArrayList<>(), null));
                    return dept;
                }).collect(Collectors.toList());
                departmentList.addAll(deptList);
            }
            if (StringUtils.isNotEmpty(data.getPageToken())) {
                return getDepartmentList(deptId, departmentList, data.getPageToken());
            }
            return departmentList;
        }
        log.error("获取飞书子部门列表失败: {}", deptResponse.getMsg());
        throw new ApiCallException("获取飞书子部门列表失败: " + deptResponse.getMsg());
    }

    @Override
    public VerifyDept getDept(String id) {
        FeishuDetailsResponse<FeishuDepartmentResponse> deptResponse = request(DEPARTMENT_GET + id,
            new FeishuBaseRequest(),
            new ParameterizedTypeReference<FeishuDetailsResponse<FeishuDepartmentResponse>>() {
            }, getAccessToken());
        if (deptResponse.isSuccess()) {
            FeishuDepartmentResponse department = deptResponse.getData().getDepartment();
            VerifyDept dept = new VerifyDept();
            dept.setDeptId(department.getOpenDepartmentId());
            dept.setParentId(department.getParentDepartmentId());
            dept.setName(department.getName());
            return dept;
        }
        log.error("飞书获取部门详情信息失败: {}", deptResponse.getMsg());
        throw new ApiCallException("飞书获取部门详情信息失败: " + deptResponse.getMsg());
    }

    @Override
    public List<VerifyDept> getDeptList(String id) {
        return getDepartmentList(id, new ArrayList<>(), null);

    }

    @Override
    public List<VerifyDept> getDeptList() {
        VerifyDept dept = getDept(getRootId());
        List<VerifyDept> deptList = getDeptList(getRootId());
        deptList.add(dept);
        return deptList;
    }

    /**
     * 调用获取用户列表
     */
    private List<FeishuUserResponse> getUserList(String deptId, String pageToken,
                                                 List<FeishuUserResponse> userList) {
        FeishuListResponse<FeishuUserResponse> userDTO = request(USER_LIST,
            new FeishuUserListRequest().setPageToken(pageToken).setDepartmentId(deptId),
            new ParameterizedTypeReference<FeishuListResponse<FeishuUserResponse>>() {
            }, getAccessToken());
        if (userDTO.isSuccess()) {
            FeishuListResponse.ListData<FeishuUserResponse> data = userDTO.getData();
            List<FeishuUserResponse> items = data.getItems();
            if (!CollectionUtils.isEmpty(items)) {
                userList.addAll(items);
            }
            pageToken = data.getPageToken();
            if (StringUtils.isNotEmpty(pageToken)) {
                return getUserList(deptId, pageToken, userList);
            }
            return userList;
        }
        log.error("获取用户列表失败: {}", userDTO.getMsg());
        throw new ApiCallException("获取部门用户列表失败: " + userDTO.getMsg());
    }

    @Override
    public VerifyUser getUser(String userId) {
        FeishuDetailsResponse<FeishuUserResponse> userResponse = request(GET_USER + userId,
            new FeishuBaseRequest(),
            new ParameterizedTypeReference<FeishuDetailsResponse<FeishuUserResponse>>() {
            }, getAccessToken());
        if (userResponse.isSuccess()) {
            FeishuUserResponse user = userResponse.getData().getUser();
            return buildUser(user.getUserId(), user.getStatus().isActive(), user.getName(),
                user.getDepartmentIds(), user.getAvatarUrl().getAvatar(), user.getEmail(),
                user.getEnterpriseEmail(), user.getMobile(), user.getCountry(), user.getNickname());
        }
        log.error("获取用户详细信息失败: {}", userResponse.getMsg());
        throw new ApiCallException("获取用户详细信息失败: " + userResponse.getMsg());
    }

    @Override
    public List<VerifyUser> getUserList(String deptId) {
        List<FeishuUserResponse> userList = getUserList(deptId, null, new ArrayList<>());
        return userList.stream()
            .map(user -> buildUser(user.getUserId(), user.getStatus().isActive(), user.getName(),
                user.getDepartmentIds(), user.getAvatarUrl().getAvatar(), user.getEmail(),
                user.getEnterpriseEmail(), user.getMobile(), user.getCountry(), user.getNickname()))
            .collect(Collectors.toList());
    }

    @Override
    public List<VerifyUser> getUserList() {
        List<VerifyDept> deptList = getDeptList();
        List<VerifyUser> users = new ArrayList<>();
        deptList.forEach(deptId -> users.addAll(getUserList(deptId.getDeptId())));
        return users;
    }

    /**
     * 请求接口
     *
     * @param url {@link String}
     * @param json {@link Object}
     * @param reference {@link ParameterizedTypeReference<T>}
     * @param token {@link String }
     * @return {@link T}
     */
    private <T> T request(String url, Object json, ParameterizedTypeReference<T> reference,
                          String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        //  构建请求地址
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (Objects.nonNull(json)) {
            JSONObject params = JSON.parseObject(JSON.toJSONString(json), JSONObject.class);
            params.forEach(builder::queryParam);
        }
        ResponseEntity<T> response = restOperations.exchange(builder.build().encode().toString(),
            HttpMethod.GET, requestEntity, reference);
        T body = response.getBody();
        Assert.notNull(body, "请求[{" + url + "}], 返回结果为空");
        return body;
    }

    private String getRootId() {
        return "0";
    }
}
