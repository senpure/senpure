package com.senpure.base.configure;


import com.senpure.base.util.DatabaseUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * copy from DruidDataSourceAutoConfigure
 */


//@MapperScan(basePackages = "com.senpure", sqlSessionFactoryRef = "sqlSessionFactory")
//@EnableTransactionManagement
//@EnableJpaRepositories
//@ConditionalOnMissingBean(name = "dataSource")
public class DataSourceAutoConfiguration extends BaseConfiguration {

    @Bean(name = "dataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource dataSource() {
        DataSourceProperties prop = dataSourceProperties();
        DatabaseUtil.checkAndCreateDatabase(prop);
       // return DruidDataSourceBuilder.create().build();
        return  null;
    }


    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        //PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //??????mapper????????????

        // sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mapperLocations));
        // logger.info("sqlsessionFactory {}", mapperLocations);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(
            @Qualifier("dataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }


    @Bean(name = "entityManager")
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactorySecondary(builder).getObject().createEntityManager();
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.jpa")
    @Primary
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactorySecondary(EntityManagerFactoryBuilder builder) {

        return builder
                .dataSource(dataSource())
                .properties(jpaProperties().getProperties())
                .packages("com.senpure") //???????????????????????????
                .persistenceUnit("persistenceUnit")
                .build();
    }


    // @Bean
    public ServletRegistrationBean DruidStatViewServlet() {
        //org.springframework.boot.context.embedded.ServletRegistrationBean????????????????????????.
        ServletRegistrationBean servletRegistrationBean;
                //= new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //servletRegistrationBean.setLoadOnStartup(-2);
        //????????????????????????initParams

        //????????????
        //servletRegistrationBean.addInitParameter("allow","127.0.0.1");
        //IP????????? (??????????????????deny?????????allow) : ????????????deny????????????:Sorry, you are not permitted to view this page.
        //servletRegistrationBean.addInitParameter("deny","192.168.1.129");
        //?????????????????????????????????.
        // servletRegistrationBean.addInitParameter("loginUsername",account);
        // servletRegistrationBean.addInitParameter("loginPassword",passwrod);
        //????????????????????????.
        logger.debug("self statViewServlet");
       // servletRegistrationBean.addInitParameter("resetEnable", "false");
        //return servletRegistrationBean;
        return null;
    }


}
