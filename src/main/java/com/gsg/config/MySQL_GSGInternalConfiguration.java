package com.gsg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mysql.properties")
//@EnableJpaRepositories(
//		basePackages = "com.gsg.mysql.internal.repository", 
//		entityManagerFactoryRef = "gsgInternalEntityManager", 
//		transactionManagerRef = "gsgInternalTransactionManager"
//	)
public class MySQL_GSGInternalConfiguration {/*

    @Autowired
    private Environment env;

    @Bean
    //@Primary
    public LocalContainerEntityManagerFactoryBean gsgInternalEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(gsgInternalDatasource());
        em.setPackagesToScan(new String[] { "com.gsg.mysql.internal.model"});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    //@Primary
    public DataSource gsgInternalDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.mysql.gsg.internal.jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("spring.mysql.gsg.internal.jdbc.url"));
        dataSource.setUsername(env.getProperty("spring.mysql.gsg.internal.user"));
        dataSource.setPassword(env.getProperty("spring.mysql.gsg.internal.pass"));

        return dataSource;
    }

    @Bean
    //@Primary
    public PlatformTransactionManager gsgInternalTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(gsgInternalEntityManager().getObject());
        return transactionManager;
    }
*/}
