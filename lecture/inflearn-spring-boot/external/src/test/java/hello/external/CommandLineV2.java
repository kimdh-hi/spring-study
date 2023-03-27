package hello.external;

import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

/**
 * java -jar app.jar --key1=value1 --key2=value2 key3=value3
 *
 * -- 가 붙는 시작하는 인수는 옵션인수
 */
@Slf4j
public class CommandLineV2 {


  public static void main(String[] args) {
    for (String arg : args) {
      log.info("arg {}", arg);
    }

    ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
    log.info("SourceArgs = {}", List.of(applicationArguments.getSourceArgs()));
    log.info("NonOptionArgs = {}", applicationArguments.getNonOptionArgs());
    log.info("OptionNames = {}", applicationArguments.getOptionNames());

    Set<String> optionNames = applicationArguments.getOptionNames();
    for (String optionName : optionNames) {
      log.info("option arg {}={}", optionName, applicationArguments.getOptionValues(optionName));
    }
  }
}
