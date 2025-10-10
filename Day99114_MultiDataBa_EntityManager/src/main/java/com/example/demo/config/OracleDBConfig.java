package com.example.demo.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.demo.doctor.repository", entityManagerFactoryRef = "oracleEntityManagerFactory", transactionManagerRef = "oracleTransactionManager")
public class OracleDBConfig {

	@Bean(name = "oracleDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.oracle")
	public DataSource oracleDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "oracleEntityManagerFactory")
	@Primary                                                                  //one of the Entity manager factory has to be primary
	public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(
			@Qualifier("oracleDataSource") DataSource dataSource) {

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.example.demo.doctor.entity"); // Scan only Oracle entities
		em.setPersistenceUnitName("oracle");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", "update"); // auto-create tables
		properties.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect"); // Oracle 10g
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean(name = "oracleTransactionManager")
	public PlatformTransactionManager oracleTransactionManager(
			@Qualifier("oracleEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}

// com.example.demo.doctor.entity

//com.hospital.doctor.entity
