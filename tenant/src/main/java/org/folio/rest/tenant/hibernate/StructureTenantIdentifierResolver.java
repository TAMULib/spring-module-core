package org.folio.rest.tenant.hibernate;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.folio.rest.tenant.TenantConstants;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class StructureTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

  @Override
  public String resolveCurrentTenantIdentifier() {
    Optional<HttpServletRequest> request = getRequestFromContext();
    if (request.isPresent()) {
      String tenant = request.get().getHeader(TenantConstants.TENANT_HEADER_NAME);
      if (tenant != null) {
        return tenant;
      }
      // throw new NoTenantHeaderException("No tenant header on request!");
    }
    return TenantConstants.DEFAULT_TENANT;
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }

  private Optional<HttpServletRequest> getRequestFromContext() {
    Optional<ServletRequestAttributes> servletRequestAttributes = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    if (servletRequestAttributes.isPresent()) {
      return Optional.ofNullable(servletRequestAttributes.get().getRequest());
    }
    return Optional.empty();
  }

}
