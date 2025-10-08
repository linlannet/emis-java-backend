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
package net.linlan.emis.manage.client;

import java.util.ArrayList;
import java.util.List;

import net.linlan.emis.manage.vo.VerifyDept;
import net.linlan.emis.manage.vo.VerifyUser;

/**
 * 身份源客户端
 *
 * @author Linlan
 *
 */
public interface IdentitySourceClient {

    /**
     * 递归获取用户
     * @param dept {@link VerifyDept}
     * @return    结果列表
     */
    default List<VerifyUser> getUserList(VerifyDept dept) {
        List<VerifyUser> list = new ArrayList<>(getUserList(dept.getDeptId()));
        List<VerifyDept> children = dept.getChildren();
        if (!dept.isLeaf()) {
            children.forEach(item -> list.addAll(getUserList(item)));
        }
        return list;
    }

    /**
     * 根据部门ID获取部门数据
     *
     * @param id {@link List} 部门ID
     * @return {@link List} 部门数据
     */
    VerifyDept getDept(String id);

    /**
     * 根据部门ID获取指定部门及其下的子部门（以及子部门的子部门等等，递归）
     *
     * @param id {@link List} 部门ID
     * @return {@link List} 部门数据
     */
    List<VerifyDept> getDeptList(String id);

    /**
     * 获取所有部门数据
     *
     * @return    结果列表
     */
    List<VerifyDept> getDeptList();

    /**
     * 根据用户ID获取用户信息
     *
     * @param id {@link String} 用户ID
     * @return {@link List} 用户信息
     */
    VerifyUser getUser(String id);

    /**
     * 根据部门ID获取用户列表
     *
     * @param deptId {@link String} 科室ID
     * @return {@link List} 用户列表
     */
    List<VerifyUser> getUserList(String deptId);

    /**
     * 根据全部用户列表
     *
     * @return    结果列表
     */
    List<VerifyUser> getUserList();
}
