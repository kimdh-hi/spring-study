package hello;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandLineBean {

  private final ApplicationArguments applicationArguments;

  public CommandLineBean(ApplicationArguments applicationArguments) {
    this.applicationArguments = applicationArguments;
  }

  @PostConstruct
  public void postConstruct() {
    log.info("SourceArgs = {}", List.of(applicationArguments.getSourceArgs()));
    log.info("NonOptionArgs = {}", applicationArguments.getNonOptionArgs());
    log.info("OptionNames = {}", applicationArguments.getOptionNames());

    Set<String> optionNames = applicationArguments.getOptionNames();
    for (String optionName : optionNames) {
      log.info("option arg {}={}", optionName, applicationArguments.getOptionValues(optionName));
    }
  }
}
