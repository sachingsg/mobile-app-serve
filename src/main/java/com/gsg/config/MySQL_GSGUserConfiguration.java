package com.gsg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mysql.properties")
//@EnableJpaRepositories(
//		basePackages = "com.gsg.mysql.user.repository", 
//		entityManagerFactoryRef = "gsgUserEntityManager", 
//		transactionManagerRef = "gsgUserTransactionManager"
//	)
public class MySQL_GSGUserConfiguration {/*

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean gsgUserEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(gsgUserDataSource());
        em.setPackagesToScan(new String[] { "com.gsg.mysql.user.model"});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @Primary
    public DataSource gsgUserDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.mysql.gsg.user.jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("spring.mysql.gsg.user.jdbc.url"));
        dataSource.setUsername(env.getProperty("spring.mysql.gsg.user.user"));
        dataSource.setPassword(env.getProperty("spring.mysql.gsg.user.pass"));

        return dataSource;
    }

    @Bean
    @Primary
    public PlatformTransactionManager gsgUserTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(gsgUserEntityManager().getObject());
        return transactionManager;
    }
*/}
