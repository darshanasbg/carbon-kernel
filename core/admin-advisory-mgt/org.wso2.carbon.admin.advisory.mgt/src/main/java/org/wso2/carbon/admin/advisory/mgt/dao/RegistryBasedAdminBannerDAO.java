/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.admin.advisory.mgt.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.advisory.mgt.dto.AdminAdvisoryBannerDTO;
import org.wso2.carbon.admin.advisory.mgt.exception.AdminAdvisoryMgtException;
import org.wso2.carbon.admin.advisory.mgt.util.RegistryResourceConfig;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.ResourceImpl;

import static org.wso2.carbon.admin.advisory.mgt.constants.AdminAdvisoryManagementConstants.BANNER_CONTENT;
import static org.wso2.carbon.admin.advisory.mgt.constants.AdminAdvisoryManagementConstants.ENABLE_BANNER;

/**
 * This class is used to manage storage of the Admin Advisory Banner configurations in the registry.
 */
public class RegistryBasedAdminBannerDAO implements AdminAdvisoryBannerDAO {

    protected static final Log LOG = LogFactory.getLog(RegistryBasedAdminBannerDAO.class);
    private static final String ADMIN_ADVISORY_BANNER_PATH = "identity/config/adminAdvisoryBanner";
    private final RegistryResourceConfig registryResourceConfig = new RegistryResourceConfig();

    @Override
    public void saveAdminAdvisoryConfig(AdminAdvisoryBannerDTO adminAdvisoryBanner, String tenantDomain)
            throws AdminAdvisoryMgtException {

        Resource bannerResource = createAdminBannerRegistryResource(adminAdvisoryBanner);
        registryResourceConfig.putRegistryResource(bannerResource, ADMIN_ADVISORY_BANNER_PATH, tenantDomain);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Admin advisory banner configuration saved successfully in registry for tenant: " + tenantDomain);
        }
    }

    @Override
    public AdminAdvisoryBannerDTO loadAdminAdvisoryConfig(String tenantDomain) throws AdminAdvisoryMgtException {

        Resource resource = registryResourceConfig.getRegistryResource(ADMIN_ADVISORY_BANNER_PATH, tenantDomain);
        if (resource == null) {
            return null;
        }

        AdminAdvisoryBannerDTO adminAdvisoryBanner = createAdminAdvisoryBannerDTO(resource);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Admin advisory banner configuration loaded successfully from registry for tenant: " +
                    tenantDomain);
        }
        return adminAdvisoryBanner;
    }

    /**
     * This method is used to convert AdminAdvisoryBannerDTO to Resource object to be saved in registry.
     *
     * @return Resource object.
     */
    private Resource createAdminBannerRegistryResource(AdminAdvisoryBannerDTO adminAdvisoryBannerDTO) {

        Resource bannerResource = new ResourceImpl();
        bannerResource.setProperty(ENABLE_BANNER, String.valueOf(adminAdvisoryBannerDTO.getEnableBanner()));
        bannerResource.setProperty(BANNER_CONTENT, String.valueOf(adminAdvisoryBannerDTO.getBannerContent()));
        return bannerResource;
    }

    /**
     * This method is used to convert Resource object to AdminAdvisoryBannerDTO to be saved in registry.
     *
     * @return AdminAdvisoryBannerDTO object.
     */
    private AdminAdvisoryBannerDTO createAdminAdvisoryBannerDTO(Resource bannerResource) {

        String enableBanner = bannerResource.getProperty(ENABLE_BANNER);
        String content = bannerResource.getProperty(BANNER_CONTENT);

        AdminAdvisoryBannerDTO adminAdvisoryBannerDTO = new AdminAdvisoryBannerDTO();
        adminAdvisoryBannerDTO.setEnableBanner(Boolean.parseBoolean(enableBanner));
        adminAdvisoryBannerDTO.setBannerContent(content);
        return adminAdvisoryBannerDTO;
    }
}
