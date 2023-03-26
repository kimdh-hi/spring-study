package hello.external;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/**
 * jvm 시스템 속성
 *
 * -Dkey1=value1 -Dkey2=value2
 * VM option
 */
@Slf4j
public class JvmProperties {

  public static void main(String[] args) {
    System.setProperty("myKey", "myValue");

    Properties properties = System.getProperties();
    for (Object key : properties.keySet()) {
      log.info("property {}={}", key, properties.get(key));
    }

    String myValue = properties.getProperty("myKey");
    log.info("property myKey={}", myValue);
  }
}
