package org.folio.rest.tenant.controller;

import static org.folio.rest.tenant.TenantConstants.TENANT_HEADER_NAME;

import java.sql.SQLException;

import org.folio.rest.tenant.model.request.TenantAttributes;
import org.folio.rest.tenant.service.HibernateSchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_/tenant")
public class TenantController {

  @Autowired
  private HibernateSchemaService hibernateSchemaService;

  @PostMapping
  // @formatter:off
  public ResponseEntity<String> create(
    @RequestHeader(required = true, value = TENANT_HEADER_NAME) String tenant,
    @RequestBody @Validated TenantAttributes attributes
  ) throws SQLException {
  // @formatter:on
    hibernateSchemaService.createTenant(tenant);
    return new ResponseEntity<String>("Success", HttpStatus.CREATED);
  }

  @DeleteMapping
  // @formatter:off
  public ResponseEntity<Void> delete(
    @RequestHeader(required = true, value = TENANT_HEADER_NAME) String tenant
  ) throws SQLException {
  // @formatter:on
    hibernateSchemaService.deleteTenant(tenant);
    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
  }

}
