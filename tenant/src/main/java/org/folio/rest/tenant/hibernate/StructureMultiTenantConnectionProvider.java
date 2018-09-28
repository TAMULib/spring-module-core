package org.folio.rest.tenant.hibernate;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.folio.rest.tenant.TenantConstants;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StructureMultiTenantConnectionProvider implements MultiTenantConnectionProvider {

  private static final long serialVersionUID = 5748544845255283079L;

  @Value("${spring.datasource.platform:h2}")
  private String platform;

  @Autowired
  private DataSource dataSource;

  @Override
  public Connection getAnyConnection() throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @Override
  public Connection getConnection(String tenantIdentifier) throws SQLException {
    final Connection connection = getAnyConnection();
    try {

      switch (platform) {
      case "h2":
        connection.createStatement().execute("USE " + tenantIdentifier);
        break;
      case "postgres":
        connection.setSchema(tenantIdentifier);
        break;
      default:
        throw new HibernateException("Unknown datasource platform [" + platform + "]");
      }

      // H2
      // connection.createStatement().execute("USE " + tenantIdentifier);
      // connection.createStatement().execute("SET SCHEMA " + tenantIdentifier);

      // PostgreSql
      // connection.setSchema(tenantIdentifier);
      // connection.createStatement().execute("SET SCHEMA '" + tenantIdentifier + "';");
    } catch (SQLException e) {
      throw new HibernateException("Could not alter JDBC connection to use schema [" + tenantIdentifier + "]", e);
    }
    return connection;
  }

  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
    try {
      switch (platform) {
      case "h2":
        connection.createStatement().execute("USE " + TenantConstants.DEFAULT_TENANT);
        break;
      case "postgres":
        connection.setSchema(TenantConstants.DEFAULT_TENANT);
        break;
      default:
        throw new HibernateException("Unknown datasource platform [" + platform + "]");
      }

      // H2
      // connection.createStatement().execute("USE " + DEFAULT_TENANT);
      // connection.createStatement().execute("SET SCHEMA " + DEFAULT_TENANT);

      // PostgreSql
      // connection.setSchema(DEFAULT_TENANT);
      // connection.createStatement().execute("SET SCHEMA '" + DEFAULT_TENANT + "';");
    } catch (SQLException e) {
      throw new HibernateException(
          "Could not alter JDBC connection to use schema [" + TenantConstants.DEFAULT_TENANT + "]", e);
    }
    connection.close();
  }

  @Override
  @SuppressWarnings("rawtypes")
  public boolean isUnwrappableAs(Class unwrapType) {
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> unwrapType) {
    return null;
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return true;
  }

}
