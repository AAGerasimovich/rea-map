package ru.dkt.persistence;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan({"ru.dkt"})
@EnableJpaRepositories(basePackages = "ru.dkt.model.repository")
@RequiredArgsConstructor
public class PersistenceJPAConfig {

    private final Environment env;

    @Bean
    @ConfigurationProperties("db.datasource.properties")
    public Properties propertiesDataSourceApp() {
        return new Properties();
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier(value = "dataSourceApp") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("ru.dkt");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }


    @Bean
    @Primary
    @ConfigurationProperties("db.app.datasource")
    public ComboPooledDataSource dataSourceApp() {
        return DataSourceBuilder.create().type(ComboPooledDataSource.class).build();
    }

    @Bean
    @Autowired
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean em) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(em.getObject());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public Properties additionalProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
        hibernateProperties.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQL9Dialect");
        return hibernateProperties;
    }

    @ConditionalOnProperty(name = "liquibase.run", havingValue = "true")
    @Primary
    @Bean
    @Autowired
    public SpringLiquibase primaryLiquibase(@Qualifier(value = "dataSourceApp") DataSource dataSourceApp) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(env.getProperty("spring.liquibase.change-log", String.class));
        liquibase.setDataSource(dataSourceApp);
        liquibase.setShouldRun(true);
        return liquibase;
    }

    @ConditionalOnProperty(name = "liquibaseToken.run", havingValue = "true")
    @Bean
    @Autowired
    public SpringLiquibase secondaryLiquibase(@Qualifier(value = "dataSourceToken") DataSource dataSourceToken) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(env.getProperty("liquibaseToken.change-log", String.class));
        liquibase.setDataSource(dataSourceToken);
        liquibase.setShouldRun(true);
        return liquibase;
    }

}
