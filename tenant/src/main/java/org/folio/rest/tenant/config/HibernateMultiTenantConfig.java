package org.folio.rest.tenant.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class HibernateMultiTenantConfig {

  @Autowired
  private JpaProperties jpaProperties;

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  // @formatter:off
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
    DataSource dataSource,
    MultiTenantConnectionProvider multiTenantConnectionProvider,
    CurrentTenantIdentifierResolver currentTenantIdentifierResolver
  ) {
  // @formatter:on
    Map<String, Object> properties = new HashMap<>();
    properties.putAll(jpaProperties.getProperties());
    properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
    properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
    properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("org.folio.rest");
    em.setJpaVendorAdapter(jpaVendorAdapter());
    em.setJpaPropertyMap(properties);
    return em;
  }

}
