package com.example.demo.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.demo.patient.repository", entityManagerFactoryRef = "postgresEntityManagerFactory", transactionManagerRef = "postgresTransactionManager")
public class PostgresDBConfig {

	@Bean(name = "postgresDataSource")
	@Primary        												//one of the data source has to be primary
	@ConfigurationProperties(prefix = "spring.datasource.postgres") 
	public DataSource postgresDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "postgresEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
			@Qualifier("postgresDataSource") DataSource dataSource) {

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.example.demo.patient.entity"); // Scan only Postgres entities
		em.setPersistenceUnitName("postgres");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", "update"); // auto-create tables
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"); // Postgres dialect
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean(name = "postgresTransactionManager")
	public PlatformTransactionManager postgresTransactionManager(
			@Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}
