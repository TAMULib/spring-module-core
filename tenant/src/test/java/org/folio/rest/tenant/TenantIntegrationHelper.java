package org.folio.rest.tenant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.folio.rest.tenant.TenantConstants;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public abstract class TenantIntegrationHelper {

    protected MockHttpServletRequestBuilder tenantGet(String path) {
        return get(path).header(TenantConstants.TENANT_HEADER_NAME, TenantConstants.DEFAULT_TENANT);
    }

    protected MockHttpServletRequestBuilder tenantPost(String path) {
        return post(path).header(TenantConstants.TENANT_HEADER_NAME, TenantConstants.DEFAULT_TENANT);
    }

    protected MockHttpServletRequestBuilder tenantPut(String path) {
        return put(path).header(TenantConstants.TENANT_HEADER_NAME, TenantConstants.DEFAULT_TENANT);
    }

    protected MockHttpServletRequestBuilder tenantPatch(String path) {
        return patch(path).header(TenantConstants.TENANT_HEADER_NAME, TenantConstants.DEFAULT_TENANT);
    }

    protected MockHttpServletRequestBuilder tenantDelete(String path) {
        return delete(path).header(TenantConstants.TENANT_HEADER_NAME, TenantConstants.DEFAULT_TENANT);
    }

}
