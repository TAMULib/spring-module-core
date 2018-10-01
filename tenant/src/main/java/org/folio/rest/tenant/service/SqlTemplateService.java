package org.folio.rest.tenant.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class SqlTemplateService {

  private final static String SCHEMA_IMPORT_INITIAL = "import/initial";
  private final static String SCHEMA = "schema";

  @Autowired
  private SpringTemplateEngine templateEngine;

  public String templateImportSql(String schema) {
    return templateEngine.process(SCHEMA_IMPORT_INITIAL, createContext(SCHEMA, schema));
  }

  private Context createContext(String modelName, Object model) {
    Context ctx = new Context(Locale.getDefault());
    ctx.setVariable(modelName, model);
    return ctx;
  }

}
