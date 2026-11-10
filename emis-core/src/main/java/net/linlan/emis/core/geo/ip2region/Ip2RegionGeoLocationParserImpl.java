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
package net.linlan.emis.core.geo.ip2region;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.core.constant.Country;
import net.linlan.emis.core.geo.District;
import net.linlan.emis.support.userdetails.geo.GeoLocation;
import net.linlan.emis.support.userdetails.geo.GeoLocationParser;
import net.linlan.emis.support.userdetails.geo.GeoLocationProvider;
import net.linlan.utils.ip.IPUtils;
import static net.linlan.emis.support.userdetails.geo.GeoLocationProvider.IP2REGION;

/**
 * Ip2regionGeo
 *
 * @author Linlan
 */
@Slf4j
public class Ip2RegionGeoLocationParserImpl implements GeoLocationParser, DisposableBean {

    private static final String DEFAULT_XDB_PATH = "/ip2region/ip2region.xdb";

    private final Searcher      searcher;

    public Ip2RegionGeoLocationParserImpl() throws IOException {
        // 加载整个xdb到内存。
        try (InputStream in = this.getClass().getResourceAsStream(DEFAULT_XDB_PATH)) {
            Assert.notNull(in, "XDB must not be null");
            //正确读取字节数组
            byte[] cBuff = new byte[in.available()];
            int bytesRead = in.read(cBuff);
            if (bytesRead != cBuff.length) {
                throw new IOException("Failed to read the entire xdb file");
            }

            log.info("create content cached searcher, file size: {} bytes", cBuff.length);

            //使用正确的方法创建搜索器
            searcher = Searcher.newWithBuffer(cBuff);
        }
    }

    @Override
    public GeoLocation getGeoLocation(String remote) {
        if (IPUtils.internalIp(remote)) {
            log.info("内网IP不查询:({})", remote);
            return GeoLocation.builder().ip(remote).provider(GeoLocationProvider.NONE.getCode())
                .build();
        }

        try {
            //添加IP格式验证
            if (!IPUtils.isIP(remote)) {
                log.warn("Invalid IP address format: {}", remote);
                return GeoLocation.builder().provider(IP2REGION.getCode()).ip(remote).build();
            }

            String region = searcher.search(remote);
            log.debug("IP {} search result: {}", remote, region);
            return buildGeoLocation(remote, region);
        } catch (Exception e) {
            log.error("failed to search({})", remote, e);
            //返回一个默认的地理位置对象而不是空的
            return GeoLocation.builder().provider(IP2REGION.getCode()).ip(remote).countryName("中国")
                .build();
        }
    }

    private GeoLocation buildGeoLocation(String remote, String region) {
        String[] geo = region.split("\\|");

        //处理可能的分段不足情况
        if (geo.length < 5) {
            log.warn("Incomplete region data for IP {}: {}", remote, region);
            return GeoLocation.builder().provider(IP2REGION.getCode()).ip(remote)
                .countryName(geo.length > 0 ? geo[0] : "未知")
                .provinceName(geo.length > 2 ? geo[2] : "未知")
                .cityName(geo.length > 3 ? geo[3] : "未知").build();
        }

        String countryName = geo[0];
        String provinceName = geo[2];
        String cityName = geo[3];
        Country country = Country.getByCountryName(countryName);

        return GeoLocation.builder().provider(IP2REGION.getCode()).ip(remote)
            .countryCode(Objects.nonNull(country) ? country.getKey() : null)
            .countryName(countryName)
            .provinceCode(Objects.toString(District.PROVINCE_DISTRICT.get(provinceName), null))
            .provinceName(provinceName)
            .cityCode(Objects.toString(District.CITY_DISTRICT.get(cityName), null))
            .cityName(cityName).build();
    }

    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(searcher)) {
            try {
                searcher.close();
                log.info("IP2Region searcher closed successfully");
            } catch (Exception e) {
                log.warn("Error closing IP2Region searcher", e);
            }
        }
    }

}
