package com.toy.springcore.`04-property`

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

/**
 * 우선순위
 * @SpringBootTest(properties) > test.resources.test-property.yml > main.resources.test-property.yml
 *
 * test.resources 에 test-property.yml (main.resources 의 yml 과 이름이 같은) 을 두는 것은 별로임 프로퍼티의 값은 같을 필요는 없지만 모든 키를 갖고 있어야 함
 *
 * test.resource 에 전용 프로퍼티 파일을 두고 싶다면 이름이 다른 파일을 사용하자.
 * 파일을 사용할 때는 @TestPropertySource 로 지정하자.
 */
//@SpringBootTest(properties = ["test-property.data=boot-config"])
@SpringBootTest
@TestPropertySource(locations = ["classpath:test-property.yml"])
class PropertyTest {

  @Value("\${test-property.data}")
  lateinit var data: String

  @Value("\${custom.number}")
  lateinit var number: String

  @Test
  fun test() {
    println(data)
    println(number)
  }
}