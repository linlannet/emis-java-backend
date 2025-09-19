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
package net.linlan.emis.core.license;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;

import net.linlan.emis.support.manage.version.AppVersionUtils;

/**
 * 控制台版权信息输出监听
 *
 * @author Linlan
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 20 + 1)
public class EmisCopyrightLicense implements
                                  ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final AtomicBoolean PROCESSED      = new AtomicBoolean(false);

    public static final String         LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String         URL            = "https://linlan.net/emis";

    @Override
    public void onApplicationEvent(@NonNull ApplicationEnvironmentPreparedEvent event) {

        if (PROCESSED.get()) {
            return;
        }
        //@formatter:off
        String bannerTextBuilder = LINE_SEPARATOR
                + " :: 欢迎使用 EMIS 多身份集成系统，实现用户生命周期的管理、统一认证和单点登录、为数字身份安全赋能。"+LINE_SEPARATOR
                + " :: Welcome to the enterprise multiple identity system to realize the management of user life cycle, unified authentication and single sign-on, and empower digital identity security."+LINE_SEPARATOR
                + " :: EMIS (V" + AppVersionUtils.getVersion(EmisCopyrightLicense.class) + ") : " + URL + LINE_SEPARATOR;
        System.out.println(bannerTextBuilder);
        // mark processed to be true
        PROCESSED.compareAndSet(false, true);
    }

}
