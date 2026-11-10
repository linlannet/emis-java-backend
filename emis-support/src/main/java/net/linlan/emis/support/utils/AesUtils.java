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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.StringUtils;

import lombok.SneakyThrows;

/**
* AES 对称加密
* @author Linlan
*/
public class AesUtils {
    private static final String ALGORITHM   = "AES";
    private static final String DEFAULT_KEY = "fDx/JA3Aw9BIQClUSOddjA==";
    private final String        KEY;

    public AesUtils(String key) {
        this.KEY = key;
    }

    /**
     * 生成秘钥
     * @return  密钥信息
     */
    @SneakyThrows
    public static String generateKey() {
        KeyGenerator keygen;
        keygen = KeyGenerator.getInstance(ALGORITHM);
        // 16 字节 == 128 bit
        keygen.init(128, new SecureRandom());
        SecretKey secretKey = keygen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 生成密钥
     * @param secretKeyStr 输入密钥键
     * @return  密钥对象
     */
    private static SecretKeySpec getSecretKeySpec(String secretKeyStr) {
        return new SecretKeySpec(Base64.getDecoder().decode(secretKeyStr), ALGORITHM);
    }

    /**
     * 加密
     * @param content   被加密内容
     * @return  加密字符串
     */
    @SneakyThrows
    public static String encrypt(String content) {
        if (StringUtils.hasText(content)) {
            return encrypt(content, DEFAULT_KEY);
        }
        return null;
    }

    /**
     * 加密
     * @param content   被加密内容
     * @param secretKey   输入密钥键
     * @return  加密字符串
     */

    @SneakyThrows
    public static String encrypt(String content, String secretKey) {
        Key key = getSecretKeySpec(secretKey);
        // 创建密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder()
            .encodeToString(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 解密
     * @param content   被解密内容
     * @return  解密字符串
     */
    @SneakyThrows
    public static String decrypt(String content) {
        if (StringUtils.hasText(content)) {
            return decrypt(content, DEFAULT_KEY);
        }
        return null;
    }

    /**
     * 解密
     * @param content   被解密内容
     * @param secretKey   输入密钥键
     * @return  解密字符串
     */
    @SneakyThrows
    public static String decrypt(String content, String secretKey) {
        Key key = getSecretKeySpec(secretKey);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(content)),
            StandardCharsets.UTF_8);
    }

    public static String encrypt(String algorithm, String input, SecretKey key,
                                 IvParameterSpec iv) throws NoSuchPaddingException,
                                                     NoSuchAlgorithmException,
                                                     InvalidAlgorithmParameterException,
                                                     InvalidKeyException, BadPaddingException,
                                                     IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String algorithm, String cipherText, SecretKey key,
                                 IvParameterSpec iv) throws NoSuchPaddingException,
                                                     NoSuchAlgorithmException,
                                                     InvalidAlgorithmParameterException,
                                                     InvalidKeyException, BadPaddingException,
                                                     IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }

    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    public static SecretKey getKeyFromPassword(String password,
                                               String salt) throws NoSuchAlgorithmException,
                                                            InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static void encryptFile(String algorithm, SecretKey key, IvParameterSpec iv,
                                   File inputFile,
                                   File outputFile) throws IOException, NoSuchPaddingException,
                                                    NoSuchAlgorithmException,
                                                    InvalidAlgorithmParameterException,
                                                    InvalidKeyException, BadPaddingException,
                                                    IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[64];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }
        inputStream.close();
        outputStream.close();
    }

    public static void decryptFile(String algorithm, SecretKey key, IvParameterSpec iv,
                                   File encryptedFile,
                                   File decryptedFile) throws IOException, NoSuchPaddingException,
                                                       NoSuchAlgorithmException,
                                                       InvalidAlgorithmParameterException,
                                                       InvalidKeyException, BadPaddingException,
                                                       IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        FileInputStream inputStream = new FileInputStream(encryptedFile);
        FileOutputStream outputStream = new FileOutputStream(decryptedFile);
        byte[] buffer = new byte[64];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] output = cipher.doFinal();
        if (output != null) {
            outputStream.write(output);
        }
        inputStream.close();
        outputStream.close();
    }

    public static SealedObject encryptObject(String algorithm, Serializable object, SecretKey key,
                                             IvParameterSpec iv) throws NoSuchPaddingException,
                                                                 NoSuchAlgorithmException,
                                                                 InvalidAlgorithmParameterException,
                                                                 InvalidKeyException, IOException,
                                                                 IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return new SealedObject(object, cipher);
    }

    public static Serializable decryptObject(String algorithm, SealedObject sealedObject,
                                             SecretKey key,
                                             IvParameterSpec iv) throws NoSuchPaddingException,
                                                                 NoSuchAlgorithmException,
                                                                 InvalidAlgorithmParameterException,
                                                                 InvalidKeyException,
                                                                 ClassNotFoundException,
                                                                 BadPaddingException,
                                                                 IllegalBlockSizeException,
                                                                 IOException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return (Serializable) sealedObject.getObject(cipher);
    }

    public static String encryptPasswordBased(String plainText, SecretKey key,
                                              IvParameterSpec iv) throws NoSuchPaddingException,
                                                                  NoSuchAlgorithmException,
                                                                  InvalidAlgorithmParameterException,
                                                                  InvalidKeyException,
                                                                  BadPaddingException,
                                                                  IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public static String decryptPasswordBased(String cipherText, SecretKey key,
                                              IvParameterSpec iv) throws NoSuchPaddingException,
                                                                  NoSuchAlgorithmException,
                                                                  InvalidAlgorithmParameterException,
                                                                  InvalidKeyException,
                                                                  BadPaddingException,
                                                                  IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }

}
