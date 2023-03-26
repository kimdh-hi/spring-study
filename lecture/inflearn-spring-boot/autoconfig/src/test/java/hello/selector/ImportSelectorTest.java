package hello.selector;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * spring AutoConfiguration 에서 흔히 사용하는 방식
 *
 * org.springframework.boot.autoconfigure.AutoConfiguration.imports
 */
public class ImportSelectorTest {

  @Test
  public void test1() {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ImportTestConfig.class);
    HelloBean helloBean = applicationContext.getBean(HelloBean.class);
    Assertions.assertThat(helloBean).isNotNull();
  }

  @Test
  public void test2() {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ImportSelectorTestConfig.class);
    HelloBean helloBean = applicationContext.getBean(HelloBean.class);
    Assertions.assertThat(helloBean).isNotNull();
  }

  @Configuration
  @Import(HelloConfig.class)
  public static class ImportTestConfig {

  }

  @Configuration
  @Import(HelloImportSelector.class)
  public static class ImportSelectorTestConfig {

  }
}
