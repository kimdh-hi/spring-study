package hello;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 커맨드 라인 옵션 인수
 * 자바 시스템 속성
 * OS 환경변수
 * 설정파일
 *
 * 모두 읽을 수 있음
 *
 * 위 속성들의 우선순위
 *   - 변경하기 쉬운, 유연한 쪽이 높다.
 *   - 번위가 넓은 것보다 좁은 것이 우선권을 가진다. (커맨드 라인 옵션 인수 > 자바 시스템 속성
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class EnvironmentCheck {

  private final Environment environment;

  @PostConstruct
  public void init() {
    String url = environment.getProperty("url");
    String username = environment.getProperty("username");
    String password = environment.getProperty("password");
    log.info("url={}, username={}, password={}", url, username, password);
  }
}
