/*
 * emis-support - support支撑模块
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
package net.linlan.emis.support.manage.preview;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

/**
 * 演示环境
 *
 * @author Linlan
 */
@Aspect
public class DemoEnvironmentAspect implements Ordered {

    private final boolean isOpen;

    public DemoEnvironmentAspect(boolean demo) {
        this.isOpen = demo;
    }

    /** 切面配置入口
     * @param environment   环境
     */
    @Pointcut(value = "@annotation(environment)")
    public void log(Preview environment) {
    }

    /**
     * 请求Controller 日志处理
     *
     * @param joinPoint 切面
     * @param environment 当前环境
     */
    @Before(value = "log(environment)", argNames = "joinPoint,environment")
    public void demoBefore(JoinPoint joinPoint, Preview environment) {
        //1.如果系统没开启演示环境，忽略注解
        //2.如果系统整体开启了，根据注解值进行判断是否拦截并提示，演示环境不允许操作
        if (isOpen) {
            throw new DemoEnvironmentNotAllowException();
        }
    }

    /**
     * Get the order value of this object.
     * @return 排序
     */
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 100;
    }
}
