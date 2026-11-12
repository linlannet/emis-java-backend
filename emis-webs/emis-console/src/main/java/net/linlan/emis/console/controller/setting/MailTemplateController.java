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
package net.linlan.emis.console.controller.setting;

import java.util.List;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.result.setting.EmailTemplateListResult;
import net.linlan.emis.console.pojo.result.setting.EmailTemplateResult;
import net.linlan.emis.console.pojo.save.setting.EmailCustomTemplateSaveParam;
import net.linlan.emis.console.service.setting.MailTemplateService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.uext.constant.MailType;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_GROUP_NAME;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_PATH;

/**
 * 邮件配置
 *
 * @author Linlan
 * Created on  2020/7/24 22:36
 */
@Validated
@Tag(name = SETTING_GROUP_NAME + "-" + "邮件模板")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH
                        + "/mail_template", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailTemplateController {

    /**
     * 获取邮件模板列表
     *
     * @return {@link EmailTemplateResult}
     */
    @Validated
    @GetMapping(value = "/list")
    @Operation(summary = "获取邮件模板列表")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<EmailTemplateListResult>> getEmailTemplateList() {
        List<EmailTemplateListResult> result = templateService.getEmailTemplateList();
        return ResponseResult.ok(result);
    }

    /**
     * 根据模板类型查询邮件模板
     *
     * @param type {@link String}
     * @return {@link EmailTemplateResult}
     */
    @Operation(summary = "获取邮件模板信息")
    @GetMapping(value = "/{type}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<EmailTemplateResult> getEmailTemplate(@PathVariable(value = "type") String type) {
        MailType templateType = MailType.getType(type);
        EmailTemplateResult result = templateService.getEmailTemplate(templateType);
        return ResponseResult.ok(result);
    }

    /**
     * 保存邮件模板
     *
     * @param type {@link String}
     * @return {@link EmailTemplateResult}
     */
    @Lock
    @Preview
    @PutMapping(value = "/save_custom/{type}")
    @Audit(type = EventType.SAVE_MAIL_TEMPLATE)
    @Operation(summary = "保存邮件模板")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> saveCustomEmailTemplate(@PathVariable(value = "type") String type,
                                                           @RequestBody @Validated EmailCustomTemplateSaveParam param) {
        MailType templateType = MailType.getType(type);
        boolean result = !Objects
            .isNull(templateService.saveCustomEmailTemplate(templateType, param));
        return ResponseResult.ok(result);
    }

    /**
     * 关闭自定义邮件模板
     *
     * @return {@link EmailTemplateResult}
     */
    @Lock
    @Preview
    @PutMapping(value = "/disable_custom/{type}")
    @Operation(summary = "禁用自定义邮件模板")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableCustomEmailTemplate(@PathVariable(value = "type") String type) {
        templateService.disableCustomEmailTemplate(MailType.getType(type));
        return ResponseResult.ok();
    }

    /**
     * 邮件模板服务类
     */
    private final MailTemplateService templateService;
}
