/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.organ.entity;

import java.util.Date;

import org.springframework.lang.Nullable;

import com.google.common.base.Function;

import lombok.Data;

import net.linlan.emis.support.userdetails.OrganizationType;
import net.linlan.frame.admin.dto.AdminDeptDto;
import net.linlan.frame.admin.entity.AdminDept;

/**
 *
 * BaseOrgan和CoreOrgan混合数据域:系统机构实体类
 * @author Linlan
 *
 * CreateTime 2018-03-02 17:19:48
 *
 */
@Data
public class EmisOrganVo {
    /**
     * 部门ID
     */
    private Long                 id;
    /**
     * 机构ID
     */
    private String               organId;
    /**
     * 机构名称
     */
    private String               organName;
    /**
     * 部门名称，对应AdminDept内sname
     */
    private String               name;

    /**
     * 机构编码
     */
    private String               code;

    /**
     * 类型
     */
    private OrganizationType     type;

    /**
     * 上级ID
     */
    private Long                 parentId;

    /**
     * 路径枚举ID，对应AdminDept内searchCode
     */
    private String               path;

    /**
     * 路径显示名称，对应AdminDept内name
     */
    private String               displayPath;

    /**
     * 外部ID，对应appId
     */
    private String               appId;

    /**
     * 数据来源，对应AdminDept内createType
     */
    private String               dataOrigin;

    /**
     * 身份源id
     */
    private String               identitySourceId;

    /**
     * 排序
     */
    private Integer              priority;

    /**
     * 是否叶子节点 leaf
     */
    private Boolean              leaf;
    /**
     * 子节点数量
     */
    private String               childNum;
    /**
     * 是否启用
     */
    private Boolean              enabled;
    /**
     * 身份源id
     */
    private String               imagePath;
    /**
     * 创建人id
     */
    private String               creatorId;
    /**
     * 创建时间
     */
    private Date                 createTime;

    /**
     * 最后修改时间
     */
    private Date                 lastTime;
    /**
     * 备注
     */
    private String               remark;
    /**
     * 数据赋值TO方法，将AdminDept的input对象赋值给AdminDeptVo
     */
    public static final Function TO = new Function<AdminDept, EmisOrganVo>() {
                                        @Nullable
                                        @Override
                                        public EmisOrganVo apply(@Nullable AdminDept input) {
                                            return new EmisOrganVo(input);
                                        }
                                    };

    /**
     * @param input 数据库查询出的entity对象，对VO对象进行赋值，默认的字段为全部字段，可结合实际调整
     */
    public EmisOrganVo(AdminDept input) {
        this.id = input.getId();
        this.organId = input.getOrganId();
        this.parentId = input.getParentId();
        this.identitySourceId = input.getForeignId();
        this.dataOrigin = input.getCreateType().toString();
        this.type = OrganizationType.fromString(input.getType());
        this.displayPath = input.getName();
        this.name = input.getSname();
        this.leaf = input.getIsLeaf();
        this.enabled = !input.getIsDisabled();
        this.path = input.getSearchCode();
        this.imagePath = input.getImagePath();
        this.priority = input.getPriority();
        this.creatorId = input.getCreatorId();
        this.createTime = input.getCreateTime();
        this.lastTime = input.getLastTime();
        this.remark = input.getDescription();
    }

    /**
     * 数据赋值DTO方法，将AdminDeptDto的input对象赋值给AdminDeptVo
     */
    public static final Function DTO = new Function<AdminDeptDto, EmisOrganVo>() {
        @Nullable
        @Override
        public EmisOrganVo apply(@Nullable AdminDeptDto input) {
            return new EmisOrganVo(input);
        }
    };

    /**
     * @param input 数据库查询出的Dto对象，对VO对象进行赋值，默认的字段为全部字段，可结合实际调整
     */
    public EmisOrganVo(AdminDeptDto input) {
        this.id = input.getId();
        this.organId = input.getOrganId();
        this.organName = input.getOrganName();
        this.parentId = input.getParentId();
        this.appId = input.getAppId();
        this.identitySourceId = input.getForeignId();
        this.dataOrigin = input.getCreateType().toString();
        this.type = OrganizationType.fromString(input.getType());
        this.displayPath = input.getName();
        this.name = input.getSname();
        this.leaf = input.getIsLeaf();
        this.enabled = !input.getIsDisabled();
        this.path = input.getSearchCode();
        this.imagePath = input.getImagePath();
        this.priority = input.getPriority();
        this.creatorId = input.getCreatorId();
        this.createTime = input.getCreateTime();
        this.lastTime = input.getLastTime();
        this.remark = input.getDescription();
        this.childNum = input.getChildNum();
    }

}
