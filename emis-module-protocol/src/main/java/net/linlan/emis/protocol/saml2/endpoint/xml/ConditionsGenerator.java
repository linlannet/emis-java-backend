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

import java.time.Instant;

import org.joda.time.DateTime;
import org.opensaml.saml.saml2.core.Audience;
import org.opensaml.saml.saml2.core.AudienceRestriction;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.impl.AudienceBuilder;
import org.opensaml.saml.saml2.core.impl.AudienceRestrictionBuilder;
import org.opensaml.saml.saml2.core.impl.ConditionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;

/**
 * 条件生成器
 *
 * @author Linlan
 * Created on  2022/6/1 22:51
 */
@AllArgsConstructor
public class ConditionsGenerator {
    private final static Logger logger = LoggerFactory.getLogger(ConditionsGenerator.class);
    /**
     * audienceUri
     */
    private final String        audienceUri;
    /**
     * 设置此主题无效的时间
     */
    private final Instant       notBefore;

    /**
     * 设置此主题无效的时间或之后的时间
     */
    private final Instant       notOnOrAfter;

    public Conditions generateConditions() {
        Conditions conditions = new ConditionsBuilder().buildObject();
        conditions.setNotBefore(new DateTime(notBefore));
        conditions.setNotOnOrAfter(new DateTime(notOnOrAfter));
        AudienceRestriction audienceRestriction = new AudienceRestrictionBuilder().buildObject();
        Audience audience = new AudienceBuilder().buildObject();
        //TODO shuxiaobo setURI修改为setAudienceURI
        audience.setAudienceURI(audienceUri);
        audienceRestriction.getAudiences().add(audience);
        conditions.getAudienceRestrictions().add(audienceRestriction);
        return conditions;
    }
}
