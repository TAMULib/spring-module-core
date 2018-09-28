package org.folio.rest.tenant.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;

import org.folio.rest.tenant.TenantConstants;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

@Service
public class HibernateSchemaService {

  private final static String CONNECTION_DRIVER_CLASS = "connection.driver_class";
  private final static String DIALECT = "dialect";
  private final static String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
  private final static String HIBERNATE_DEFAULT_SCHEMA = "hibernate.default_schema";
  private final static String HIBERNATE_JDBC_LOB_NON_CONTEXTUAL_CREATION = "hibernate.jdbc.lob.non_contextual_creation";
  private final static String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
  private final static String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";
  private final static String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

  // @formatter:off
  private static String[] defaultBaseEntityPackages = new String[] { "org.folio.rest.model" };
  // @formatter:on

  @Autowired
  private SqlTemplateService sqlTemplateService;

  @Autowired
  private DataSourceProperties dataSourceProperties;

  @Autowired
  private JpaProperties jpaProperties;

  @PostConstruct
  public void initialize() throws SQLException {
    initializeSchema(TenantConstants.DEFAULT_TENANT, defaultBaseEntityPackages);
  }

  public void initializeTenant(String tenant) throws SQLException {
    initializeSchema(tenant, defaultBaseEntityPackages);
  }

  public void deleteTenant(String tenant) throws SQLException {
    dropSchema(tenant);
  }

  public void initializeSchema(String schema, String[] baseEntityPackages) throws SQLException {
    Map<String, String> settings = getSettings(schema);
    createSchemaIfNotExists(settings);
    MetadataImplementor metadata = buildMetadata(settings, baseEntityPackages);
    SchemaExport schemaExport = new SchemaExport();
    schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata);
    initializeSchemaData(settings);
  }

  private Map<String, String> getSettings(String schema) {
    Map<String, String> settings = new HashMap<String, String>();
    settings.put(CONNECTION_DRIVER_CLASS, dataSourceProperties.getDriverClassName());
    settings.put(DIALECT, jpaProperties.getDatabasePlatform());
    settings.put(HIBERNATE_CONNECTION_URL, dataSourceProperties.getUrl());
    settings.put(HIBERNATE_DEFAULT_SCHEMA, schema);
    settings.put(HIBERNATE_JDBC_LOB_NON_CONTEXTUAL_CREATION, "true");
    settings.put(HIBERNATE_CONNECTION_USERNAME, dataSourceProperties.getUsername());
    settings.put(HIBERNATE_CONNECTION_PASSWORD, dataSourceProperties.getPassword());
    settings.put(HIBERNATE_HBM2DDL_AUTO, "none");
    settings.put("show_sql", String.valueOf(jpaProperties.isShowSql()));
    return settings;
  }

  private void createSchemaIfNotExists(Map<String, String> settings) throws SQLException {
    String jdbcUrl = settings.get(HIBERNATE_CONNECTION_URL);
    String username = settings.get(HIBERNATE_CONNECTION_USERNAME);
    String password = settings.get(HIBERNATE_CONNECTION_PASSWORD);
    String schema = settings.get(HIBERNATE_DEFAULT_SCHEMA);
    Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
    Statement statement = connection.createStatement();
    statement.executeUpdate(String.format("CREATE SCHEMA IF NOT EXISTS %s;", schema));
    statement.close();
    connection.close();
  }

  private void dropSchema(String schema) throws SQLException {
    Map<String, String> settings = getSettings(schema);
    String jdbcUrl = settings.get(HIBERNATE_CONNECTION_URL);
    String username = settings.get(HIBERNATE_CONNECTION_USERNAME);
    String password = settings.get(HIBERNATE_CONNECTION_PASSWORD);
    Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
    Statement statement = connection.createStatement();
    statement.executeUpdate(String.format("DROP SCHEMA IF EXISTS %s CASCADE;", schema));
    statement.close();
    connection.close();
  }

  private MetadataImplementor buildMetadata(Map<String, String> settings, String[] baseEntityPackages) {
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(settings).build();
    MetadataSources sources = addEntities(new MetadataSources(registry), baseEntityPackages);
    return (MetadataImplementor) sources.getMetadataBuilder().build();
  }

  private MetadataSources addEntities(MetadataSources sources, String[] baseEntityPackages) {
    ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
    for (String baseEntityPackage : baseEntityPackages) {
      for (BeanDefinition beanDefinition : scanner.findCandidateComponents(baseEntityPackage)) {
        sources.addAnnotatedClassName(beanDefinition.getBeanClassName());
      }
    }
    return sources;
  }

  private void initializeSchemaData(Map<String, String> settings) throws SQLException {
    String jdbcUrl = settings.get(HIBERNATE_CONNECTION_URL);
    String username = settings.get(HIBERNATE_CONNECTION_USERNAME);
    String password = settings.get(HIBERNATE_CONNECTION_PASSWORD);
    String schema = settings.get(HIBERNATE_DEFAULT_SCHEMA);
    Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
    Statement statement = connection.createStatement();
    statement.execute(sqlTemplateService.templateImportSql(schema));
    statement.close();
    connection.close();
  }

}
