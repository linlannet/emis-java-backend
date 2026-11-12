/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.linlan.emis.organ.entity.EmisOrganVo;
import net.linlan.emis.organ.service.EmisOrganService;
import net.linlan.emis.support.manage.init.AbstractSystemInitializer;
import net.linlan.emis.support.manage.init.InitializationException;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.emis.support.userdetails.OrganizationType;
import static net.linlan.emis.support.constant.EmisConstants.PATH_SEPARATOR;
import static net.linlan.emis.support.constant.EmisConstants.ROOT_NODE;
import static net.linlan.emis.support.userdetails.DataOrigin.INPUT;
import static net.linlan.emis.user.constant.AccountConstants.ROOT_DEPT_NAME;

/**
 * SystemInitializer
 *
 * @author Linlan
 * Created on 2024/04/04 21:24
 */
@Component
public class RootOrganizationInitializer extends AbstractSystemInitializer {

    private final Logger logger = LoggerFactory.getLogger(RootOrganizationInitializer.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() throws InitializationException {
        //@formatter:off
        EmisOrganVo optional = organizationService.getVoById(Long.valueOf(ROOT_NODE));
        if (optional == null) {
            logger.info("初始化父级组织");
            EmisOrganVo organization = new EmisOrganVo();
            organization.setId(Long.valueOf(ROOT_NODE));
            organization.setName(ROOT_DEPT_NAME);
            organization.setCode(ROOT_NODE);
            organization.setPath(PATH_SEPARATOR+ROOT_NODE);
            organization.setDisplayPath(PATH_SEPARATOR+ROOT_DEPT_NAME);
            organization.setDataOrigin(INPUT.getType());
            organization.setType(OrganizationType.GROUP);
            organization.setLeaf(false);
            organization.setEnabled(true);
            organization.setPriority(0);
            organization.setCreatorId(SecurityUtils.getCurrentUserName());
            organization.setCreateTime(new java.util.Date());
            organization.setRemark("Root organization");
            organizationService.saveByVo(organization);
        }
        //@formatter:on
    }

    @Override
    public int getOrder() {
        return 1;
    }

    /**
     * EmisOrganService
     */
    private final EmisOrganService organizationService;

    /**
     *
     * @param organizationService {@link EmisOrganService}
     */
    public RootOrganizationInitializer(EmisOrganService organizationService) {
        this.organizationService = organizationService;
    }
}
