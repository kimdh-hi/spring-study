package hello;

import hello.config.MyDataSourceConfigV1;
import hello.config.MyDataSourceValueConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"hello.datasource"})
//@Import(MyDataSourceValueConfig.class)
@Import(MyDataSourceConfigV1.class)
public class ExternalReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalReadApplication.class, args);
    }

}
