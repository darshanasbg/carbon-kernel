package org.wso2.carbon.admin.advisory.mgt.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.advisory.mgt.constants.AdminAdvisoryManagementConstants;
import org.wso2.carbon.admin.advisory.mgt.dto.AdminAdvisoryBannerDTO;
import org.wso2.carbon.admin.advisory.mgt.exception.AdminAdvisoryMgtException;
import org.wso2.carbon.admin.advisory.mgt.internal.AdminAdvisoryManagementDataHolder;
import org.wso2.carbon.admin.advisory.mgt.service.AdminAdvisoryManagementService;
import org.wso2.carbon.admin.advisory.mgt.util.RegistryResourceConfig;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.ResourceImpl;

public class RegistryBasedAdminBannerDAO implements AdminAdvisoryBannerDAO {

    protected static final Log LOG = LogFactory.getLog(RegistryBasedAdminBannerDAO.class);
    private static final String ADMIN_ADVISORY_BANNER_PATH = "identity/config/adminAdvisoryBanner";
    private final RegistryResourceConfig registryResourceConfig = new RegistryResourceConfig();

    @Override
    public void saveAdminAdvisoryConfig(AdminAdvisoryBannerDTO adminAdvisoryBanner) throws AdminAdvisoryMgtException {

        AdminAdvisoryBannerDAO adminAdvisoryBannerDAO = AdminAdvisoryManagementDataHolder.getInstance().getAdminAdvisoryBannerDAOService();
    }

    @Override
    public AdminAdvisoryBannerDTO loadAdminAdvisoryConfig() throws AdminAdvisoryMgtException {

        AdminAdvisoryBannerDTO adminAdvisoryBanner;
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();

        Resource registryResource = registryResourceConfig.getRegistryResource(ADMIN_ADVISORY_BANNER_PATH,
                tenantDomain);
        if (registryResource != null) {
            adminAdvisoryBanner = createAdminAdvisoryBannerDTO(registryResource);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Admin advisory banner configuration loaded successfully for tenant: " + tenantDomain);
            }
        } else {
            adminAdvisoryBanner = new AdminAdvisoryBannerDTO();
            adminAdvisoryBanner.setEnableBanner(AdminAdvisoryManagementConstants.ENABLE_BANNER_BY_DEFAULT);
            adminAdvisoryBanner.setBannerContent(AdminAdvisoryManagementConstants.DEFAULT_BANNER_CONTENT);
        }

        return adminAdvisoryBanner;
    }

    /**
     * This method is used to convert AdminAdvisoryBannerDTO to Resource object to be saved in registry.
     *
     * @return Resource object.
     */
    private Resource createAdminBannerRegistryResource(AdminAdvisoryBannerDTO adminAdvisoryBannerDTO) {

        // Set resource properties.
        Resource bannerResource = new ResourceImpl();
        bannerResource.setProperty(AdminAdvisoryManagementConstants.ENABLE_BANNER,
                String.valueOf(adminAdvisoryBannerDTO.getEnableBanner()));
        bannerResource.setProperty(AdminAdvisoryManagementConstants.BANNER_CONTENT,
                String.valueOf(adminAdvisoryBannerDTO.getBannerContent()));

        return bannerResource;
    }

    /**
     * This method is used to convert Resource object to AdminAdvisoryBannerDTO to be saved in registry.
     *
     * @return AdminAdvisoryBannerDTO object.
     */
    private AdminAdvisoryBannerDTO createAdminAdvisoryBannerDTO(Resource bannerResource) {

        AdminAdvisoryBannerDTO adminAdvisoryBannerDTO = new AdminAdvisoryBannerDTO();
        String enableBanner = bannerResource.getProperty(AdminAdvisoryManagementConstants.ENABLE_BANNER);
        String content = bannerResource.getProperty(AdminAdvisoryManagementConstants.BANNER_CONTENT);
        adminAdvisoryBannerDTO.setEnableBanner(Boolean.parseBoolean(enableBanner));
        adminAdvisoryBannerDTO.setBannerContent(content);

        return adminAdvisoryBannerDTO;
    }

}
