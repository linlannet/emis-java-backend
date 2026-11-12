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
package net.linlan.emis.console.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.linlan.emis.console.pojo.query.admin.AdministratorListQuery;
import net.linlan.emis.organ.entity.EmisAdminVo;
import net.linlan.emis.organ.service.EmisAdminService;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.constant.UserStatusInt;
import net.linlan.frame.admin.dto.AdminUserDto;
import net.linlan.frame.admin.entity.AdminUser;
import net.linlan.frame.admin.param.AdminUserParam;
import net.linlan.frame.admin.service.AdminUserService;

/**
 * 管理员数据操作门面，统一转调 emis 管理员服务，对上层提供 EmisAdminVo 视图
 *
 * @author Linlan
 */
@Component
public class AdministratorRepository {

    @Resource
    private EmisAdminService  emisAdminService;

    @Resource
    private AdminUserService  adminUserService;

    /**
     * 根据主键查询
     *
     * @param id {@link String}
     * @return {@link Optional}
     */
    public Optional<EmisAdminVo> findById(String id) {
        return Optional.ofNullable(toVo(emisAdminService.findById(id)));
    }

    /**
     * 根据用户名查询
     *
     * @param username {@link String}
     * @return {@link Optional}
     */
    public Optional<EmisAdminVo> findByUsername(String username) {
        return Optional.ofNullable(toVo(emisAdminService.getByUsername(username)));
    }

    /**
     * 根据手机号查询
     *
     * @param mobile {@link String}
     * @return {@link Optional}
     */
    public Optional<EmisAdminVo> findByMobile(String mobile) {
        return Optional.ofNullable(toVo(emisAdminService.getByMobile(mobile)));
    }

    /**
     * 根据邮箱查询
     *
     * @param email {@link String}
     * @return {@link Optional}
     */
    public Optional<EmisAdminVo> findByEmail(String email) {
        return Optional.ofNullable(toVo(emisAdminService.getByEmail(email)));
    }

    /**
     * 保存
     *
     * @param entity {@link EmisAdminVo}
     */
    public void save(EmisAdminVo entity) {
        emisAdminService.save(toEntity(entity));
    }

    /**
     * 删除
     *
     * @param id {@link String}
     */
    public void deleteById(String id) {
        emisAdminService.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids {@link String[]}
     */
    public void deleteAllById(String[] ids) {
        emisAdminService.deleteAllById(ids);
    }

    /**
     * 更新状态
     *
     * @param id     {@link String}
     * @param status {@link UserStatus}
     */
    public void updateStatus(String id, UserStatus status) {
        emisAdminService.updateStatus(Long.valueOf(id), toStatusInt(status));
    }

    /**
     * 更新密码
     *
     * @param id       {@link String}
     * @param password {@link String}
     * @param time     {@link LocalDateTime}
     */
    public void updatePassword(String id, String password, LocalDateTime time) {
        emisAdminService.updateUserPassword(id, password);
    }

    /**
     * 更新手机号
     *
     * @param id     {@link String}
     * @param mobile {@link String}
     */
    public void updateByIdAndMobile(String id, String mobile) {
        emisAdminService.updateMobile(id, mobile);
    }

    /**
     * 更新邮箱
     *
     * @param id    {@link String}
     * @param email {@link String}
     */
    public void updateByIdAndEmail(String id, String email) {
        emisAdminService.updateEmail(id, email);
    }

    /**
     * 更新认证成功信息
     *
     * @param id        {@link String}
     * @param ip        {@link String}
     * @param loginTime {@link LocalDateTime}
     */
    public void updateAuthSucceedInfo(String id, String ip, LocalDateTime loginTime) {
        emisAdminService.updateLoginCount(id, ip);
    }

    /**
     * 判断用户名是否已存在
     *
     * @param username {@link String}
     * @return boolean
     */
    public boolean existsByUsername(String username) {
        return emisAdminService.getByUsername(username) != null;
    }

    /**
     * 判断手机号是否已存在
     *
     * @param mobile {@link String}
     * @return boolean
     */
    public boolean existsByMobile(String mobile) {
        return emisAdminService.getByMobile(mobile) != null;
    }

    /**
     * 判断邮箱是否已存在
     *
     * @param email {@link String}
     * @return boolean
     */
    public boolean existsByEmail(String email) {
        return emisAdminService.getByEmail(email) != null;
    }

    /**
     * 分页查询管理员
     *
     * @param query {@link AdministratorListQuery}
     * @param model {@link PageModel}
     * @return {@link com.github.pagehelper.Page}
     */
    public com.github.pagehelper.Page<EmisAdminVo> findPage(AdministratorListQuery query, PageModel model) {
        AdminUserParam param = new AdminUserParam();
        if (query != null) {
            param.setUsername(query.getUsername());
            param.setEmail(query.getEmail());
            param.setMobile(query.getMobile());
        }
        if (model != null) {
            param.setPage(model.getCurrent() + 1);
            param.setLimit(model.getPageSize());
        }
        com.github.pagehelper.Page<AdminUserDto> page = adminUserService.getPageDto(param);
        com.github.pagehelper.Page<EmisAdminVo> result = new com.github.pagehelper.Page<>(page.getPageNum(),
            page.getPageSize());
        for (AdminUserDto dto : page) {
            result.add(new EmisAdminVo(dto));
        }
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        return result;
    }

    /**
     * 实体转VO
     *
     * @param entity {@link AdminUser}
     * @return {@link EmisAdminVo}
     */
    private EmisAdminVo toVo(AdminUser entity) {
        if (entity == null) {
            return null;
        }
        return new EmisAdminVo(entity);
    }

    /**
     * VO转实体
     *
     * @param vo {@link EmisAdminVo}
     * @return {@link AdminUser}
     */
    private AdminUser toEntity(EmisAdminVo vo) {
        if (vo == null) {
            return null;
        }
        AdminUser entity = new AdminUser();
        if (vo.getAdminId() != null) {
            entity.setId(Long.valueOf(vo.getAdminId()));
        }
        entity.setUserId(vo.getUserId());
        entity.setUsername(vo.getUsername());
        entity.setName(vo.getFullName());
        entity.setPassword(vo.getPassword());
        entity.setEmail(vo.getEmail());
        entity.setMobile(vo.getMobile());
        entity.setImagePath(vo.getAvatarUrl());
        entity.setStatus(toStatusInt(vo.getStatus()) == null ? null : toStatusInt(vo.getStatus()).getKey());
        entity.setSpare1(vo.getRemark());
        return entity;
    }

    /**
     * 用户状态转整数状态
     *
     * @param status {@link UserStatus}
     * @return {@link UserStatusInt}
     */
    private UserStatusInt toStatusInt(UserStatus status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case ENABLED:
                return UserStatusInt.ENABLED;
            case DISABLED:
                return UserStatusInt.DISABLED;
            case LOCKED:
                return UserStatusInt.LOCKED;
            case EXPIRED_LOCKED:
                return UserStatusInt.EXPIRED_LOCKED;
            case PASSWORD_EXPIRED_LOCKED:
                return UserStatusInt.PASSWORD_EXPIRED_LOCKED;
            default:
                return null;
        }
    }
}
