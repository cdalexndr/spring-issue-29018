package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.MyConfiguration.MyCustomBeanEntityManagerFactoryDependsOnPostProcessor;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MyTest {
    @Autowired BeanFactory beanFactory;

    @Test
    public void myTestMethod() {
        beanFactory.getBean( MyCustomBeanEntityManagerFactoryDependsOnPostProcessor.class );
    }
}
