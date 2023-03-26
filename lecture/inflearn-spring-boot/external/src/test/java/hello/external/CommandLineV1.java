package hello.external;

import lombok.extern.slf4j.Slf4j;

/**
 * 커맨드 라인 인수(Program arguments), 실행시점에 main 메서드의 args 파라미터로 전달
 *
 * java -jar app.jar valueA valueB
 */
@Slf4j
public class CommandLineV1 {

  public static void main(String[] args) {
    for (String arg : args) {
      log.info("arg {}", arg);
    }
  }
}
