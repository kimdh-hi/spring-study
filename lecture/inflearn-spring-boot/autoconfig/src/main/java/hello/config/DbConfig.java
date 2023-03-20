package hello.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionManager;

@Slf4j
//@Configuration
public class DbConfig {

  @Bean
  public DataSource dataSource() {
    log.info("datasource bean create");
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setJdbcUrl("jdbc:h2:mem:test");
    dataSource.setUsername("sa");
    dataSource.setPassword("");

    return dataSource;
  }

  @Bean
  public TransactionManager transactionManager() {
    log.info("transactionManager bean create");
    return new JdbcTransactionManager(dataSource());
  }

  @Bean
  public JdbcTemplate jdbcTemplate() {
    log.info("jdbcTemplate bean create");
    return new JdbcTemplate(dataSource());
  }
}
