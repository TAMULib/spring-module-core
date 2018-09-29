package org.folio.rest.tenant.controller.advice;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class TenantControllerAdvice {

  @ResponseStatus(value = HttpStatus.CONFLICT)
  @ExceptionHandler(value = SQLException.class)
  public ResponseEntity<String> hasndleSQLException(SQLException exception) {
    return ResponseEntity.ok("SQL Exception");
  }

}
