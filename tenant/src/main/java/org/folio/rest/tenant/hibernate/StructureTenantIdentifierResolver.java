package org.folio.rest.tenant.hibernate;

import static org.folio.rest.tenant.TenantConstants.DEFAULT_TENANT;
import static org.folio.rest.tenant.TenantConstants.TENANT_HEADER_NAME;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.folio.rest.tenant.exception.NoTenantHeaderException;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class StructureTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

  @Override
  public String resolveCurrentTenantIdentifier() {
    Optional<HttpServletRequest> request = getRequestFromContext();
    if (request.isPresent()) {
      String tenant = request.get().getHeader(TENANT_HEADER_NAME);
      if (tenant != null) {
        return tenant;
      }
      // NOTE: comment this if wanting to test against default tenant
      throw new NoTenantHeaderException("No tenant header on request!");
    }
    return DEFAULT_TENANT;
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }

  private Optional<HttpServletRequest> getRequestFromContext() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes != null) {
      return Optional.ofNullable(((ServletRequestAttributes) requestAttributes).getRequest());
    }
    return Optional.empty();
  }

}
