package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import test.MyConfiguration.MyCustomBeanEntityManagerFactoryDependsOnPostProcessor;

@Configuration
//TODO: 1 of 2: comment following line to work
@AutoConfigureBefore(HibernateJpaAutoConfiguration.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
@Import({MyCustomBeanEntityManagerFactoryDependsOnPostProcessor.class})
public class MyConfiguration {
    private static final Logger log = LoggerFactory.getLogger( MyConfiguration.class );

    //TODO: 2 of 2: comment following bean to work
    @Bean
    public PlatformTransactionManager transactionManager(
            ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers ) {
        //insert my custom transaction manager logic here
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManagerCustomizers.ifAvailable( ( customizers ) -> customizers.customize( transactionManager ) );
        return transactionManager;
    }

    @ConditionalOnClass(LocalContainerEntityManagerFactoryBean.class)
    @ConditionalOnBean(AbstractEntityManagerFactoryBean.class)
    static class MyCustomBeanEntityManagerFactoryDependsOnPostProcessor
            extends EntityManagerFactoryDependsOnPostProcessor {
        MyCustomBeanEntityManagerFactoryDependsOnPostProcessor() {
            super( MyCustomBean.class );
            log.info( "MyCustomBeanPostProcessor created" );
        }
    }

    private static class MyCustomBean implements InitializingBean {
        public MyCustomBean() {
            log.info( "My bean created" );
        }

        @Override public void afterPropertiesSet() {
            log.info( "My bean afterPropertiesSet" );
        }
    }

    @Bean
    public MyCustomBean myCustomBean() {
        return new MyCustomBean();
    }
}
