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

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.core.storage.impl.*;
import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 存储提供商
 *
 * @author Linlan
 */
public enum StorageProvider implements BaseEnumString {

                                                       /**
                                                        * 阿里云
                                                        */
                                                       ALIYUN_OSS("aliyun_oss", "阿里云OSS",
                                                                  AliYunOssStorage.class),
                                                       /**
                                                        * 腾讯云
                                                        */
                                                       TENCENT_COS("tencent_cos", "腾讯云COS",
                                                                   TencentCosStorage.class),
                                                       /**
                                                        * 七牛
                                                        */
                                                       QINIU_KODO("qiniu_kodo", "七牛云Kodo",
                                                                  QiNiuKodoStorage.class),
                                                       /**
                                                        * minio
                                                        */
                                                       MINIO("minio", "minio", MinIoStorage.class),
                                                       /**
                                                        * S3
                                                        */
                                                       S3("s3", "s3", S3Storage.class);

    /**
     * key
     */
    @JsonValue
    private final String                   key;
    /**
     * value
     */
    private final String                   value;
    /**
     * Storage
     */
    private final Class<? extends Storage> storage;

    StorageProvider(String key, String value, Class<? extends Storage> storage) {
        this.key = key;
        this.value = value;
        this.storage = storage;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    public Class<? extends Storage> getStorage() {
        return storage;
    }

    @EnumConvert
    public static StorageProvider getType(String key) {
        StorageProvider[] values = values();
        for (StorageProvider status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        throw new NullPointerException("未找到该平台");
    }

    @Override
    public String toString() {
        return this.key;
    }
}
