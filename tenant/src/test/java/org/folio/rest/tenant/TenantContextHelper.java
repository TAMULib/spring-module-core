package org.folio.rest.tenant;

import javax.annotation.PreDestroy;

import org.folio.rest.tenant.TenantConstants;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class TenantContextHelper {

    public abstract void addTenantToContext();

    @PreDestroy
    public void removeTenantFromContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    protected void addTenantToContext(String tenant) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(TenantConstants.TENANT_HEADER_NAME, tenant);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

}
