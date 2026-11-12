/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.base;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.linlan.emis.app.constant.AppCertUsingType;
import net.linlan.emis.app.entity.AppCertification;
import net.linlan.emis.core.exception.app.AppCreateCertException;
import net.linlan.emis.protocol.repository.*;
import net.linlan.emis.support.utils.CertUtils;
import net.linlan.emis.support.utils.RsaUtils;
import static net.linlan.emis.support.utils.CertUtils.encodePem;
import static net.linlan.emis.support.utils.CertUtils.getX500Name;
import static net.linlan.emis.support.utils.RsaUtils.getKeys;

/**
 * AbstractCertificateApplicationService
 *
 * @author Linlan
 * Created on 2022/8/31 22:34
 */
public abstract class AbstractCertApplicationService extends AbstractApplicationService {
    private final Logger         logger = LoggerFactory
        .getLogger(AbstractCertApplicationService.class);
    protected final ObjectMapper mapper = new ObjectMapper();

    /**
     * 创建证书
     *
     * @param appId     {@link String}
     * @param appCode     {@link String}
     * @param usingType {@link AppCertUsingType}
     */
    public void createCertificate(String appId, String appCode, AppCertUsingType usingType) {
        try {
            AppCertification config = new AppCertification();
            config.setAppId(appId);
            //私钥长度
            config.setKeyLong(2048);
            //算法
            config.setSignAlgo("SHA256WITHRSA");
            RsaUtils.RsaResult keys = getKeys(config.getKeyLong());
            X500Name x500Name = getX500Name("app_" + appCode, "EMIS", "Xuhui", "Shanghai", "CN",
                "EMIS");
            //发行者
            config.setIssuer(x500Name.toString());
            //主题
            config.setSubject(x500Name.toString());
            //证书 起始日期 与 结束日期
            LocalDateTime localDateTime = LocalDateTime.now();
            //证书序列号
            config.setSerial(System.currentTimeMillis());
            //开始时间
            Date notBefore = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            config.setBeginDate(notBefore);
            //结束时间
            Date notAfter = Date
                .from(localDateTime.plusYears(10).atZone(ZoneId.systemDefault()).toInstant());
            config.setEndDate(notAfter);
            //相差天数
            config
                .setValidity((int) ((notAfter.getTime() - notBefore.getTime()) / 1000 / 3600 / 24));
            //生成证书
            String certificate = CertUtils.getCertificate(x500Name, x500Name,
                BigInteger.valueOf(config.getSerial()), notBefore, notAfter, keys.getPublicKey(),
                keys.getPrivateKey());
            //私钥
            config.setPrivateKey(encodePem(keys.getPrivateKey()));
            //公钥
            config.setPublicKey(encodePem(keys.getPublicKey()));
            //证书
            config.setCertification(certificate);
            //使用类型
            config.setUsingType(usingType.getKey());
            appCertRepository.save(config);
        } catch (Exception e) {
            logger.error("创建应用证书异常", e);
            throw new AppCreateCertException();
        }
    }

    /**
     * AppCertRepository
     */
    protected final AppCertRepository         appCertRepository;

    /**
     *AppAccessPolicyRepository
     */
    protected final AppAccessPolicyRepository appAccessPolicyRepository;

    /**
     * IdGenerator
     */
    protected final IdGenerator               idGenerator;

    protected AbstractCertApplicationService(AppCertRepository appCertRepository,
                                             AppGroupAssociationRepository appGroupAssociationRepository,
                                             AppAccountRepository appAccountRepository,
                                             AppAccessPolicyRepository appAccessPolicyRepository,
                                             AppRepository appRepository) {
        super(appAccountRepository, appGroupAssociationRepository, appRepository);
        this.appCertRepository = appCertRepository;
        this.appAccessPolicyRepository = appAccessPolicyRepository;
        this.idGenerator = new AlternativeJdkIdGenerator();
    }
}
