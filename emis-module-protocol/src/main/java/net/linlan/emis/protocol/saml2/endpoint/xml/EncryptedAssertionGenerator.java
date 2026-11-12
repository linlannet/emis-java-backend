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
package net.linlan.emis.protocol.saml2.endpoint.xml;

import org.apache.xml.security.utils.EncryptionConstants;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.EncryptedAssertion;
import org.opensaml.saml.saml2.encryption.Encrypter;
import org.opensaml.security.credential.Credential;
import org.opensaml.xmlsec.encryption.support.DataEncryptionParameters;
import org.opensaml.xmlsec.encryption.support.EncryptionException;
import org.opensaml.xmlsec.encryption.support.KeyEncryptionParameters;

import lombok.AllArgsConstructor;

import net.linlan.emis.protocol.constant.SamlEncryptAssertAlgorithmType;

/**
 * EncryptedAssertionGenerator
 *
 * @author Linlan
 * Created on  2022/6/1 22:05
 */
@AllArgsConstructor
public class EncryptedAssertionGenerator {
    /**
     * 断言
     */
    private final Assertion                      assertion;

    /**
     * 加密算法
     */
    private final SamlEncryptAssertAlgorithmType encryptAlgorithm;

    /**
     * 加密证书
     */
    private final Credential                     encryptionCredential;

    /**
     * @return {@link EncryptedAssertion}
     */
    public EncryptedAssertion generateEncryptAssertion() {

        DataEncryptionParameters encryptionParameters = new DataEncryptionParameters();
        encryptionParameters.setAlgorithm(encryptAlgorithm.getValue());

        KeyEncryptionParameters keyEncryptionParameters = new KeyEncryptionParameters();
        keyEncryptionParameters.setEncryptionCredential(encryptionCredential);
        keyEncryptionParameters.setAlgorithm(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP);

        Encrypter encrypter = new Encrypter(encryptionParameters, keyEncryptionParameters);
        encrypter.setKeyPlacement(Encrypter.KeyPlacement.INLINE);

        try {
            return encrypter.encrypt(assertion);
        } catch (EncryptionException e) {
            throw new RuntimeException(e);
        }
    }
}
