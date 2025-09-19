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
package net.linlan.emis.core.storage;

import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Linlan
 */
public class AbstractStorage implements Storage {
    protected StorageConfig    config;
    public static final String SEPARATOR      = "/";
    public static final String JOINER         = "-";
    public static final int    EXPIRY_SECONDS = 3600;
    /**
     * url 正则
     */
    public static final String URL_REGEXP     = "^https?://[\\w.-]+(:\\d+)?$";

    public AbstractStorage(StorageConfig config) {
        this.config = config;
    }

    /**
     * 判断域名是否为https
     *
     * @param url {@link String}
     * @return {@link Boolean}
     */
    public boolean getUrlSecure(String url) {
        return "https:".equals(url.split("//")[0]);
    }

    public String getFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            fileName = "";
        }
        return UUID.randomUUID().toString().replace(JOINER, "").toLowerCase() + JOINER + fileName;
    }

    @Override
    public String upload(String fileName, InputStream inputStream) throws StorageProviderException {
        return null;
    }

    @Override
    public String download(String path) throws StorageProviderException {
        return null;
    }
}
