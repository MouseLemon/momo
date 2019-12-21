package com.joysupply.config;

import javax.sql.DataSource;
 
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
@Configuration
@EnableTransactionManagement
public class DataSourceConfig implements EnvironmentAware {
	private Environment env;
	@Override
	public void setEnvironment(Environment env) {
		this.env=env;
	}
	@Bean
	public DataSource dataSource() {
		try {
			HikariConfig config = new HikariConfig(); 
			config.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
			config.setJdbcUrl(env.getProperty("spring.datasource.url"));
			config.setUsername(env.getProperty("spring.datasource.username"));
			config.setPassword(env.getProperty("spring.datasource.password"));
			config.setPoolName(env.getProperty("spring.datasource.pool-name"));
			config.setMaximumPoolSize(Integer.parseInt(env.getProperty("spring.datasource.maximum-pool-size")));
			config.setMinimumIdle(Integer.parseInt(env.getProperty("spring.datasource.minimum-idle")));
			config.setConnectionTestQuery(env.getProperty("spring.datasource.connection-test-query"));
			config.setAutoCommit(false);  
			return new HikariDataSource(config);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null; 
	} 
}
