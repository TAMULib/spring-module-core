package org.folio.rest.tenant.exception;

public class TenantDoesNotExistsException extends RuntimeException {

  private static final long serialVersionUID = -1398289121513584030L;

  public TenantDoesNotExistsException(String message) {
    super(message);
  }

}
