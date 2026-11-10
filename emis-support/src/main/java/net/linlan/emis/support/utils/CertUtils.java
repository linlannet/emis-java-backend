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
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStrictStyle;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.linlan.emis.support.exception.SupportException;
import okhttp3.CertificatePinner;

/**
 * 证书工具类，读取证书和私钥的工具类
 *
 *
 * @author Linlan
 */
public class CertUtils {
    private static final Logger logger     = LoggerFactory.getLogger(CertUtils.class);
    public static final String  BEGIN_CERT = "-----BEGIN CERTIFICATE-----\n";
    public static final String  END_CERT   = "-----END CERTIFICATE-----";
    public static final String  BEGIN_KEY  = "-----BEGIN RSA PRIVATE KEY-----\n";
    public static final String  END_KEY    = "-----END RSA PRIVATE KEY-----";
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     *  颁发者 或者 申请者 信息封装
     * @param cn 公用名
     *              对于 SSL 证书，一般为网站域名；而对于代码签名证书则为申请单位名称；而对于客户端证书则为证书申请者的姓名
     * @param o 组织
     *              对于 SSL 证书，一般为网站域名；而对于代码签名证书则为申请单位名称；而对于客户端单位证书则为证书申请者所在单位名称；
     * @param s 城市
     * @param st 省/ 市/ 自治区名
     * @param c 国家
     * @param ou 组织单位/显示其他内容
     * @return {@link  X500Name}
     */
    public static X500Name getX500Name(String cn, String o, String s, String st, String c,
                                       String ou) {
        X500NameBuilder rootIssueMessage = new X500NameBuilder(BCStrictStyle.INSTANCE);
        rootIssueMessage.addRDN(BCStyle.CN, cn);
        rootIssueMessage.addRDN(BCStyle.O, o);
        rootIssueMessage.addRDN(BCStyle.L, s);
        rootIssueMessage.addRDN(BCStyle.ST, st);
        rootIssueMessage.addRDN(BCStyle.C, c);
        rootIssueMessage.addRDN(BCStyle.OU, ou);
        return rootIssueMessage.build();
    }

    /**
     *  根据如下参数获取对应base64编码格式的证书文件字符串
     *      issuerName 与 reqName 对象是同一个则认为生成的是CA证书
     * @param issuerName 颁发者信息
     * @param reqName   请求证主题信息：issuerName，reqName，CA
     * @param serial 证书序列号，eg: BigInteger serial = BigInteger.valueOf(System.currentTimeMillis() / 1000);
     * @param notBefore 有效期开始时间  2018-08-01 00:00:00
     * @param notAfter 有效期截至时间   2028-08-01 00:00:00
     * @param userPublicKey 请求者主题公钥信息
     * @param rootPrivateKey   颁发者私钥信息
     * @return 获取对应base64编码格式的证书文件字符串
     * @throws OperatorCreationException    异常
     * @throws CertificateException     异常
     * @throws IOException      异常
     */
    public static String getCertificate(X500Name issuerName, X500Name reqName, BigInteger serial,
                                        Date notBefore, Date notAfter, PublicKey userPublicKey,
                                        PrivateKey rootPrivateKey) throws OperatorCreationException,
                                                                   CertificateException,
                                                                   IOException {
        JcaX509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(
            issuerName, serial, notBefore, notAfter, reqName, userPublicKey);
        // 签发者 与 使用者 信息一致则是CA证书生成，增加CA 基本约束属性
        if (issuerName == reqName) {
            BasicConstraints constraint = new BasicConstraints(1);
            x509v3CertificateBuilder.addExtension(Extension.basicConstraints, false, constraint);
        }
        //签名的工具
        ContentSigner signer = new JcaContentSignerBuilder("SHA256WITHRSA").setProvider("BC")
            .build(rootPrivateKey);
        //触发签名产生用户证书
        X509CertificateHolder x509CertificateHolder = x509v3CertificateBuilder.build(signer);
        JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter();
        certificateConverter.setProvider("BC");
        Certificate userCertificate = certificateConverter.getCertificate(x509CertificateHolder);
        return getCertificate(userCertificate);
    }

    /**
     *  Certificate 转 证书
     *
     * @param certificate {@link  Certificate}
     * @return {@link String}
     */
    public static String getCertificate(Certificate certificate) {
        return encodePem(certificate);
    }

    public static String encodePem(Certificate certificate) {
        try {
            return toPem("CERTIFICATE", certificate.getEncoded());
        } catch (CertificateEncodingException e) {
            throw new SupportException("证书编码异常", e);
        }
    }

    public static String encodePem(PrivateKey privateKey) {
        return toPem("PRIVATE KEY", privateKey.getEncoded());
    }

    public static String encodePem(PublicKey publicKey) {
        return toPem("PUBLIC KEY", publicKey.getEncoded());
    }

    protected static String toPem(String type, byte[] data) {
        final PemObject pemObject = new PemObject(type, data);
        final StringWriter sw = new StringWriter();
        try (final PemWriter pw = new PemWriter(sw)) {
            pw.writeObject(pemObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sw.toString();
    }

    /**
     * keyCleanup
     *
     * @param pem {@link String}
     * @return {@link String}
     */
    public static String keyCleanup(String pem) {
        return pem.replace(BEGIN_CERT, "").replace(END_CERT, "").replace(BEGIN_KEY, "")
            .replace(END_KEY, "").replace("\n", "").trim();
    }

    /**
     * 获取证书
     *
     * @param pem {@link String}
     * @return {@link X509Certificate}
     */
    public static X509Certificate loadCertFromString(String pem) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            byte[] re = Base64.decode(keyCleanup(pem));
            ByteArrayInputStream bain = new ByteArrayInputStream(re);
            return (X509Certificate) cf.generateCertificate(bain);
        } catch (CertificateException | NoSuchProviderException e) {
            logger.error(e.getMessage());
            throw new SupportException(e.getMessage(), e);
        }

    }

    public static X509Certificate loadCertFromInputStream(InputStream inputStream) throws Exception {
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certificateFactory.generateCertificate(inputStream);
        } catch (CertificateException e) {
            logger.error("证书无法识别，证书类型必须是X.509");
            throw new Exception("证书无法识别，证书类型必须是X.509", e);
        }
    }

    public static String getSubjectDN(X509Certificate certificate) {
        String s = certificate.getSubjectX500Principal().toString();
        s = s.substring("CN=".length(), s.indexOf(','));
        return s;
    }

    public static String getIssuerDN(X509Certificate certificate) {
        String s = certificate.getIssuerX500Principal().toString();
        s = s.substring("CN=".length(), s.indexOf(','));
        return s;
    }

    public static String getNotAfter(X509Certificate certificate) {
        return certificate.getNotAfter().toString();
    }

    public static String getPin(X509Certificate certificate) {
        String pin = CertificatePinner.pin(certificate);
        pin = pin.substring("sha256/".length());
        return pin;
    }

    public static String isRoot(X509Certificate certificate) {
        return String.valueOf(
            certificate.getSubjectX500Principal().equals(certificate.getIssuerX500Principal()));
    }

    public static byte[] getDer(String combinedKeyAndCertPem, String begin, String end) {
        String[] tokens = combinedKeyAndCertPem.split(begin);
        tokens = tokens[0].split(end);
        return getDer(tokens[0]);
    }

    public static byte[] getDer(String pem) {
        String data = keyCleanup(pem);

        return DatatypeConverter.parseBase64Binary(data);
    }

    public static X509Certificate getCertificate(byte[] der) throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(der));
    }

    public static RSAPrivateKey getPrivateKey(byte[] der,
                                              String algorithm) throws InvalidKeySpecException,
                                                                NoSuchAlgorithmException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        KeyFactory factory = KeyFactory.getInstance(algorithm);
        return (RSAPrivateKey) factory.generatePrivate(spec);
    }

    public static PrivateKey readPrivateKey(String pem, String passphrase) throws IOException {

        PEMParser parser = new PEMParser(new CharArrayReader(pem.toCharArray()));
        Object obj = parser.readObject();
        parser.close();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        KeyPair kp;
        if (obj == null) {
            throw new NullPointerException("Unable to decode PEM key:" + pem);
        } else if (obj instanceof PEMEncryptedKeyPair) {
            // Encrypted key - we will use provided password
            PEMEncryptedKeyPair ckp = (PEMEncryptedKeyPair) obj;
            PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder()
                .build(passphrase.toCharArray());
            kp = converter.getKeyPair(ckp.decryptKeyPair(decProv));
        } else if (obj instanceof PrivateKeyInfo) {
            PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) obj;
            return converter.getPrivateKey(privateKeyInfo);
        } else {
            // Unencrypted key - no password needed
            PEMKeyPair ukp = (PEMKeyPair) obj;
            kp = converter.getKeyPair(ukp);
        }
        return kp.getPrivate();
    }

    public static PublicKey readPublicKey(String pem, String passphrase) throws IOException {

        PEMParser parser = new PEMParser(new CharArrayReader(pem.toCharArray()));
        Object obj = parser.readObject();
        parser.close();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        KeyPair kp;
        if (obj == null) {
            throw new NullPointerException("Unable to decode PEM key:" + pem);
        } else if (obj instanceof PEMEncryptedKeyPair) {
            // Encrypted key - we will use provided password
            PEMEncryptedKeyPair ckp = (PEMEncryptedKeyPair) obj;
            PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder()
                .build(passphrase.toCharArray());
            kp = converter.getKeyPair(ckp.decryptKeyPair(decProv));
        } else if (obj instanceof SubjectPublicKeyInfo) {
            SubjectPublicKeyInfo privateKeyInfo = (SubjectPublicKeyInfo) obj;
            return converter.getPublicKey(privateKeyInfo);
        } else {
            // Unencrypted key - no password needed
            PEMKeyPair ukp = (PEMKeyPair) obj;
            kp = converter.getKeyPair(ukp);
        }
        return kp.getPublic();
    }

}
