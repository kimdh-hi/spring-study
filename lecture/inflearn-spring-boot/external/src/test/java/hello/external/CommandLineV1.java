package hello.external;

import lombok.extern.slf4j.Slf4j;

/**
 * 커맨드 라인 인수(Program arguments), 실행시점에 main 메서드의 args 파라미터로 전달
 * 기본적으로 key-value 형태가 아님
 * java -jar app.jar valueA valueB
 *
 * java -jar app.jar key1=value1 key2=value2
 * key-value(map) 로 변환하여 사용해야 하는 불편함
 *
 * 스프링은 위와 같은 불편함을 해결하기 위해 커맨드 라인 옵션 인수(--)를 제공한다.
 * java -jar app.jar --key1=value1 --key2=value2
 */
@Slf4j
public class CommandLineV1 {

  public static void main(String[] args) {
    for (String arg : args) {
      log.info("arg {}", arg);
    }
  }
}
