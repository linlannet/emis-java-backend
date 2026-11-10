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
package net.linlan.emis.core.storage.impl;

import java.io.InputStream;

import net.linlan.emis.core.storage.Storage;
import net.linlan.emis.core.storage.StorageProviderException;

/**
 * 本地存储配置
 *
 * @author Linlan
 *
  */
public class NoneStorage implements Storage {

    public NoneStorage() {
    }

    @Override
    public String upload(String fileName, InputStream inputStream) throws StorageProviderException {
        throw new StorageProviderException("暂未配置存储提供商");
    }

    @Override
    public String download(String path) throws StorageProviderException {
        throw new StorageProviderException("暂未配置存储提供商");
    }
}
