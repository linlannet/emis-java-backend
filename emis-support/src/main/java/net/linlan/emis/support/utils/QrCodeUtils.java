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
package net.linlan.emis.support.utils;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 *
 * @author Linlan
 */
public class QrCodeUtils {
    public static String BASE64_URL = "data:image/png;base64,";

    /**
     * 创建二维码
     * @param str       字符串
     * @param width     宽度
     * @param height    高度
     * @return 二维码字符串
     */
    public static String createQrCode(String str, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            HashMap<EncodeHintType, Object> hints = new HashMap<>(16);
            hints.put(EncodeHintType.CHARACTER_SET, UTF_8.name());
            BitMatrix bitMatrix = qrCodeWriter.encode(str, BarcodeFormat.QR_CODE, width, height,
                hints);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            Base64.Encoder encoder = Base64.getEncoder();
            String text = encoder.encodeToString(outputStream.toByteArray());
            return BASE64_URL + text;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
