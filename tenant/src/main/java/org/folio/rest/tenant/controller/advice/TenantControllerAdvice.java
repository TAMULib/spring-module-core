package org.folio.rest.tenant.controller.advice;

import java.sql.SQLException;

import org.folio.rest.tenant.exception.TenantAlreadyExistsException;
import org.folio.rest.tenant.exception.TenantDoesNotExistsException;
import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class TenantControllerAdvice {

  @ExceptionHandler(value = SQLException.class)
  public ResponseEntity<String> hasndleSQLException(SQLException exception) {
    return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = HibernateException.class)
  public ResponseEntity<String> hasndleHibernateException(HibernateException exception) {
    return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = TenantAlreadyExistsException.class)
  public ResponseEntity<String> hasndleTenantAlreadyExistsException(TenantAlreadyExistsException exception) {
    return new ResponseEntity<String>(exception.getMessage(), HttpStatus.NO_CONTENT);
  }

  @ExceptionHandler(value = TenantDoesNotExistsException.class)
  public ResponseEntity<String> hasndleTenantDoesNotExistsException(TenantDoesNotExistsException exception) {
    return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

}
