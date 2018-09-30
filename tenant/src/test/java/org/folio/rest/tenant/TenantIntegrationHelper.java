package org.folio.rest.tenant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public abstract class TenantIntegrationHelper {

  protected MockHttpServletRequestBuilder tenantGet(String tenant, String path) {
    return get(path).header(TenantConstants.TENANT_HEADER_NAME, tenant);
  }

  protected MockHttpServletRequestBuilder tenantPost(String tenant, String path) {
    return post(path).header(TenantConstants.TENANT_HEADER_NAME, tenant);
  }

  protected MockHttpServletRequestBuilder tenantPut(String tenant, String path) {
    return put(path).header(TenantConstants.TENANT_HEADER_NAME, tenant);
  }

  protected MockHttpServletRequestBuilder tenantPatch(String tenant, String path) {
    return patch(path).header(TenantConstants.TENANT_HEADER_NAME, tenant);
  }

  protected MockHttpServletRequestBuilder tenantDelete(String tenant, String path) {
    return delete(path).header(TenantConstants.TENANT_HEADER_NAME, tenant);
  }

}
