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
package net.linlan.emis.console.converter.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.linlan.emis.console.pojo.result.admin.AdministratorListResult;
import net.linlan.emis.console.pojo.result.admin.AdministratorResult;
import net.linlan.emis.console.pojo.save.admin.AdministratorCreateParam;
import net.linlan.emis.console.pojo.update.admin.AdministratorUpdateParam;
import net.linlan.emis.organ.entity.EmisAdminVo;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.emis.support.utils.ImageAvatarUtils;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.utils.SpringContextUtils;
import static net.linlan.emis.support.utils.ImageAvatarUtils.generateAvatarImg;
import static net.linlan.emis.support.utils.MobileNumberUtils.getMobileAreaCode;
import static net.linlan.emis.support.utils.MobileNumberUtils.getMobileNumber;

/**
 * 管理员映射
 *
 * @author Linlan
 * Created on  2020/8/14 22:45
 */
@Mapper(componentModel = "spring")
public interface AdministratorConverter {

    /**
     * 管理员实体转换为管理员分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<AdministratorListResult> entityConvertToAdministratorPaginationResult(com.github.pagehelper.Page<EmisAdminVo> page) {
        Page<AdministratorListResult> result = new Page<>();
        if (!page.isEmpty()) {
            List<AdministratorListResult> list = new ArrayList<>();
            for (EmisAdminVo user : page) {
                AdministratorListResult convert = entityConvertToAdministratorPaginationResult(
                    user);
                //头像
                if (StringUtils.isEmpty(user.getAvatarUrl())) {
                    convert
                        .setAvatarUrl(ImageAvatarUtils.bufferedImageToBase64(generateAvatarImg()));
                } else {
                    convert.setAvatarUrl(user.getAvatarUrl());
                }
                if (org.springframework.util.StringUtils.hasText(convert.getMobile())) {
                    convert.setMobile(
                        (org.springframework.util.StringUtils.hasText(user.getMobileAreaCode())
                            ? "+" + user.getMobileAreaCode()
                            : "") + convert.getMobile());
                }
                list.add(convert);
            }
            //@formatter:off
            result.setPagination(Page.Pagination.builder()
                    .total(page.getTotal())
                    .totalPages(page.getPages())
                    .current(page.getPageNum())
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 管理员实体转换为管理员分页结果
     *
     * @param page {@link EmisAdminVo}
     * @return {@link AdministratorListResult}
     */
    @Mapping(target = "initialized", expression = "java(page.getUsername().equals(net.linlan.emis.support.constant.EmisConstants.DEFAULT_ADMIN_USERNAME))")
    @Mapping(target = "status", source = "status.key")
    @Mapping(target = "emailVerified", source = "emailVerified", defaultValue = "false")
    @Mapping(target = "authTotal", source = "authTotal", defaultValue = "0L")
    AdministratorListResult entityConvertToAdministratorPaginationResult(EmisAdminVo page);

    /**
     * 管理员创建参数转换为管理员实体
     *
     * @param param {@link AdministratorCreateParam}
     * @return {@link EmisAdminVo}
     */
    default EmisAdminVo administratorCreateParamConvertToEntity(AdministratorCreateParam param) {
        if (param == null) {
            return null;
        }

        EmisAdminVo entity = new EmisAdminVo();

        entity.setRemark(param.getRemark());
        entity.setUsername(param.getUsername());
        entity.setPassword(param.getPassword());
        //邮箱
        if (org.springframework.util.StringUtils.hasText(param.getEmail())) {
            entity.setEmail(param.getEmail());
            entity.setEmailVerified(Boolean.TRUE);
        }
        //手机号
        if (org.springframework.util.StringUtils.hasText(param.getMobile())) {
            entity.setMobile(getMobileNumber(param.getMobile()));
            entity.setMobileVerified(Boolean.TRUE);
            entity.setMobileAreaCode(getMobileAreaCode(param.getMobile()));
        }
        entity.setStatus(UserStatus.ENABLED);
        entity.setAuthTotal(0L);
        entity.setNeedChangePassword(false);
        entity.setLastUpdatePasswordTime(java.time.LocalDateTime.now());
        //密码处理
        entity.setPassword(SpringContextUtils
            .getBean(org.springframework.security.crypto.password.PasswordEncoder.class)
            .encode(param.getPassword()));
        return entity;
    }

    /**
     * 管理员更新参数转换为管理员实体类
     *
     * @param param {@link AdministratorUpdateParam}  更新参数
     * @param target {@link AdministratorUpdateParam} 查询实体
     * @return {@link EmisAdminVo} 管理员实体
     */
    default EmisAdminVo administratorUpdateParamConvertToEntity(AdministratorUpdateParam param,
                                                                        EmisAdminVo target) {
        if (param == null) {
            return null;
        }

        EmisAdminVo entity = new EmisAdminVo();
        if (param.getId() != null) {
            entity.setAdminId(param.getId());
        }
        entity.setRemark(param.getRemark());
        //邮箱
        if (StringUtils.isNotEmpty(param.getEmail())) {
            entity.setEmail(param.getEmail());
            entity.setEmailVerified(Boolean.TRUE);
        }
        //手机号
        if (StringUtils.isNotEmpty(param.getMobile())) {
            entity.setMobile(getMobileNumber(param.getMobile()));
            entity.setMobileVerified(Boolean.TRUE);
            entity.setMobileAreaCode(getMobileAreaCode(param.getMobile()));
        }
        entity.setAvatarUrl(param.getAvatarUrl());
        BeanUtils.merge(entity, target);
        if (StringUtils.isBlank(entity.getMobile())) {
            target.setMobile(null);
            target.setMobileVerified(Boolean.FALSE);
            target.setMobileAreaCode("");
        }
        if (StringUtils.isBlank(entity.getEmail())) {
            target.setEmail("");
            target.setEmailVerified(Boolean.FALSE);
        }
        return target;
    }

    /**
     * 实体转为管理员详情返回
     *
     * @param user {@link EmisAdminVo}
     * @return {@link AdministratorResult} 管理员详情
     */
    @Mapping(target = "status", source = "status.key")
    @Mapping(target = "initialized", expression = "java(user.getUsername().equals(net.linlan.emis.support.constant.EmisConstants.DEFAULT_ADMIN_USERNAME))")
    AdministratorResult entityConvertToAdministratorDetailsResult(EmisAdminVo user);

}
