/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;

import net.linlan.emis.protocol.constant.ProtocolConstants;

/**
 * Form 单点登录
 *
 * @author Linlan
 * Created on  2022/5/7 22:46
 */
@Controller
@RequestMapping(ProtocolConstants.IDP_FORM_SSO_INITIATOR)
@AllArgsConstructor
public class FormInitSingleSignOnEndpoint {
    private final Logger logger = LoggerFactory.getLogger(FormInitSingleSignOnEndpoint.class);

    /**
     * SSO
     *
     * @return {@link ModelAndView}
     */
    @PostMapping
    public ModelAndView sso(@PathVariable String appId) {
        IdGenerator idGenerator = new AlternativeJdkIdGenerator();
        ModelAndView view = new ModelAndView("form_redirect");
        //目标地址
        view.addObject("target", "");
        //随机数
        view.addObject("nonce", idGenerator.generateId());
        return view;
    }
}
