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
package net.linlan.emis.core.geo.maxmind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import net.linlan.emis.support.userdetails.geo.GeoLocationParser;

/**
 * 更新 maxmind 数据库
 *
 * @author Linlan
 */
@Component
@RequiredArgsConstructor
public class UpdateMaxmindTaskConfiguration {
    private final Logger logger = LoggerFactory.getLogger(UpdateMaxmindTaskConfiguration.class);

    /**
     * 每天凌晨一点执行ip库文件更新
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void update() {
        try {
            if (geoLocationParser instanceof MaxmindGeoLocationParserImpl) {
                logger.info("执行IP库文件更新定时任务开始");
                MaxmindGeoLocationParserImpl maxmindGeoLocation = (MaxmindGeoLocationParserImpl) geoLocationParser;
                if (maxmindGeoLocation.checkDbFileIsUpdate()) {
                    maxmindGeoLocation.download();
                }
                logger.info("执行IP库文件更新定时任务结束");
            }
        } catch (Exception e) {
            logger.error("执行IP库文件更新定时发生异常：{}", e.getMessage());
        }
    }

    private final GeoLocationParser geoLocationParser;

}
