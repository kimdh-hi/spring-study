package hello.config;

import hello.datasource.MyDataSource;
import java.time.Duration;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MyDataSourceEnvConfig {

  private Environment env;

  public MyDataSourceEnvConfig(Environment env) {
    this.env = env;
  }

  @Bean
  public MyDataSource myDataSource() {
    String url = env.getProperty("my.datasource.url");
    String username = env.getProperty("my.datasource.username");
    String password = env.getProperty("my.datasource.password");
    Integer maxConnection = env.getProperty("my.datasource.etc.max-connection", Integer.class);
    Duration timeout = env.getProperty("my.datasource.etc.timeout", Duration.class);
    List<String> options = env.getProperty("my.datasource.etc.options", List.class);

    return new MyDataSource(url, username, password, maxConnection, timeout, options);
  }
}
