package hello.pay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
public class PayConfig {

  @Bean
  @Profile("default")
  public LocalPayClient localPayClient() {
    log.info("LocalPayClient bean");
    return new LocalPayClient();
  }

  @Bean
  @Profile("prod")
  public ProdPayClient payPayClient() {
    log.info("ProdPayClient bean");
    return new ProdPayClient();
  }
}

