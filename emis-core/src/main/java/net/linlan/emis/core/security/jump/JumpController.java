/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.security.jump;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.core.security.savedredirect.HttpSessionRedirectCache;
import net.linlan.emis.core.security.savedredirect.RedirectCache;
import net.linlan.emis.core.security.savedredirect.SavedRedirect;
import net.linlan.emis.support.constant.EmisConstants;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * 跳转
 *
 * @author Linlan
 */
@Controller
@RequestMapping(EmisConstants.V1_API_PATH + "/jump")
public class JumpController {

    private final RedirectCache redirectCache = new HttpSessionRedirectCache();
    public static String        JUMP_PATH     = "/jump";

    /**
     * jump
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {@link ModelAndView}
     */
    @GetMapping
    public ModelAndView jump(HttpServletRequest request, HttpServletResponse response) {
        SavedRedirect redirect = redirectCache.getRedirect(request, response);
        if (Objects.isNull(redirect)) {
            redirect = new SavedRedirect();
            redirect.setAction(PropertiesContextHelper.getPortalPublicBaseUrl());
            redirect.setMethod(GET.name());
            redirect.setParameters(Lists.newArrayList());
        }
        //清理重定向信息
        request.getSession(false).removeAttribute(RedirectCache.EMIS_SECURITY_SAVED_REDIRECT);
        IdGenerator idGenerator = new AlternativeJdkIdGenerator();
        ModelAndView view = new ModelAndView("jump/jump_get");
        view.addObject("redirect", redirect);
        view.addObject("nonce", idGenerator.generateId());
        if (GET.matches(redirect.getMethod())) {
            return view;
        }
        if (POST.matches(redirect.getMethod())) {
            view.setViewName("jump/jump_post");
            return view;
        }
        return view;
    }
}
