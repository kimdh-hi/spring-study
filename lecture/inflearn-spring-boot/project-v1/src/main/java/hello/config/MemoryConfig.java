package hello.config;

import memory.MemoryController;
import memory.MemoryFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//외부 라이브러리에 대해 어떤 빈을 등록해야 하는지 알아야 함 (직접 등록해서 써야하기 때문)
@Configuration
public class MemoryConfig {

  @Bean
  public MemoryFinder memoryFinder() {
    return new MemoryFinder();
  }

  @Bean
  public MemoryController memoryController() {
    return new MemoryController(memoryFinder());
  }

}
