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
package net.linlan.emis.support.manage.trace;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.JdkIdGenerator;

import lombok.Data;

import net.linlan.commons.core.RequestIdUtils;

/**
 * 日志追踪加入MDC
 * @author Linlan
 */
@Data
@Aspect
@Component
public class TraceAspect {

    public static final String   TRACE_ID       = "TRACE_ID";
    private final JdkIdGenerator jdkIdGenerator = new JdkIdGenerator();

    /** 之前操作
     * @param joinPoint     切面入口
     * @param trace     跟踪器
     */
    @Before(value = "@annotation(trace)")
    public void logBefore(final JoinPoint joinPoint, Trace trace) {
        //添加MDC
        RequestIdUtils.put(jdkIdGenerator.generateId().toString());
    }

    /**
     * 之后操作
     *
     * @param trace 跟踪器
     */
    @After(value = "@annotation(trace)")
    public void logDoAfterReturning(Trace trace) {
        //清除MDC
        RequestIdUtils.remove();
    }

}
