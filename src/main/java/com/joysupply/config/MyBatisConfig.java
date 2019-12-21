package com.joysupply.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver; 
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
@Configuration 
@AutoConfigureAfter({ DataSourceConfig.class }) 
public class MyBatisConfig implements EnvironmentAware{
	@Autowired
	private DataSource dataSource;
	private Environment env;
	@Override
	public void setEnvironment(Environment env) {
		this.env=env;
	}
	@Bean
	public SqlSessionFactory sqlSessionFactory() {
		try {
			SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
			sessionFactory.setDataSource(dataSource); 
			sessionFactory.setTypeAliasesPackage(env.getProperty("mybatis.type-aliases-package"));
			sessionFactory.setConfigLocation(new ClassPathResource(env.getProperty("mybatis.config-location")));
			sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapper-locations")));
			sessionFactory.setVfs(SpringBootVFS.class);
			return sessionFactory.getObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate() {
		return new SqlSessionTemplate(sqlSessionFactory(), ExecutorType.BATCH);
	}
	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
	
}
