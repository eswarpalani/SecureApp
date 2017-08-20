package com.virtusa.virtusabank.secureapp;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;

@Configuration
class PersistenceContext implements EnvironmentAware {

	@SuppressWarnings("unused")
	private RelaxedPropertyResolver jpaPropertyResolver;
	
	@Autowired(required = false)
	private PersistenceUnitManager persistenceUnitManager;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/secureapp");
		dataSource.setUsername("root");
		return dataSource;
	}

	@Bean
	@DependsOn("jdbcTemplate")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
			JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		if (persistenceUnitManager != null) {
			entityManagerFactoryBean.setPersistenceUnitManager(persistenceUnitManager);
		}
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setPackagesToScan("com.virtusa.virtusabank.secureapp.model");
		Map<String, Object> properties = entityManagerFactoryBean.getJpaPropertyMap();
		
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.format_sql", "true");
		properties.put("hibernate.use_sql_comments", "true");
		properties.put("hibernate.hbm2ddl.auto", "update");
		return entityManagerFactoryBean;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.jpaPropertyResolver = new RelaxedPropertyResolver(environment, "spring.jpa.");
	}
}