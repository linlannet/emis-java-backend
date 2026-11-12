/*
 * emis-module-audit - audit安全审计
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
package net.linlan.emis.audit.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.audit.repository.result.AuditStatisticsResult;
import net.linlan.emis.audit.repository.result.AuthnQuantityResult;
import net.linlan.emis.audit.repository.result.AuthnZoneResult;

/**
 * 审计统计持久层，基于 JDBC 的聚合查询
 *
 * @author Linlan
 */
@Repository
@RequiredArgsConstructor
public class AuditStatisticsRepository {

    private static final String              AUDIT_TABLE = "EMIS_AUDIT_INFO";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 统计指定事件类型在时间范围内的操作者认证方式分布
     *
     * @param types     {@link List}
     * @param startTime {@link LocalDateTime}
     * @param endTime   {@link LocalDateTime}
     * @return {@link List}
     */
    public List<AuditStatisticsResult> authnHotProvider(List<EventType> types,
                                                        LocalDateTime startTime,
                                                        LocalDateTime endTime) {
        String sql = "SELECT ACTOR_AUTH_TYPE AS key_, COUNT(*) AS COUNT_ FROM " + AUDIT_TABLE
                     + " WHERE EVENT_TYPE IN (:types)"
                     + " AND EVENT_TIME BETWEEN :startTime AND :endTime"
                     + " GROUP BY ACTOR_AUTH_TYPE ORDER BY COUNT_";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("types", toCodes(types));
        params.addValue("startTime", startTime);
        params.addValue("endTime", endTime);
        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            AuditStatisticsResult result = new AuditStatisticsResult();
            if (StringUtils.isNoneBlank(rs.getString("key_"))) {
                result.setKey(rs.getString("key_").replace("\"", ""));
            }
            result.setCount(rs.getLong("COUNT_"));
            return result;
        });
    }

    /**
     * 按时间格式统计认证量
     *
     * @param types      {@link List}
     * @param startTime  {@link LocalDateTime}
     * @param endTime    {@link LocalDateTime}
     * @param dateFormat {@link String}
     * @return {@link List}
     */
    public List<AuthnQuantityResult> authnQuantity(List<EventType> types, LocalDateTime startTime,
                                                   LocalDateTime endTime, String dateFormat) {
        String sql = "SELECT DATE_FORMAT(EVENT_TIME, :dateFormat) AS NAME, COUNT(*) AS COUNT_,"
                     + " EVENT_STATUS AS STATUS FROM " + AUDIT_TABLE
                     + " WHERE EVENT_TYPE IN (:types)"
                     + " AND EVENT_TIME BETWEEN :startTime AND :endTime"
                     + " GROUP BY DATE_FORMAT(EVENT_TIME, :dateFormat), EVENT_STATUS ORDER BY NAME";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("types", toCodes(types));
        params.addValue("startTime", startTime);
        params.addValue("endTime", endTime);
        params.addValue("dateFormat", dateFormat);
        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            AuthnQuantityResult result = new AuthnQuantityResult();
            result.setName(rs.getString("NAME"));
            result.setCount(rs.getLong("COUNT_"));
            result.setStatus(rs.getString("STATUS"));
            return result;
        });
    }

    /**
     * 应用访问排名
     *
     * @param type      {@link EventType}
     * @param startTime {@link LocalDateTime}
     * @param endTime   {@link LocalDateTime}
     * @return {@link List}
     */
    public List<AuditStatisticsResult> appVisitRank(EventType type, LocalDateTime startTime,
                                                    LocalDateTime endTime) {
        String sql = "SELECT JSON_EXTRACT(TARGET, '$[0].id') AS key_, COUNT(*) AS COUNT_ FROM "
                     + AUDIT_TABLE + " WHERE EVENT_TYPE = :type"
                     + " AND EVENT_TIME BETWEEN :startTime AND :endTime"
                     + " GROUP BY JSON_EXTRACT(TARGET, '$[0].id') ORDER BY COUNT_";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("type", type.getCode());
        params.addValue("startTime", startTime);
        params.addValue("endTime", endTime);
        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            AuditStatisticsResult result = new AuditStatisticsResult();
            if (StringUtils.isNoneBlank(rs.getString("key_"))) {
                result.setKey(rs.getString("key_").replace("\"", ""));
            }
            result.setCount(rs.getLong("COUNT_"));
            return result;
        });
    }

    /**
     * 登录区域分布
     *
     * @param types     {@link List}
     * @param startTime {@link LocalDateTime}
     * @param endTime   {@link LocalDateTime}
     * @return {@link List}
     */
    public List<AuthnZoneResult> authnZone(List<EventType> types, LocalDateTime startTime,
                                           LocalDateTime endTime) {
        String sql = "SELECT JSON_EXTRACT(GEO_LOCATION, '$.provinceCode') AS key_, COUNT(*) AS"
                     + " COUNT_ FROM " + AUDIT_TABLE + " WHERE EVENT_TYPE IN (:types)"
                     + " AND EVENT_TIME BETWEEN :startTime AND :endTime"
                     + " GROUP BY JSON_EXTRACT(GEO_LOCATION, '$.provinceCode')";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("types", toCodes(types));
        params.addValue("startTime", startTime);
        params.addValue("endTime", endTime);
        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            AuthnZoneResult result = new AuthnZoneResult();
            if (StringUtils.isNoneBlank(rs.getString("key_"))) {
                result.setName(rs.getString("key_").replace("\"", ""));
            }
            result.setCount(rs.getLong("COUNT_"));
            return result;
        });
    }

    /**
     * 统计指定事件类型在时间范围内的数量
     *
     * @param type      {@link EventType}
     * @param startTime {@link LocalDateTime}
     * @param endTime   {@link LocalDateTime}
     * @return long
     */
    public long countByTypeAndTime(EventType type, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT COUNT(*) FROM " + AUDIT_TABLE + " WHERE EVENT_TYPE = :type"
                     + " AND EVENT_TIME BETWEEN :startTime AND :endTime";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("type", type.getCode());
        params.addValue("startTime", startTime);
        params.addValue("endTime", endTime);
        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count == null ? 0L : count;
    }

    private List<String> toCodes(List<EventType> types) {
        return types.stream().map(EventType::getCode).collect(Collectors.toList());
    }
}
