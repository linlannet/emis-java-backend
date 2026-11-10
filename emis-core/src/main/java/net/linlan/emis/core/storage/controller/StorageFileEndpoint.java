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
package net.linlan.emis.core.storage.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.MultipartConfigElement;

import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.core.constant.ViewContentType;
import net.linlan.emis.core.storage.Storage;
import static net.linlan.emis.core.storage.StorageConstants.STORAGE_GROUP_NAME;
import static net.linlan.emis.core.storage.StorageConstants.STORAGE_PATH;

/**
 * 存储配置
 *
 * @author Linlan
 */
@Validated
@Tag(name = STORAGE_GROUP_NAME + "-" + "存储文件管理")
@RestController
@RequestMapping(value = STORAGE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class StorageFileEndpoint {

    /**
     * 上传文件
     * @param file  上传文件
     * @return {@link ResponseResult}
     */
    @Operation(summary = "上传文件")
    @PostMapping(value = "/upload")
    public ResponseResult<Object> uploadFile(@Schema(description = "文件") MultipartFile file) {
        try {
            if (storage == null) {
                return ResponseResult.error("暂未开启存储服务，请联系管理员");
            }
            String fileName = new String(Objects.requireNonNull(file.getOriginalFilename())
                .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String fileType = file.getContentType();
            List<String> allowedTypes = Arrays.asList(MimeTypeUtils.IMAGE_JPEG_VALUE,
                MimeTypeUtils.IMAGE_PNG_VALUE, ViewContentType.SVG.getType(),
                "image/vnd.microsoft.icon");
            if (Objects.isNull(fileType) || !allowedTypes.contains(fileType)) {
                return ResponseResult.error("文件类型不支持，请上传图片");
            }
            return ResponseResult.ok(storage.upload(fileName, file.getInputStream()));
        } catch (Exception e) {
            log.error("Failed to upload storage file: {}", e.getMessage(), e);
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 获取存储文件路径
     * @param path  存储文件路径
     * @return {@link ResponseResult}
     */
    @Operation(summary = "获取存储文件路径")
    @GetMapping(value = "/get")
    public ResponseResult<Object> getUrl(@Schema(description = "文件路径") @RequestParam String path) {
        try {
            if (storage == null) {
                return ResponseResult.error("暂未开启存储服务，请联系管理员");
            }
            return ResponseResult.ok(storage.download(path));
        } catch (Exception e) {
            log.error("Failed to get storage file: {}", e.getMessage(), e);
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 上传文件过大
     *
     * @return {@link ResponseResult}
     */
    @ExceptionHandler(value = { MaxUploadSizeExceededException.class })
    public ResponseResult<Object> maxUploadSizeExceededException() {
        return ResponseResult
            .error("文件大小不能超过:" + multipartConfigElement.getMaxFileSize() / 1024 / 1024 + "M");
    }

    /**
     * Storage
     */
    private final Storage                storage;

    private final MultipartConfigElement multipartConfigElement;
}
