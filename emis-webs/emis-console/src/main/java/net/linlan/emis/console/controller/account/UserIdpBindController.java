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
package net.linlan.emis.console.controller.account;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import static net.linlan.emis.user.constant.AccountConstants.ACCOUNT_GROUP_NAME;
import static net.linlan.emis.user.constant.AccountConstants.USER_PATH;

/**
 * 用户身份提供商绑定
 *
 * @author Linlan
 * Created on  2022/6/4 21:06
 */
@Validated
@Tag(name = ACCOUNT_GROUP_NAME + "-" + "用户身份提供商绑定关系")
@RestController
@AllArgsConstructor
@RequestMapping(value = USER_PATH + "/idp", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserIdpBindController {

}
